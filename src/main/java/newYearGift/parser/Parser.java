package newYearGift.parser;

import javafx.scene.control.TextField;
import newYearGift.exception.BusinessException;

import java.math.BigDecimal;

public class Parser {

    static public Integer parseInteger(
            TextField field,
            String fieldName
    ) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new BusinessException(fieldName + " must be a number");
        }
    }

    static public Long parseLong(
            TextField field,
            String fieldName
    ) {
        try {
            return Long.parseLong(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new BusinessException(fieldName + " must be a number");
        }
    }

    static public BigDecimal parseBigDecimal(
            TextField field,
            String fieldName
    ) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(field.getText().trim()));
        } catch (NumberFormatException e) {
            throw new BusinessException(fieldName + " must be a number");
        }
    }

    static public BigDecimal parseSugarContent(
            TextField field,
            String fieldName,
            BigDecimal defaultValue
    ) {
        String input = field.getText().trim();
        if (input == null || input.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return BigDecimal.valueOf(Double.parseDouble(input));
        } catch (NumberFormatException e) {
            throw new BusinessException(fieldName + " must be a number");
        }
    }
}
