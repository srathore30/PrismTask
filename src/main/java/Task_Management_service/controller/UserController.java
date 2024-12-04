package Task_Management_service.controller;

import Task_Management_service.dto.request.JwtRequest;
import Task_Management_service.dto.request.UserReqDto;
import Task_Management_service.dto.response.JwtResponse;
import Task_Management_service.dto.response.UserResDto;
import Task_Management_service.service.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<UserResDto> registerUser(@RequestBody UserReqDto userReqDto) {
        UserResDto userResDto = userServices.createUser(userReqDto);
        return new ResponseEntity<>(userResDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody JwtRequest jwtRequest) {
        JwtResponse jwtResponse = userServices.loginUser(jwtRequest);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResDto> getUserById(@PathVariable Long id) {
        UserResDto userResDto = userServices.getUserById(id);
        return new ResponseEntity<>(userResDto, HttpStatus.OK);
    }
}
