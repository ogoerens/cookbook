package com.olgo.cookbook.dto.requests;

import com.olgo.cookbook.validation.FieldsMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@FieldsMatch(
        field = "newPassword",
        fieldMatch = "newPasswordConfirmation",
        message = "New password fields do not match."
)
public record PasswordUpdateDto(
        @NotBlank(message = "New password is required.")
        @Size(min = 8, message = "New password must be at least 8 characters.")
        String newPassword,

        @NotBlank(message = "Confirmation is required.")
        String newPasswordConfirmation,

        @NotBlank(message = "Old password is required.")
        String oldPassword
) {
}