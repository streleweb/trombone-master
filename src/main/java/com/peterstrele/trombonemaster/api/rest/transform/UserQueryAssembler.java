package com.peterstrele.trombonemaster.api.rest.transform;

import com.peterstrele.trombonemaster.application.results.RetrievedUser;
import com.peterstrele.trombonemaster.application.results.UserPageResult;
import com.peterstrele.trombonemaster.generated.model.UserPage;
import com.peterstrele.trombonemaster.generated.model.UserResponse;

public final class UserQueryAssembler {

    private UserQueryAssembler() {
    }

    public static UserResponse toUserResponse(RetrievedUser retrievedUser) {
        return new UserResponse(
                retrievedUser.id(),
                retrievedUser.username(),
                retrievedUser.email(),
                retrievedUser.displayName(),
                retrievedUser.country()
        );
    }

    public static UserPage toUserPage(UserPageResult result) {
        UserPage response = new UserPage();

        response.setContent(
                result.content()
                        .stream()
                        .map(UserQueryAssembler::toUserResponse)
                        .toList()
        );

        response.setPage(result.page());
        response.setSize(result.size());
        response.setTotalElements(result.totalElements());
        response.setTotalPages(result.totalPages());

        return response;
    }
}
