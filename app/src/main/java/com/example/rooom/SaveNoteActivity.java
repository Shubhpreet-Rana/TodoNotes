package com.example.rooom;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SaveNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "com.example.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.example.EXTRA_PRIORITY";

    EditText title, description;
    private NumberPicker priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_note);

        title = findViewById(R.id.edit_text_title);
        description = findViewById(R.id.edit_text_description);
        priority = findViewById(R.id.priority_picker);
        priority.setMaxValue(10);
        priority.setMinValue(1);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("EditText");
            title.setText(intent.getStringExtra(EXTRA_TITLE));
            description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priority.setValue(Integer.parseInt(String.valueOf(intent.getIntExtra(EXTRA_PRIORITY,1))));
        } else {
            setTitle("Add Note");
        }
    }

    private void SaveNote() {
        String edit_description = description.getText().toString();
        String edit_title = title.getText().toString();
        int edit_priority = priority.getValue();

        if (edit_title.trim().isEmpty() || edit_description.trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, edit_title);
        data.putExtra(EXTRA_DESCRIPTION, edit_description);
        data.putExtra(EXTRA_PRIORITY, edit_priority);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1 ){
            data.putExtra(EXTRA_ID,id);
        }
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                SaveNote();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}