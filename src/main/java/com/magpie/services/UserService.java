package com.magpie.services;

import com.magpie.dtos.UserDTO;
import com.magpie.entities.User;
import com.magpie.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private UserDTO convertToDTO(User user){
        return new UserDTO(
                user.getPublicId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getProfilePicture(),
                user.getBio(),
                user.getGroups());
    }
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create
    public void createUser(User user){
        this.userRepository.save(user);
    }

    // Read
    public UserDTO findById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
        return convertToDTO(user);
    }
    public UserDTO findByPublicId(UUID publicId) {
        User user = userRepository.findByPublicId(publicId).orElseThrow(() -> new IllegalArgumentException("User with id " + publicId + " not found"));
        return convertToDTO(user);
    }
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with name " + username + " not found"));
        return convertToDTO(user);
    }
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));
        return convertToDTO(user);
    }

    // Update
    public void updateUser(User user){
        if (user.getPublicId() == null){
            throw new IllegalArgumentException("User with id " + user.getPublicId() + " not found");
        }
        User existing = userRepository.findByPublicId(user.getPublicId())
                .orElseThrow(() -> new IllegalArgumentException("User with id " + user.getPublicId() + " not found"));

        // Must have at least one non-empty name
        boolean hasFirstName = user.getFirstName() != null && !user.getFirstName().trim().isEmpty();
        boolean hasLastName = user.getLastName() != null && !user.getLastName().trim().isEmpty();

        if (!hasFirstName && !hasLastName) {
            throw new IllegalArgumentException("User must have at least a first name or a last name");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Account must have an email");
        }
        existing.setEmail(user.getEmail());
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setBio(user.getBio());

        userRepository.save(existing);
    }

    // Delete
    public void deleteUser(Integer id){
        if (id == null){
            throw new IllegalArgumentException("An id must be provided to perform this method");
        }
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User id " + id + " not found"));
        userRepository.delete(existing);
    }
    public void deleteUser(UUID publicId){
        if (publicId == null){
            throw new IllegalArgumentException("An id must be provided to perform this method");
        }
        User existing = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("User id " + publicId + " not found"));
        userRepository.delete(existing);
    }
}
