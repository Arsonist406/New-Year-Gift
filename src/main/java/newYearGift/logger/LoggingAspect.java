package newYearGift.logger;

import jakarta.validation.ConstraintViolationException;
import newYearGift.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger;

    @Autowired
    public LoggingAspect(
            LoggerContainer loggerContainer
    ) {
        this.logger = loggerContainer.getLogger();
    }

    @Before("execution(public * newYearGift.service.impl.*.*(..))")
    public void logServiceBefore(
            JoinPoint joinPoint
    ) {
        logger.info("→ Enter the method: {}, with args: {}", joinPoint.getSignature().toString(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(public * newYearGift.service.impl.*.*(..)))", returning = "result")
    public void logServiceAfterReturning(
            JoinPoint joinPoint,
            Object result
    ) {
        logger.info("← Exited the method: {}, result: {}", joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "execution(public * newYearGift.service.impl.*.*(..)))", throwing = "error")
    public void logServiceAfterThrowing(
            JoinPoint joinPoint,
            Throwable error
    ) {
        if (error instanceof BusinessException) {
            logger.warn("! BusinessException was thrown in the method: {}, message: {}", joinPoint.getSignature().toShortString(), error.getMessage());
        } else if (error instanceof ConstraintViolationException) {
            logger.warn("! ConstraintViolationException was thrown in the method: {}, message: {}", joinPoint.getSignature().toShortString(), error.getMessage());
        } else {
            logger.error("!!! Unpredicted Exception was thrown in the method: {}, message: {}", joinPoint.getSignature().toShortString(), error.getMessage(), error);
        }
    }
}
