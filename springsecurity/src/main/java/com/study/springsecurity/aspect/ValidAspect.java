package com.study.springsecurity.aspect;

import com.study.springsecurity.dto.request.ReqSignupDto;
import com.study.springsecurity.exception.ValidException;
import com.study.springsecurity.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.beans.BeanProperty;

@Slf4j
@Aspect
@Component
public class ValidAspect {

    @Autowired
    private SignupService signupService;

    @Pointcut("@annotation(com.study.springsecurity.aspect.annotation.ValidAop)")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();

        BeanPropertyBindingResult bindingResult = null;
        for (Object arg : args) {
            if (arg.getClass() == BeanPropertyBindingResult.class) {
                bindingResult = (BeanPropertyBindingResult) arg;
                break;
            }
        }
        switch (proceedingJoinPoint.getSignature().getName()) {
            case "signup":
                validSignupDto(args, bindingResult);
                break;
        }

        if (bindingResult.hasErrors()) {
            throw new ValidException("유효성 검사 오류", bindingResult.getFieldErrors());
        }

        return proceedingJoinPoint.proceed();
    }

    private void validSignupDto(Object[] args, BeanPropertyBindingResult bindingResult) {
        for (Object arg : args) {
            if (arg.getClass() == ReqSignupDto.class) {
                ReqSignupDto dto = (ReqSignupDto) arg;
                if (!dto.getPassword().equals(dto.getCheckPassword())) {
                    FieldError fieldError = new FieldError("checkpassword", "checkpassword", "비밀번호가 일치하지 않습니다.");
                    bindingResult.addError(fieldError);
                    break;
                }
                if (signupService.isDulicatedUsername(dto.getUsername())) {
                    FieldError fieldError = new FieldError("username", "username", "이미 존재하는 사용자 이름입니다.");
                    bindingResult.addError(fieldError);
                }
            }
            break;
        }
    }
}
