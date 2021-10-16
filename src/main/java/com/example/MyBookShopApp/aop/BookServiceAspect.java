package com.example.MyBookShopApp.aop;

import com.example.MyBookShopApp.exception.BookstoreAPiWrongParameterException;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class BookServiceAspect {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut(value = "@annotation(com.example.MyBookShopApp.aop.annotations.EmptyArgsCatchable)")
    public void EmptyArgPointerCatcherPointcut() {
    }

    @AfterThrowing(pointcut = "args(arg) && EmptyArgPointerCatcherPointcut()", throwing = "ex")
    public void EmptyArgPointerCatcherAdvice(String arg, Exception ex) throws BookstoreAPiWrongParameterException {
        if (arg.equals("")) {
            logger.warning("ATTENTION, BookstoreAPiWrongParameterException caught: " + ex.getMessage());
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    @Pointcut(value = "execution(* getAuthorBooksCount(String))")
    public void NullStringArgPointerCatcherPointcut() {
    }

    @Before(value = "args(arg) && NullStringArgPointerCatcherPointcut()")
    public void NullStringArgPointerCatcherAdvice(String arg) throws BookstoreAPiWrongParameterException {
        if (arg == null) {
            logger.warning("ATTENTION, BookstoreAPiWrongParameterException: wrong value of input parameter");
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    @Pointcut(value = "@annotation(com.example.MyBookShopApp.aop.annotations.EmptyOrLengthOneArgsCatchable)")
    public void EmptyOrLengthOneArgsPointerCatcherPointcut() {
    }

    @Before(value = "args(arg) && EmptyOrLengthOneArgsPointerCatcherPointcut()")
    public void handleEmptyOrLengthOneArgsCatchable(String arg) throws BookstoreAPiWrongParameterException {
        if (arg.equals("") || arg.length() <= 1) {
            logger.warning("ATTENTION, BookstoreAPiWrongParameterException: wrong values passed to one or more parameters");
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    @Pointcut(value = "execution(* *..*(Integer, Integer))")
    public void getTwoArgsCatcherPointcut() {
    }

    @Before(value = "args(arg0, arg1) && getTwoArgsCatcherPointcut()", argNames = "arg0,arg1")
    public void handleGetBooksWithPriceBetweenCatchable(Integer arg0, Integer arg1) throws BookstoreAPiWrongParameterException {
        if (arg0 == null || arg1 == null) {
            logger.warning("ATTENTION, BookstoreAPiWrongParameterException: one or both of the input parameters are null");
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

    @Pointcut(value = "execution(* *..*(String, Integer, Integer)) && !execution(* getPageOfBooksByAuthorName(..)) && !execution(* getPageOfBooksByGenreName(..)))")
    public void getFreeArgsCatcherPointcut() {
    }

    @Before(value = "args(arg0, arg1, arg2) && getFreeArgsCatcherPointcut()", argNames = "arg0,arg1,arg2")
    public void handleGetBooksWithPriceBetweenCatchable(String arg0, Integer arg1, Integer arg2) throws BookstoreAPiWrongParameterException {
        if (arg0 == null || arg1 == null || arg2 == null) {
            logger.warning("ATTENTION, BookstoreAPiWrongParameterException: one of the three input parameters are null");
            throw new BookstoreAPiWrongParameterException("Wrong values passed to one or more parameters");
        }
    }

}
