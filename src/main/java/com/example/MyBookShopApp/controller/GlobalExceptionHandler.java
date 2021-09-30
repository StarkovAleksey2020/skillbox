package com.example.MyBookShopApp.controller;

import com.example.MyBookShopApp.exception.*;
import com.example.MyBookShopApp.security.ContactConfirmationResponse;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
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

    @ExceptionHandler(InsufficientRightsToChangeCoverException.class)
    public String handleInsufficientRightsToChangeCoverException(InsufficientRightsToChangeCoverException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("isBadCredentialsForChangeCover", "Insufficient rights to change cover");
        return ("redirect:/books/" + e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        Logger.getLogger(this.getClass().getSimpleName()).warning("!__ UsernameNotFoundException: " + e.getLocalizedMessage());
        redirectAttributes.addFlashAttribute("usernameNotFoundError", e);
        return "redirect:/";
    }

    // 400
//    @ExceptionHandler(Throwable.class)
//    public @ResponseBody ResponseEntity<CustomErrorResponse> handleDefaultException(Throwable ex) {
//        CustomErrorResponse errors = new CustomErrorResponse();
//        errors.setError("request has empty body or exception occured!");
//        errors.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        errors.setTimestamp(LocalDateTime.now());
//        errors.setResult("false");
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

    // 401 (but 200)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse();
        customErrorResponse.setResult("false");
        customErrorResponse.setError(e.getMessage());
        return new ResponseEntity(customErrorResponse, HttpStatus.OK);
    }

    // 403 (but 200)
    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    public ResponseEntity<CustomErrorResponse> handleForbiddenException(ForbiddenException e) {
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setError(e.getMessage());
        errors.setStatus(HttpServletResponse.SC_FORBIDDEN);
        errors.setTimestamp(LocalDateTime.now());
        errors.setResult("false");
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }
}
