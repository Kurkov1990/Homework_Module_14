package com.homework.app.controller;

import com.homework.app.entity.Note;
import com.homework.app.service.NoteService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@Controller
public class NoteController {

    private final NoteService noteService;

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
    public String createSubmit(
            @RequestParam("title") @NotBlank @Size(min = 1, max = 250) String title,
            @RequestParam("content") @NotBlank String content) {
        noteService.addNote(new Note(null, title, content));
        return "redirect:/note/list";
    }

    @PostMapping("/note/delete")
    public String delete(@RequestParam("id") @Positive long id) {
        noteService.deleteById(id);
        return "redirect:/note/list";
    }

    @GetMapping("/note/edit")
    public String editForm(@RequestParam("id") @Positive long id, Model model) {
        model.addAttribute("note", noteService.getById(id));
        return "note-edit";
    }

    @PostMapping("/note/edit")
    public String editSubmit(
            @RequestParam("id") @Positive long id,
            @RequestParam("title") @NotBlank @Size(min = 1, max = 250) String title,
            @RequestParam("content") @NotBlank String content) {
        noteService.updateNote(new Note(id, title, content));
        return "redirect:/note/list";
    }
}
