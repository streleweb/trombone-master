package com.peterstrele.trombonemaster.api.rest;

import com.peterstrele.trombonemaster.api.rest.transform.UserCommandAssembler;
import com.peterstrele.trombonemaster.api.rest.transform.UserQueryAssembler;
import com.peterstrele.trombonemaster.application.commandservices.UserCommandService;
import com.peterstrele.trombonemaster.application.queryservices.UserQueryService;
import com.peterstrele.trombonemaster.application.results.RetrievedUser;
import com.peterstrele.trombonemaster.application.results.UserPageResult;
import com.peterstrele.trombonemaster.application.results.UserResult;
import com.peterstrele.trombonemaster.generated.api.UsersApi;
import com.peterstrele.trombonemaster.generated.model.UpdateUserRequest;
import com.peterstrele.trombonemaster.generated.model.UserPage;
import com.peterstrele.trombonemaster.generated.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController implements UsersApi {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(
            UserCommandService userCommandService,
            UserQueryService userQueryService
    ) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    // =========================================================
    // GET /api/users
    // =========================================================

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

    // =========================================================
    // GET /api/users/{userId}
    // =========================================================

    @Override
    public ResponseEntity<UserResponse> getUser(UUID userId) {

        RetrievedUser retrievedUser =
                userQueryService.retrieveUser(userId);

        UserResponse response =
                UserQueryAssembler.toUserResponse(retrievedUser);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // PUT /api/users/{userId}
    // =========================================================

    @Override
    public ResponseEntity<UserResponse> updateUser(
            UUID userId,
            UpdateUserRequest updateUserRequest
    ) {
        UserResult updatedUser = userCommandService.updateUser(
                UserCommandAssembler.toUpdateUserCommand(
                        userId,
                        updateUserRequest
                )
        );

        UserResponse response =
                UserCommandAssembler.toUserResponse(updatedUser);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE /api/users/{userId}
    // =========================================================

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {

        /*
         * TODO:
         *
         * userCommandService.deleteUser(userId);
         *
         * return ResponseEntity.noContent().build();
         */

        throw new UnsupportedOperationException(
                "Delete user has not been implemented yet."
        );
    }

    // =========================================================
    // GET /api/users/me
    // =========================================================

    @Override
    public ResponseEntity<UserResponse> getCurrentUser() {

        /*
         * We will implement this after Spring Security.
         *
         * The authenticated JWT will identify the current user.
         *
         * Eventually this becomes approximately:
         *
         * UUID userId = authenticatedUser.getUserId();
         *
         * RetrievedUser user =
         *     userQueryService.retrieveUser(userId);
         *
         * return ResponseEntity.ok(
         *     UserQueryAssembler.toUserResponse(user)
         * );
         */

        throw new UnsupportedOperationException(
                "Current user requires authentication."
        );
    }

    // =========================================================
    // PUT /api/users/me
    // =========================================================

    @Override
    public ResponseEntity<UserResponse> updateCurrentUser(
            UpdateUserRequest updateUserRequest
    ) {

        /*
         * Once authentication exists:
         *
         * 1. Get user ID from authenticated principal/JWT
         * 2. Create UpdateUserCommand
         * 3. Execute UserCommandService
         * 4. Return updated UserResponse
         */

        throw new UnsupportedOperationException(
                "Current user requires authentication."
        );
    }

    // =========================================================
    // DELETE /api/users/me
    // =========================================================

    @Override
    public ResponseEntity<Void> deleteCurrentUser() {

        /*
         * Once authentication exists:
         *
         * UUID userId = authenticatedUser.getUserId();
         *
         * userCommandService.deleteUser(userId);
         *
         * return ResponseEntity.noContent().build();
         */

        throw new UnsupportedOperationException(
                "Current user requires authentication."
        );
    }
}