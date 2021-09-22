package com.example.rooom;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteReprosetry reprosetry;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        reprosetry = new NoteReprosetry(application);
        allNotes = reprosetry.gelAllNotes();
    }

    public void insert(Note note) {
        reprosetry.insert(note);
    }

    public void delete(Note note) {
        reprosetry.delete(note);
    }

    public void update(Note note) {
        reprosetry.update(note);
    }

    public void deleteAll() {
        reprosetry.deleteAll();
    }

    public LiveData<List<Note>> getAllNote() {
        return allNotes;
    }
}
