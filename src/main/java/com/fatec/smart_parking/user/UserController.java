package com.fatec.smart_parking.user;

import com.fatec.smart_parking.core.authentication.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(){
        List<UserDTO> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        UserDTO user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }


    @PutMapping
    public ResponseEntity<LoginResponseDTO> update(@RequestBody UserUpdateDTO updateUserDTO) {
        LoginResponseDTO token = userService.update(updateUserDTO);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(){
       UserDTO userDTO = userService.getCurrentUser();
        return ResponseEntity.ok().body(userDTO);
    }




}
