package com.peterstrele.trombonemaster.api.rest;

import com.peterstrele.trombonemaster.api.rest.transform.UserCommandAssembler;
import com.peterstrele.trombonemaster.api.rest.transform.UserQueryAssembler;
import com.peterstrele.trombonemaster.application.commandservices.UserCommandService;
import com.peterstrele.trombonemaster.application.queryservices.UserQueryService;
import com.peterstrele.trombonemaster.application.results.CreatedUser;
import com.peterstrele.trombonemaster.application.results.RetrievedUser;
import com.peterstrele.trombonemaster.application.results.UserPageResult;
import com.peterstrele.trombonemaster.generated.api.UsersApi;
import com.peterstrele.trombonemaster.generated.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
                UserCommandAssembler.toCreateUserCommand(createUserRequest)
        );

        UserResponse response = UserCommandAssembler.toUserResponse(createdUser);

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
        RetrievedUser retrievedUser = userQueryService.retrieveUser(userId);

        UserResponse response = UserQueryAssembler.toUserResponse(retrievedUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<UserPage> getUsers(
            Integer page,
            Integer size,
            String username,
            String email,
            String displayName,
            List<String> country
    ) {

        UserPageResult result = userQueryService.retrieveUsers(
                page,
                size,
                username,
                email,
                displayName,
                country
        );

        UserPage response = UserQueryAssembler.toUserPage(result);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok().build();
    }
}
