package com.olgo.cookbook.service;

import com.olgo.cookbook.model.Note;
import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.repository.NoteRepository;
import com.olgo.cookbook.repository.RecipeBookmarkRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final RecipeBookmarkRepository bookmarkRepository;

    public NoteService(NoteRepository noteRepository, RecipeBookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.noteRepository = noteRepository;
    }

    @Transactional
    public boolean upsertForBookmark(UUID bookmarkId, String additionalInfo) {
        // Ensure the bookmark exists (and optionally check ownership here)
        RecipeBookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Bookmark " + bookmarkId + " not found"));

        // Try to find an existing Note by the same id (mapsId)
        Note note = noteRepository.findById(bookmarkId).orElse(null);
        if (note == null) {
            note = new Note();
            note.setAdditionalInfo(additionalInfo == null ? "" : additionalInfo.trim());
            note.setRecipeBookmark(bookmark);
            noteRepository.save(note);
            return true;
        } else {
            note.setAdditionalInfo(additionalInfo == null ? "" : additionalInfo.trim());
            return false;
        }
    }
}
