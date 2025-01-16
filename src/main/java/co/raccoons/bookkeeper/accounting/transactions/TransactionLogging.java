package co.raccoons.bookkeeper.accounting.transactions;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TransactionLogging {

    /*
    @Pointcut("execution(public void org.raccoons.bookkeeper.TransactionController.find*(..))")
    private void pointcut_reference() {
    }

    @Before("pointcut_reference()")
    public void adviceBeforeMethod(JoinPoint joinPoint) {
        joinPoint.getKind();
    }

    @AfterReturning(
            pointcut = "pointcut_reference()",
            returning = "someObject"
    )
    public void adviceOnSuccess(JoinPoint joinPoint, Object someObject) {
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
