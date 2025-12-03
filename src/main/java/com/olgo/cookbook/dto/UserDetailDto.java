package com.olgo.cookbook.dto;

import com.olgo.cookbook.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailDto extends UserDto {
    private String email;

    public UserDetailDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.id = user.getId();
    }
}
