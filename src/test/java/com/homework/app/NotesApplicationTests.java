package com.homework.app;

import com.homework.app.entity.Note;
import com.homework.app.exception.NoteNotFoundException;
import com.homework.app.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoteServiceTest {

    private NoteService service;

    @BeforeEach
    void setUp() {
        service = new NoteService();
    }

    @Test
    @DisplayName("addNote: creates a note with incremental id and returns a copy")
    void addNote_createsNoteWithId() {
        Note created = service.addNote(new Note(null, "T1", "C1"));
        assertNotNull(created.getId());
        assertEquals("T1", created.getTitle());
        assertEquals("C1", created.getContent());

        Note fromStore = service.getById(created.getId());
        assertEquals(created.getId(), fromStore.getId());
    }

    @Test
    @DisplayName("listAll: returns notes sorted by id")
    void listAll_sortedById() {
        service.addNote(new Note(null, "A", "a"));
        service.addNote(new Note(null, "B", "b"));
        List<Note> all = service.listAll();
        assertEquals(2, all.size());
        assertTrue(all.get(0).getId() < all.get(1).getId());
    }

    @Test
    @DisplayName("getById: throws NoteNotFoundException if not found")
    void getById_notFound() {
        assertThrows(NoteNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    @DisplayName("updateNote: updates title and content of an existing note")
    void updateNote_updatesFields() {
        Note created = service.addNote(new Note(null, "Old", "OldC"));
        Long id = created.getId();

        service.updateNote(new Note(id, "New", "NewC"));
        Note updated = service.getById(id);
        assertEquals("New", updated.getTitle());
        assertEquals("NewC", updated.getContent());
    }

    @Test
    @DisplayName("updateNote: throws NoteNotFoundException if note does not exist")
    void updateNote_notFound() {
        assertThrows(NoteNotFoundException.class, () ->
                service.updateNote(new Note(123L, "T", "C"))
        );
    }

    @Test
    @DisplayName("deleteById: deletes an existing note")
    void deleteById_ok() {
        Note created = service.addNote(new Note(null, "T", "C"));
        service.deleteById(created.getId());
        assertThrows(NoteNotFoundException.class, () -> service.getById(created.getId()));
    }

    @Test
    @DisplayName("deleteById: throws NoteNotFoundException if note does not exist")
    void deleteById_notFound() {
        assertThrows(NoteNotFoundException.class, () -> service.deleteById(42L));
    }

    @Test
    @DisplayName("validation: title/content cannot be blank")
    void validation_blankTitleOrContent() {
        assertThrows(IllegalArgumentException.class, () -> service.addNote(new Note(null, " ", "C")));
        assertThrows(IllegalArgumentException.class, () -> service.addNote(new Note(null, "T", " \t")));

        Note created = service.addNote(new Note(null, "T", "C"));
        assertThrows(IllegalArgumentException.class, () -> service.updateNote(new Note(created.getId(), " ", "C")));
        assertThrows(IllegalArgumentException.class, () -> service.updateNote(new Note(created.getId(), "T", null)));
    }
}
