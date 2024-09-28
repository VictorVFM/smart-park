package com.fatec.smart_parking.user;

import com.fatec.smart_parking.core.Role;

import com.fatec.smart_parking.core.authentication.AuthenticationService;
import com.fatec.smart_parking.core.authentication.LoginResponseDTO;
import com.fatec.smart_parking.core.config.TokenService;
import com.fatec.smart_parking.core.exception.IncorrectPasswordException;
import com.fatec.smart_parking.core.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    public List<UserDTO> findAll(){
        List<User> clientsList = userRepository.findAll();
        List<UserDTO> clientsDTOList = new  ArrayList();
        for (User user : clientsList) {
            clientsDTOList.add(convertToDTO(user));
        }
        return clientsDTOList;
    }

    public UserDTO findById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return convertToDTO(user);
    }

    public UserDTO findByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return convertToDTO(user);
    }

    public UserDTO create(User user) {
        userValidator.checkEmailExists(user.getEmail());
        userValidator.checkEmailValidity(user.getEmail());
        user.setRole(Role.CLIENT);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return convertToDTO(userRepository.save(user));
    }

    public LoginResponseDTO update(UserUpdateDTO userUpdateDTO){
        User user = this.authenticationService.getCurrentUser();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(passwordEncoder.matches(userUpdateDTO.oldPassword(),user.getPassword())){
            if(!userUpdateDTO.email().equals(user.getEmail())){
                this.userValidator.checkEmailExists(userUpdateDTO.email());
                this.userValidator.checkEmailValidity(userUpdateDTO.email());
            }

            user.setEmail(userUpdateDTO.email());
            user.setName(userUpdateDTO.name());
            if (userUpdateDTO.password() != null && !userUpdateDTO.password().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));
            }

            User savedUser = this.userRepository.save(user);
            var token = tokenService.generateToken(savedUser);
            return new LoginResponseDTO(token);
        }

        throw new IncorrectPasswordException();
    }

    public UserDTO getCurrentUser(){
       User user =  this.authenticationService.getCurrentUser();
       return convertToDTO(user);
    }

    public void delete(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException();
        }
    }

    public UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getEnabled());
    }



}
