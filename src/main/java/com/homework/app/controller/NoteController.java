package com.homework.app.controller;

import com.homework.app.entity.Note;
import com.homework.app.service.NoteService; // інтерфейс
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NoteController {

    private final NoteService noteService; // інтерфейс

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/note/list")
    public String list(Model model) {
        model.addAttribute("notes", noteService.listAll());
        return "note-list";
    }

    @GetMapping("/note/create")
    public String createForm() {
        return "note-create";
    }

    @PostMapping("/note/create")
    public String createSubmit(@RequestParam("title") String title,
                               @RequestParam("content") String content) {
        noteService.addNote(new Note(null, title, content));
        return "redirect:/note/list";
    }

    @PostMapping("/note/delete")
    public String delete(@RequestParam("id") long id) {
        noteService.deleteById(id);
        return "redirect:/note/list";
    }

    @GetMapping("/note/edit")
    public String editForm(@RequestParam("id") long id, Model model) {
        model.addAttribute("note", noteService.getById(id));
        return "note-edit";
    }

    @PostMapping("/note/edit")
    public String editSubmit(@RequestParam("id") long id,
                             @RequestParam("title") String title,
                             @RequestParam("content") String content) {
        noteService.updateNote(new Note(id, title, content));
        return "redirect:/note/list";
    }
}
