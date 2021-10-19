package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.user.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public class PhoneNumberUserEntityDetails extends UserEntityDetails {
    public PhoneNumberUserEntityDetails(UserEntity userEntity) {
        super(userEntity);
    }

    @Override
    public String getUsername() {
        return getUserEntity().getPhone();
    }
}
