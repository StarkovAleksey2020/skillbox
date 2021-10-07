package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.entity.user.UserEntity;
import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean isUserFound(String email) throws BookstoreAPiWrongParameterException {
        validateEmail(email);
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            return true;
        } else {
            return false;
        }
    }

    private void validateEmail(String email) throws BookstoreAPiWrongParameterException {
        if (email == null || email.equals("")) {
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }

    }
}
