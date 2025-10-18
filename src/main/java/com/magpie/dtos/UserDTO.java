package com.magpie.dtos;

import com.magpie.entities.Group;

import java.util.List;
import java.util.UUID;

public record UserDTO(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String profilePicture,
            String bio,
            List<Group> groups
){}

