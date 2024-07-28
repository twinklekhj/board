package io.github.twinklekhj.board.aop;

import io.github.twinklekhj.board.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class ControllerAspect {
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && execution(* *(..))")
    private void controllerPointcut() {}

    @Around(value = "controllerPointcut()", argNames = "pjp")
    public Object around(ProceedingJoinPoint pjp) {
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = Utils.getClientIP(request);

        Object[] args = pjp.getArgs();
        String uri = request.getRequestURI();

        log.info("* REQUEST [url: {}, ip: {}, params: {}]", uri, ip, Arrays.toString(args));

        try {
            return pjp.proceed();
        } catch (Throwable e) {
            log.warn("* ERROR: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
