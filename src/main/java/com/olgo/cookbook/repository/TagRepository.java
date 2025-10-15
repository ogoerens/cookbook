package com.olgo.cookbook.repository;

import com.olgo.cookbook.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    Optional<Tag> findByNameIgnoreCase(String name);

    Optional<Tag> findByName(String name);

    List<Tag> findAllByNameIn(Collection<String> names);
}
