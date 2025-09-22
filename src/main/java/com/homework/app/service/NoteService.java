package com.homework.app.service;

import com.homework.app.entity.Note;

import java.util.List;

public interface NoteService {

    List<Note> listAll();

    Note addNote(Note note);

    void deleteById(long id);

    void updateNote(Note note);

    Note getById(long id);
}
