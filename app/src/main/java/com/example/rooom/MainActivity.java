package com.example.rooom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel viewModel;
    ActivityResultLauncher<Intent> launcherInsert,launcherUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = (FloatingActionButton) findViewById(R.id.new_note_page);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveNoteActivity.class);
                launcherInsert.launch(intent);
            }
        });
        launcherInsert = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK ) {
                        Intent data = result.getData();
                        String title = data.getStringExtra(SaveNoteActivity.EXTRA_TITLE);
                        String description = data.getStringExtra(SaveNoteActivity.EXTRA_DESCRIPTION);
                        String priority = String.valueOf(data.getIntExtra(SaveNoteActivity.EXTRA_PRIORITY, 1));

                        Note note = new Note(title, description, priority);
                        viewModel.insert(note);
                        Toast.makeText(MainActivity.this, "Note Saved ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Note Saved Error ", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        launcherUpdate = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK ) {
                        Intent data = result.getData();
                        int id = data.getIntExtra(SaveNoteActivity.EXTRA_ID, -1);
                        if (id == -1) {
                            Toast.makeText(MainActivity.this, "noTE Not Updated ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String title = data.getStringExtra(SaveNoteActivity.EXTRA_TITLE);
                        String description = data.getStringExtra(SaveNoteActivity.EXTRA_DESCRIPTION);
                        String priority = String.valueOf(data.getIntExtra(SaveNoteActivity.EXTRA_PRIORITY, 1));

                        Note note = new Note(title, description, priority);
                        note.setId(id);
                        viewModel.update(note);
                        Toast.makeText(MainActivity.this, "Note UPdated ", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Note Update  Error ", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RecyclerView recyclerView = findViewById(R.id.note_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        viewModel.getAllNote().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getAdapterPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickEvent(note -> {
            Intent intent = new Intent(MainActivity.this, SaveNoteActivity.class);
            intent.putExtra(SaveNoteActivity.EXTRA_ID,note.getId());
            intent.putExtra(SaveNoteActivity.EXTRA_TITLE,note.getTitle());
            intent.putExtra(SaveNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
            intent.putExtra(SaveNoteActivity.EXTRA_PRIORITY,note.getPriority());
            launcherUpdate.launch(intent);
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_deleteAll:
                viewModel.deleteAll();
                Toast.makeText(this, "Deleted All Notes", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}