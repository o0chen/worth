package com.blackeye.worth.core;

import com.blackeye.worth.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(SessionNotFoundException.class)
//    @ResponseBody
//    public ErrorMessage<String> sessionNotFoundExceptionHandler(HttpServletRequest request, SessionNotFoundException exception) throws Exception {
//        return handleErrorInfo(request, exception.getMessage(), exception);
//    }
//
//    @ExceptionHandler(NullOrEmptyException.class)
//    @ResponseBody
//    public ErrorMessage<String> nullOrEmptyExceptionHandler(HttpServletRequest request, NullOrEmptyException exception) throws Exception {
//        return handleErrorInfo(request, exception.getMessage(), exception);
//    }
//
//    @ExceptionHandler(IllegalPropertiesException.class)
//    @ResponseBody
//    public ErrorMessage<String> illegalPropExceptionHandler(HttpServletRequest request, IllegalPropertiesException exception) throws Exception {
//        return handleErrorInfo(request, exception.getMessage(), exception);
//    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        return handleErrorInfo(request, exception.getMessage()==null?exception.getCause().getMessage():exception.getMessage(), exception);
    }

    private Result handleErrorInfo(HttpServletRequest request, String message, Exception exception) {
        return new Result.Builder().isSuccess(false).code(-1).message(message).build();
    }
}