package com.peterstrele.trombonemaster.api.rest;

import com.peterstrele.trombonemaster.application.commandservices.UserCommandService;
import com.peterstrele.trombonemaster.application.queryservices.UserQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") //http://localhost:8080/api/users
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }



    @GetMapping
    public String getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "50" ) int limit) {
        return "getusers was called with parameters " + page + " and " + limit;
    }

    @GetMapping(path = "/{userId}")
    public String getUser(@PathVariable String userId) {
        return "getuser was called with id of " + userId;
    }

    @PostMapping
    public String createUser(){
        return "createuser was called";
    }

    @PutMapping
    public String updateUser(){
        return "updateuser was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "deleteuser was called";
    }


}
