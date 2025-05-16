package newYearGift.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.util.Collections;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(ApplicationExtension.class)
class ExceptionHandlerTest {

    private ExceptionHandler exceptionHandler;

    @BeforeEach
    void init() {
        exceptionHandler = new ExceptionHandler();
    }

    @Test
    void handleBusinessException_setNotificationLabel() {

        String text = "test";
        BusinessException ex = new BusinessException(text);
        Label label = new Label();

        exceptionHandler.handleBusinessException(ex, label);
        String actual = label.getText();

        assertEquals(text, actual);
    }

    @Test
    void handleConstraintViolationException_setNotificationLabel() {
        Path.Node node = mock(Path.Node.class);
        when(node.getName()).thenReturn("name");

        Iterator<Path.Node> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(node);

        Path path = mock(Path.class);
        when(path.iterator()).thenReturn(iterator);
        when(path.toString()).thenReturn("name");

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be empty");

        ConstraintViolationException exception =
                new ConstraintViolationException("Validation failed", Collections.singleton(violation));
        Label label = new Label();


        exceptionHandler.handleConstraintViolationException(exception, label);


        assertEquals("Name must not be empty", label.getText());
    }

    @Test
    void handleUnpredictedException_setNotificationLabel() {
        String expected = "!Unpredictable error, see the logs!";
        Label label = new Label();

        exceptionHandler.handleUnpredictedException(label);

        String actual = label.getText();
        assertEquals(expected, actual);
    }
}
