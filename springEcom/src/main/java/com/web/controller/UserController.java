package com.web.controller;

import com.web.entity.User;
import com.web.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody User user,
            BindingResult bindingResult) {

        // Validation errors
        if (bindingResult.hasErrors()) {

            Map<String, String> validationErrors =
                    new HashMap<>();

            for (FieldError error :
                    bindingResult.getFieldErrors()) {

                validationErrors.put(
                        error.getField(),
                        error.getDefaultMessage()
                );
            }

            return ResponseEntity
                    .badRequest()
                    .body(validationErrors);
        }

        try {

            userService.registerUser(user);

            Map<String, String> response =
                    new HashMap<>();

            response.put(
                    "message",
                    "User registered successfully"
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {

            Map<String, String> error =
                    new HashMap<>();

            error.put(
                    "message",
                    e.getMessage()
            );

            return ResponseEntity
                    .badRequest()
                    .body(error);
        }
    }
}