package com.fatec.smart_parking.user;


public record UserUpdateDTO(
        String name,
        String email,
        String password,
        String oldPassword

        ) {
}
