package co.raccoons.bookkeeper;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class BookkeeperControllerLogger {

    @Pointcut("within(co.raccoons.bookkeeper..*Controller)")
    private void anyEndpoint() {
    }

    @Around("anyEndpoint()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        var timePoint = System.currentTimeMillis();
        try {
            log.info(timePoint + " Call: " + joinPoint.getSignature().toString());
            var result = joinPoint.proceed();
            log.info(timePoint + " Return: " + result);
            return result;
        } catch (Throwable e) {
            log.info(timePoint + " Exception: " + e);
            throw e;
        }
    }

   /*

    @AfterReturning(pointcut = "logMethodCall()", returning = "result")
    public void adviceOnSuccess(Object result) {
    }

    @AfterThrowing(
            pointcut = "pointcut_reference()",
            throwing = "exception"
    )
    public void adviceOnFailure(JoinPoint joinPoint, Throwable exception) {
    }

    @After("pointcut_reference()")
    public void adviceOnSuccessOrFailure() {
    }

    @Around("pointcut_reference()")
    public Object adviceOnInstead(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed();
    }

     */

}
