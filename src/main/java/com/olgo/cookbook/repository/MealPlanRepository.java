package com.olgo.cookbook.repository;

import com.olgo.cookbook.model.MealPlanEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MealPlanRepository extends JpaRepository<MealPlanEntry, UUID> {
    List<MealPlanEntry> findByUserIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);
}
