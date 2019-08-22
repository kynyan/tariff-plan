package yota.homework.tariff.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yota.homework.tariff.dto.UserDto;
import yota.homework.tariff.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(path = "/api/user")
    @ApiOperation(httpMethod = "POST", value = "creates new user")
    public ResponseEntity createUser(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }
}
