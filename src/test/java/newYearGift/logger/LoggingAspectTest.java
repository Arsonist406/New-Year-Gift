package newYearGift.logger;

import jakarta.validation.ConstraintViolationException;
import newYearGift.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    private Logger logger;
    private LoggingAspect loggingAspect;
    private JoinPoint joinPoint;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);

        LoggerContainer loggerContainer = mock(LoggerContainer.class);
        when(loggerContainer.getLogger())
                .thenReturn(logger);

        loggingAspect = new LoggingAspect(loggerContainer);

        joinPoint = mock(JoinPoint.class);
        when(joinPoint.getSignature())
                .thenReturn(mock(Signature.class));
    }

    @Test
    void logServiceBefore_logSignatureAndArgs() {
        String signature = "signature";
        when(joinPoint.getSignature().toString())
                .thenReturn(signature);
        String[] args = {"arg1", "arg2", "arg3"};
        when(joinPoint.getArgs())
                .thenReturn(args);


        loggingAspect.logServiceBefore(joinPoint);


        verify(logger)
                .info("→ Enter the method: {}, with args: {}", signature, args);
    }

    @Test
    void logServiceAfterReturning_logSignatureAndResult() {
        String signature = "signature";
        when(joinPoint.getSignature().toShortString())
                .thenReturn(signature);
        Object result = "result";


        loggingAspect.logServiceAfterReturning(joinPoint, result);


        verify(logger)
                .info("← Exited the method: {}, result: {}", signature, result);
    }

    @Test
    void logServiceAfterThrowing_whenCatchBusinessException_logSignatureAndErrorMessage() {
        String signature = "signature";
        when(joinPoint.getSignature().toShortString())
                .thenReturn(signature);
        BusinessException error = new BusinessException("error");


        loggingAspect.logServiceAfterThrowing(joinPoint, error);


        verify(logger)
                .warn("! BusinessException was thrown in the method: {}, message: {}", signature, error.getMessage());
    }

    @Test
    void logServiceAfterThrowing_whenCatchConstraintViolationException_logSignatureAndErrorMessage() {
        String signature = "signature";
        when(joinPoint.getSignature().toShortString())
                .thenReturn(signature);
        ConstraintViolationException error = new ConstraintViolationException("error", null);


        loggingAspect.logServiceAfterThrowing(joinPoint, error);


        verify(logger)
                .warn("! ConstraintViolationException was thrown in the method: {}, message: {}", signature, error.getMessage());
    }

    @Test
    void logServiceAfterThrowing_whenCatchOtherExceptions_logSignatureAndErrorMessageAndStacktrace() {
        String signature = "signature";
        when(joinPoint.getSignature().toShortString())
                .thenReturn(signature);
        Throwable error = new Throwable("error");


        loggingAspect.logServiceAfterThrowing(joinPoint, error);


        verify(logger)
                .error("!!! Unpredicted Exception was thrown in the method: {}, message: {}", signature, error.getMessage(), error);
    }
}