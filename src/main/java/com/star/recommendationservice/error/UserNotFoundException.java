package com.star.recommendationservice.error;

// Исключение для случаев, когда пользователь с запрашиваемым ID в базе не найден
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
