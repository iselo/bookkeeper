package co.raccoons.bookkeeper;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * The Bookkeeper REST controller logger.
 */
@Component
@Aspect
@Slf4j
final class BookkeeperControllerLogger {

    /**
     * Logs normal and exceptional states of the execution of any endpoint for
     * any REST controller.
     */
    @Around("anyEndpoint()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        var timePoint = System.currentTimeMillis();
        try {
            log.info(timePoint + " Call: " + joinPoint.getSignature().toString());
            var result = joinPoint.proceed();
            log.info(timePoint + " Return: " + result);
            return result;
        } catch (Throwable e) {
            log.info(timePoint + " Exception: " + joinPoint.getSignature() + " | " + e);
            throw e;
        }
    }

    @Pointcut("@within(org.springframework.web.bind.annotation.RequestMapping)")
    private void anyEndpoint() {
    }
}
