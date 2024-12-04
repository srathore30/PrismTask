package Task_Management_service.implementation;

import Task_Management_service.AuthUtils.JwtHelper;
import Task_Management_service.config.AuthConfig;
import Task_Management_service.constant.ApiErrorCodes;
import Task_Management_service.dto.request.JwtRequest;
import Task_Management_service.dto.request.UserReqDto;
import Task_Management_service.dto.response.JwtResponse;
import Task_Management_service.dto.response.UserResDto;
import Task_Management_service.entity.UserEntity;
import Task_Management_service.exception.NoSuchElementFoundException;
import Task_Management_service.exception.ValidationException;
import Task_Management_service.repository.UserRepo;
import Task_Management_service.service.UserServices;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServicesImpl implements UserServices {

    private final UserRepo userRepo;
    private final AuthConfig authConfig;
    private final JwtHelper jwtHelper;

    public UserServicesImpl(UserRepo userRepo, AuthConfig authConfig, JwtHelper jwtHelper) {
        this.userRepo = userRepo;
        this.authConfig = authConfig;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public UserResDto createUser(UserReqDto userReqDto) {
        Optional<UserEntity> optionalUserEntity = userRepo.findByEmail(userReqDto.getEmail());
        if (optionalUserEntity.isPresent()) {
            throw new ValidationException(
                    ApiErrorCodes.USER_ALREADY_EXIST.getErrorCode(),ApiErrorCodes.USER_ALREADY_EXIST.getErrorMessage()
            );
        }
        UserEntity user = mapToEntity(userReqDto);
        UserEntity savedUser = userRepo.save(user);
        return mapToDto(savedUser);
    }

    @Override
    public JwtResponse loginUser(JwtRequest jwtRequest) {
        UserDetails userDetails = loadUserByUsername(jwtRequest.getUsername());
        if (AuthConfig.matches(jwtRequest.getPassword(), userDetails.getPassword())) {
            String token = jwtHelper.generateToken(userDetails);
            return new JwtResponse(token);
        }

        throw new ValidationException(
                ApiErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorCode(),  ApiErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorMessage()
        );
    }

    @Override
    public UserResDto getUserById(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepo.findById(id);
        if (optionalUserEntity.isEmpty()) {
            throw new NoSuchElementFoundException(
                    ApiErrorCodes.USER_NOT_FOUND.getErrorCode(), ApiErrorCodes.USER_NOT_FOUND.getErrorMessage()
            );
        }

        return mapToDto(optionalUserEntity.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepo.findByUsername(username);
        if (optionalUserEntity.isPresent()) {
            UserEntity user = optionalUserEntity.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")) // Default authority
            );
        }

        throw new ValidationException(
                ApiErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorCode(),
                ApiErrorCodes.INVALID_USERNAME_OR_PASSWORD.getErrorMessage()
        );
    }

    private UserResDto mapToDto(UserEntity userEntity) {
        UserResDto userResDto = new UserResDto();
        userResDto.setId(userEntity.getId());
        userResDto.setEmail(userEntity.getEmail());
        userResDto.setUsername(userEntity.getUsername());
        userResDto.setMobileNo(userEntity.getMobileNo());
        return userResDto;
    }

    private UserEntity mapToEntity(UserReqDto userReqDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userReqDto.getEmail());
        userEntity.setUsername(userReqDto.getUsername());
        userEntity.setPassword(authConfig.passwordEncoder().encode(userReqDto.getPassword()));
        userEntity.setMobileNo(userReqDto.getMobileNo());
        return userEntity;
    }
}