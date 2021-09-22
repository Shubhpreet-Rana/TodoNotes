package com.example.rooom;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteReprosetry {

    private final NoteDao noteDao ;
    private final LiveData<List<Note>> allNotes ;

    public NoteReprosetry(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note){
        new InsertDatabase(noteDao).execute(note);
    }

    public void update(Note note){
        new UpdateDatabase(noteDao).execute(note);
    }

    public void delete(Note note){
        new DeleteDatabase(noteDao).execute(note);
    }

    public void deleteAll(){
        new DeleteAllDatabase( noteDao).execute();
    }

    public LiveData<List<Note>> gelAllNotes(){
        return allNotes;
    }

    private static class InsertDatabase extends AsyncTask<Note , Void , Void>{

        private final NoteDao noteDao ;

        private InsertDatabase (NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }


    private static class UpdateDatabase extends AsyncTask<Note , Void , Void>{

        private final NoteDao noteDao ;

        private UpdateDatabase (NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteDatabase extends AsyncTask<Note , Void , Void>{

        private final NoteDao noteDao ;

        private DeleteDatabase (NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllDatabase extends AsyncTask<Note , Void , Void>{

        private final NoteDao noteDao ;

        private DeleteAllDatabase (NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

}
