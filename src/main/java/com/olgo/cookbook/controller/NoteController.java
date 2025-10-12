package com.olgo.cookbook.controller;

import com.olgo.cookbook.model.User;
import com.olgo.cookbook.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/note")
public class NoteController {

    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PutMapping(path = "/{noteId}", consumes = "text/plain")
    public ResponseEntity<?> updateNote(
            @PathVariable UUID noteId,
            @AuthenticationPrincipal User user,
            @RequestBody String noteAddInfo
    ) {
        noteService.upsertForBookmark(noteId, noteAddInfo);
        return ResponseEntity.noContent().build();
    }
}
