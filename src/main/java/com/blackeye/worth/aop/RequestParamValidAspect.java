package com.blackeye.worth.aop;

import com.blackeye.worth.model.BaseDojo;
import com.blackeye.worth.utils.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Aspect
public class RequestParamValidAspect {
    Logger log = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(* com.blackeye.worth.controller.*.*(..))")
    public void controllerBefore() {
    }

    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Before("controllerBefore()")
    public void before(JoinPoint point) throws NoSuchMethodException, SecurityException, ParamValidException {
        //  获得切入目标对象
        Object target = point.getThis();
        // 获得切入方法参数
        Object[] args = point.getArgs();
        // 获得切入的方法
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        // 执行校验，获得校验结果
        Set<ConstraintViolation<Object>> validResult = validMethodParams(target, method, args);
        if (!validResult.isEmpty()) {
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method); // 获得方法的参数名称
            List<FieldError> errors = validResult.stream().map(constraintViolation -> {
                PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();  // 获得校验的参数路径信息
                int paramIndex = pathImpl.getLeafNode().getParameterIndex(); // 获得校验的参数位置
                String paramName = parameterNames[paramIndex];  // 获得校验的参数名称
                FieldError error = new FieldError();  // 将需要的信息包装成简单的对象，方便后面处理
                error.setName(paramName);  // 参数名称（校验错误的参数名称）
                error.setMessage(constraintViolation.getMessage()); // 校验的错误信息
                log.error("{}信息错误，提示：{}", error.getName(), error.getMessage());
                return error;
            }).collect(Collectors.toList());
            throw new ParamValidException(errors);  // 抛出异常，交给上层处理
        }

        // 执行校验，获得校验结果
        Set<ConstraintViolation> argValidResult = validMethodBeanParams(args);
        if (!argValidResult.isEmpty()) {
            List<FieldError> errors = argValidResult.stream().map(constraintViolation -> {
                FieldError error = new FieldError();  // 将需要的信息包装成简单的对象，方便后面处理
                error.setName(constraintViolation.getPropertyPath().toString());  // 参数名称（校验错误的参数名称）
                error.setMessage(constraintViolation.getMessage()); // 校验的错误信息
                log.error("{}信息错误，提示：{}", error.getName(), error.getMessage());
                return error;
            }).collect(Collectors.toList());

            throw new ParamValidException(errors);  // 抛出异常，交给上层处理
        }
    }

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private final ExecutableValidator executableValidator = validator.forExecutables();

    /**
     * 验证以下请求方式
     *
     * @RequestMapping(value = "token")
     * public Result token(@NotBlank String username, @NotBlank String password){
     * String token = userService.login(username, PwdUtils.pwd(password));
     * Result result = new Result(token);
     * return result;
     * }
     */
    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
        Set<ConstraintViolation<T>> result = executableValidator.validateParameters(obj, method, params);
        return result;
    }


    private Set<ConstraintViolation> validMethodBeanParams(Object[] params) {
        Set<ConstraintViolation> result = new HashSet<>();
        Arrays.stream(params).forEach(p -> {
            if (p instanceof BaseDojo) {
                Set<ConstraintViolation<Object>> pSet = validator.validate(p);
                result.addAll(pSet);
            }
        });
        return result;
    }


}


class FieldError implements Serializable {
    private String name;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class ParamValidException extends Exception {
    private List<FieldError> fieldErrors;

    public ParamValidException(List<FieldError> errors) {
        super(JsonUtil.objectToJson(errors));
        this.fieldErrors = errors;
    }

}