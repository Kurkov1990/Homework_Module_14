package com.homework.app.service.impl;

import com.homework.app.entity.Note;
import com.homework.app.exception.NoteNotFoundException;
import com.homework.app.repository.NoteRepository;
import com.homework.app.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.homework.app.utils.Validator.requireNonBlank;
import static com.homework.app.utils.Validator.requireNonNull;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repo;

    public NoteServiceImpl(NoteRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> listAll() {
        return repo.findAll().stream()
                .sorted(Comparator.comparing(Note::getId))
                .toList();
    }

    @Override
    public Note addNote(Note note) {
        requireValidForCreate(note);
        note.setTitle(note.getTitle());
        note.setContent(note.getContent());
        return repo.save(note);
    }

    @Override
    public void deleteById(long id) {
        if (!repo.existsById(id)) {
            throw notFound(id);
        }
        repo.deleteById(id);
    }

    @Override
    public void updateNote(Note note) {
        requireValidForUpdate(note);
        Note existing = repo.findById(note.getId()).orElseThrow(() -> notFound(note.getId()));
        existing.setTitle(note.getTitle());
        existing.setContent(note.getContent());
        repo.save(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public Note getById(long id) {
        return repo.findById(id).orElseThrow(() -> notFound(id));
    }

    private static NoteNotFoundException notFound(long id) {
        return new NoteNotFoundException("Note not found: id=" + id);
    }

    private static void requireValidForCreate(Note note) {
        requireNonNull(note, "note");
        requireNonBlank(note.getTitle(), "title");
        requireNonBlank(note.getContent(), "content");
    }

    private static void requireValidForUpdate(Note note) {
        requireNonNull(note, "note");
        requireNonNull(note.getId(), "note.id");
        requireNonBlank(note.getTitle(), "title");
        requireNonBlank(note.getContent(), "content");
    }
}
