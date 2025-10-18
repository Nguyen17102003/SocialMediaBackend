package com.magpie.dtos;

public record UserCreateTemplate(
                                String username,
                                String password,
                                String firstName,
                                String lastName,
                                String email,
                                String profilePicture) {
}
