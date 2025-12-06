package com.olgo.cookbook.controller;

import com.olgo.cookbook.dto.requests.MealPlanRequestDto;
import com.olgo.cookbook.dto.responses.MealPlanResponseDto;
import com.olgo.cookbook.service.MealPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mealplan")
public class MealPlanController {

    private final MealPlanService mealPlanService;

    @GetMapping
    public ResponseEntity<List<MealPlanResponseDto>> getMealPlan(
            @AuthenticationPrincipal(expression = "id") UUID userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<MealPlanResponseDto> responseBody = mealPlanService.getMealPlanEntry(userId, startDate, endDate);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<MealPlanResponseDto> createMealPlan(
            @AuthenticationPrincipal(expression = "id") UUID userId,
            @RequestBody MealPlanRequestDto request) {
        MealPlanResponseDto dto = mealPlanService.createMealPlan(request, userId);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    void deleteMealPlan(UUID id) {
        mealPlanService.deleteMealPlan(id);
    }
}
