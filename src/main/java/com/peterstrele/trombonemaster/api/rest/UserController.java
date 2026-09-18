package com.peterstrele.trombonemaster.api.rest;

import com.peterstrele.trombonemaster.api.rest.transform.UserAssembler;
import com.peterstrele.trombonemaster.application.commands.CreateUserCommand;
import com.peterstrele.trombonemaster.application.commandservices.UserCommandService;
import com.peterstrele.trombonemaster.application.queryservices.UserQueryService;
import com.peterstrele.trombonemaster.application.results.CreatedUser;
import com.peterstrele.trombonemaster.generated.api.UsersApi;
import com.peterstrele.trombonemaster.generated.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
/**
 * http://localhost:8080/api/users
 * Requestmapping PATHS already defined in openapi.yaml and generated in UsersApi
 */

public class UserController implements UsersApi {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }


    @Override
    public ResponseEntity<UserResponse> createUser(CreateUserRequest createUserRequest) {

        CreatedUser createdUser = userCommandService.createUser(
                UserAssembler.toCreateUserCommand(createUserRequest)
        );

        UserResponse response = UserAssembler.toUserResponse(createdUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID userId) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserPage> getUsers(Integer page, Integer size, String username, String country, UserStatus status) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok().build();
    }
}
