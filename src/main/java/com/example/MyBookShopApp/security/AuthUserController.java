package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import com.example.MyBookShopApp.exception.UserNotFoundException;
import com.example.MyBookShopApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthUserController {

    private final UserEntityRegister userEntityRegister;
    private final UserService userService;

    @Autowired
    public AuthUserController(UserEntityRegister userEntityRegister, UserService userService) {
        this.userEntityRegister = userEntityRegister;
        this.userService = userService;
    }

    @GetMapping("/signin")
    public String handleSignIn() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) throws UserNotFoundException, BookstoreAPiWrongParameterException {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        if (userService.isUserFound(payload.getContact())) {
            response.setResult("true");
        } else {
            throw new UserNotFoundException("The specified user was not found");
        }
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userEntityRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletRequest httpServletRequest,
                                                   HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = userEntityRegister.jwtLogin(httpServletRequest, payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @PostMapping("/principal")
    @ResponseBody
    public Object handlePrincipal(@RequestBody ContactConfirmationPayload payload) {
        return userEntityRegister.getPrincipal(payload);
    }

    @GetMapping("/my")
    public String handleMy() {
        return "/my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUsr", userEntityRegister.getCurrentUser());
        return "profile";
    }
}
