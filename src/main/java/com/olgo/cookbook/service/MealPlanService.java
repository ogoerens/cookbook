package com.olgo.cookbook.service;

import com.olgo.cookbook.dto.requests.MealPlanRequestDto;
import com.olgo.cookbook.dto.responses.MealPlanResponseDto;
import com.olgo.cookbook.model.MealPlanEntry;
import com.olgo.cookbook.model.RecipeBookmark;
import com.olgo.cookbook.model.User;
import com.olgo.cookbook.repository.MealPlanRepository;
import com.olgo.cookbook.repository.RecipeBookmarkRepository;
import com.olgo.cookbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MealPlanService {
    private final MealPlanRepository mealPlanRepository;
    private final RecipeBookmarkRepository recipeBookmarkRepository;
    private final UserRepository userRepository;

    public void deleteMealPlan(UUID id) {
        mealPlanRepository.deleteById(id);
    }

    public List<MealPlanResponseDto> getMealPlanEntry(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<MealPlanEntry> entries = mealPlanRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        return entries.stream()
                .map(entry ->
                        new MealPlanResponseDto(
                                entry.getId(),
                                entry.getBookmark().getId(),
                                entry.getBookmark().getName(),
                                entry.getDate(),
                                entry.getMealType(),
                                entry.isNeedsCooking()
                        )
                )
                .sorted(Comparator.comparing(MealPlanResponseDto::date))
                .toList();
    }

    public MealPlanResponseDto createMealPlan(MealPlanRequestDto request, UUID userId) {
        RecipeBookmark bookmark = recipeBookmarkRepository.findById(request.recipeBookmarkId()).orElseThrow();

        User user = userRepository.findById(userId).orElseThrow();

        MealPlanEntry mealPlanEntry = new MealPlanEntry(
                request.date(),
                request.mealType(),
                request.needsCooking(),
                bookmark,
                user
        );

        MealPlanEntry entity = mealPlanRepository.save(mealPlanEntry);
        return new MealPlanResponseDto(
                entity.getId(),
                entity.getBookmark().getId(),
                entity.getBookmark().getName(),
                entity.getDate(),
                entity.getMealType(),
                entity.isNeedsCooking()
        );
    }
}
