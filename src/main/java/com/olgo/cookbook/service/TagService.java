package com.olgo.cookbook.service;

import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.Tag;
import com.olgo.cookbook.repository.RecipeBookmarkRepository;
import com.olgo.cookbook.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepo;
    private final RecipeBookmarkRepository bookmarkRepo;


    public TagService(RecipeBookmarkRepository bookmarkRepo, TagRepository tagRepo) {
        this.bookmarkRepo = bookmarkRepo;
        this.tagRepo = tagRepo;
    }

    /**
     * Replace the tags for the given bookmark so that ONLY tagNames remain associated.
     * - Creates missing Tag entities by name
     * - Removes associations that aren't in tagNames
     * - Does NOT delete Tag rows (shared across bookmarks)
     */
    @Transactional
    public void replaceTagsForBookmark(UUID bookmarkId, List<String> tagNames) {
        // normalize: trim, drop empties, dedupe
        Set<String> desiredNames = (tagNames == null ? Set.<String>of() :
                tagNames.stream()
                        .map(s -> s == null ? "" : s.trim())
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toCollection(LinkedHashSet::new)));

        RecipeBookmark bookmark = bookmarkRepo.findByIdWithTags(bookmarkId)
                .orElseThrow(() -> new NoSuchElementException("Bookmark not found: " + bookmarkId));

        Set<Tag> current = bookmark.getTags();
        if (current == null) {
            current = new LinkedHashSet<>();
            bookmark.setTags(current);
        }

        // load existing tags by desired names
        Map<String, Tag> byName = desiredNames.isEmpty() ? new HashMap<>()
                : tagRepo.findAllByNameIn(desiredNames).stream()
                .collect(Collectors.toMap(Tag::getName, t -> t, (a, b) -> a, LinkedHashMap::new));

        // create any that don't exist yet
        List<Tag> toCreate = desiredNames.stream()
                .filter(n -> !byName.containsKey(n))
                .map(n -> {
                    Tag t = new Tag();
                    t.setName(n);
                    return t;
                })
                .toList();
        if (!toCreate.isEmpty()) {
            tagRepo.saveAll(toCreate);
            toCreate.forEach(t -> byName.put(t.getName(), t));
        }

        // new desired set
        Set<Tag> desired = desiredNames.stream()
                .map(byName::get)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // no-op if identical
        if (current.size() == desired.size() && current.containsAll(desired)) return;

        // remove extras, add missing
        current.removeIf(tag -> !desired.contains(tag));
        for (Tag tag : desired) if (!current.contains(tag)) current.add(tag);

        // managed entity -> join table updates on commit
    }
}

