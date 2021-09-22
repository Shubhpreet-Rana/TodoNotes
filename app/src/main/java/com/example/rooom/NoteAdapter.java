package com.example.rooom;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note , NoteAdapter.NoteHolder> {

    private OnItemClickEvent listenet;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority().equals(newItem.getPriority());
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.priority.setText(currentNote.getPriority());
        holder.description.setText(currentNote.getDescription());
        holder.title.setText(currentNote.getTitle());
    }


    public Note getAdapterPosition(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView priority, title, description;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            priority = itemView.findViewById(R.id.text_view_priority);
            title = itemView.findViewById(R.id.text_view_title);
            description = itemView.findViewById(R.id.text_view_description);
            itemView.setOnClickListener(view -> {
                MainActivity activity = new MainActivity();
                Log.i("sds","sdkshjksjks");
                int position = getAdapterPosition();
                if (listenet != null && position != RecyclerView.NO_POSITION) {
                    listenet.onItemClick(getItem(position));
                }
            });

        }
    }

    public interface OnItemClickEvent {
        public void onItemClick(Note note);
    }

    public void setOnItemClickEvent(OnItemClickEvent listener) {
        this.listenet = listener;
    }
}


