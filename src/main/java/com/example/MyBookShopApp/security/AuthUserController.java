package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.entity.SmsCode;
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
    private final SmsService smsService;

    @Autowired
    public AuthUserController(UserEntityRegister userEntityRegister, UserService userService, SmsService smsService) {
        this.userEntityRegister = userEntityRegister;
        this.userService = userService;
        this.smsService = smsService;
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
        response.setResult("true");

        if (payload.getContact().contains("@")) {
            if (userService.isUserFound(payload.getContact())) {
                return response;
            } else {
                throw new UserNotFoundException("The specified user was not found");
            }
        } else {
            String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
            smsService.saveNewCode(new SmsCode(smsCodeString, 60));
            return response;
        }

    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();

        if (smsService.verifyCode(payload.getCode())) {
            response.setResult("true");
            return response;
        } else {
            if (payload.getContact().contains("@")) {
                response.setResult("true");
                return response;
            } else {
                return new ContactConfirmationResponse();
            }
        }
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

    @PostMapping("/login-by-phone-number")
    @ResponseBody
    public ContactConfirmationResponse handleLoginByPhoneNumber(@RequestBody ContactConfirmationPayload payload,
                                                                HttpServletRequest httpServletRequest,
                                                                HttpServletResponse httpServletResponse) {
        if (smsService.verifyCode(payload.getCode())) {
            ContactConfirmationResponse loginResponse = userEntityRegister.jwtLoginByPhoneNumber(httpServletRequest, payload);
            Cookie cookie = new Cookie("token", loginResponse.getResult());
            httpServletResponse.addCookie(cookie);
            return loginResponse;
        } else {
            return null;
        }
    }

    @PostMapping("/principal")
    @ResponseBody
    public Object handlePrincipal(ContactConfirmationPayload payload) {
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
