package com.em248.controller;

import com.em248.controller.vm.RegisterUserVM;
import com.em248.repository.UserRepository;
import com.em248.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserRestController {


    private final UserService userService;


    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserVM registerUserVM) {

        userService.register(registerUserVM);

        return ResponseEntity.ok("success");
    }

}
