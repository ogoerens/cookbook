package com.olgo.cookbook.dto;

import com.olgo.cookbook.model.User;

import java.util.UUID;


public class UserDto {
    protected UUID id;
    protected String username;

    public UserDto() {
    }

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.username = user.getUsername();
        return userDto;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
