package com.example.MyBookShopApp.aop;

import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class AuthorServiceAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut(value = "execution(* *..*(String)) && @annotation(com.example.MyBookShopApp.aop.annotations.NullArgsCatchable)")
    public void NullArgPointerCatcherPointcut() {}

    @Before(value = "args(arg0) && NullArgPointerCatcherPointcut()", argNames = "arg0")
    public void NullArgPointerCatcherAdvice(String arg0) throws BookstoreAPiWrongParameterException {
        if (arg0 == null) {
            logger.warning("ATTENTION, BookstoreAPiWrongParameterException: input parameter are null");
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }


}
