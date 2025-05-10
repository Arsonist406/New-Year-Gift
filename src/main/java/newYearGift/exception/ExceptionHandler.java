package newYearGift.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler {

    public void handleBusinessException(
            BusinessException e,
            Label notificationLabel
    ) {
        notificationLabel.setStyle("-fx-text-fill: red;");
        notificationLabel.setText(e.getMessage());
    }

    public void handleConstraintViolationException(
            ConstraintViolationException e,
            Label notificationLabel
    ) {
        ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();

        String fullPath = violation.getPropertyPath().toString();

        String fieldName = fullPath.substring(fullPath.lastIndexOf('.') + 1);
        fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        String errorMessage = violation.getMessage();
        errorMessage = errorMessage.substring(0, 1).toLowerCase() + errorMessage.substring(1);

        notificationLabel.setStyle("-fx-text-fill: red;");
        notificationLabel.setText(fieldName + " " + errorMessage);
    }

    public void handleUnpredictableException(
            Label notificationLabel
    ) {
        notificationLabel.setStyle("-fx-text-fill: red;");
        notificationLabel.setText("!Unpredictable error, see the logs!");
    }
}
