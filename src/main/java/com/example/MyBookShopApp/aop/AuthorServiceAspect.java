package com.example.MyBookShopApp.aop;

import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class AuthorServiceAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut(value = "@annotation(com.example.MyBookShopApp.aop.annotations.NullArgsCatchable)")
    public void NullArgPointerCatcherPointcut() {}

    @AfterThrowing(pointcut = "args(description) && NullArgPointerCatcherPointcut()", throwing = "ex")
    public void NullArgPointerCatcherAdvice(String description, Exception ex) throws BookstoreAPiWrongParameterException {
        if (description == null) {
        logger.warning("ATTENTION, exception caught: " + ex.getMessage());
        throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

}
