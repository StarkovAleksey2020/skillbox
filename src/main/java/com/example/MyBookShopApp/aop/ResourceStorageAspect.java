package com.example.MyBookShopApp.aop;

import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class ResourceStorageAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut(value = "within(com.example.MyBookShopApp.service.ResourceStorage) && @annotation(com.example.MyBookShopApp.aop.annotations.EmptyOrNullArgsCatchable) && execution(* *..*(String))")
    public void EmptyOrNullArgPointerCatcherPointcut() {}

    @Before(value = "args(arg0) && EmptyOrNullArgPointerCatcherPointcut()", argNames = "arg0")
    public void EmptyOrNullArgPointerCatcherPointcutAdvice(String arg0) throws BookstoreAPiWrongParameterException {
        if (arg0 == null || arg0.equals("")) {
            logger.warning("ATTENTION, BookstoreAPiWrongParameterException: input String parameter empty or null");
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

}
