package com.homework.app.service;

import com.homework.app.entity.Note;
import com.homework.app.exception.NoteNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.homework.app.utils.Validator.requireNonBlank;
import static com.homework.app.utils.Validator.requireNonNull;

@Service
public class NoteService {

    private final ConcurrentMap<Long, Note> store = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(0);

    public List<Note> listAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(Note::getId))
                .map(NoteService::copy)
                .toList();
    }

    public Note addNote(Note note) {
        requireValidForCreate(note);
        long id = nextId();
        Note toSave = new Note(id, note.getTitle(), note.getContent());
        store.put(id, toSave);
        return copy(toSave);
    }

    public void deleteById(long id) {
        if (store.remove(id) == null) {
            throw notFound(id);
        }
    }

    public void updateNote(Note note) {
        requireValidForUpdate(note);
        store.compute(note.getId(), (id, existing) -> {
            if (existing == null) throw notFound(id);
            return new Note(id, note.getTitle(), note.getContent());
        });
    }

    public Note getById(long id) {
        return copy(getExisting(id));
    }

    private static Note copy(Note n) {
        return new Note(n.getId(), n.getTitle(), n.getContent());
    }

    private long nextId() {
        return idSeq.incrementAndGet();
    }

    private static NoteNotFoundException notFound(long id) {
        return new NoteNotFoundException("Note not found: id=" + id);
    }

    private Note getExisting(long id) {
        Note n = store.get(id);
        if (n == null) throw notFound(id);
        return n;
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
