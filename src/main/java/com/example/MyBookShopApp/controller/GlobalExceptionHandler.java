package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.exception.BadRequestException;
import com.example.MyBookShopApp.exception.CustomErrorResponse;
import com.example.MyBookShopApp.exception.EmptySearchException;
import com.example.MyBookShopApp.exception.UserNotFoundException;
import com.example.MyBookShopApp.security.ContactConfirmationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        Logger.getLogger(this.getClass().getSimpleName()).warning("!__ UsernameNotFoundException: " + e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("usernameNotFoundError", e);
        return "redirect:/";
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleBadRequestException(EmptySearchException e, HttpServletResponse response) {

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(e.getMessage());
        errors.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        errors.setResult("false");

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse();
        customErrorResponse.setResult("false");
        customErrorResponse.setError(e.getMessage());
        return new ResponseEntity(customErrorResponse, HttpStatus.OK);
    }

}
