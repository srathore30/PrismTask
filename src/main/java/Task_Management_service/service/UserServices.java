package Task_Management_service.service;

import Task_Management_service.dto.request.JwtRequest;
import Task_Management_service.dto.request.UserReqDto;
import Task_Management_service.dto.response.JwtResponse;
import Task_Management_service.dto.response.UserResDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserServices extends UserDetailsService {
    UserResDto createUser(UserReqDto userReqDto);
    JwtResponse loginUser(JwtRequest jwtRequest);
    UserResDto getUserById(Long id);
}
