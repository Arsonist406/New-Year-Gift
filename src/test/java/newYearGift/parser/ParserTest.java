package newYearGift.parser;

import javafx.scene.control.TextField;
import newYearGift.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(ApplicationExtension.class)
class ParserTest {

    @Test
    void parseInteger_whenParsedSuccessfully_returnInteger() {
        TextField textfield = new TextField();
        textfield.setText("100");

        Integer actual = Parser.parseInteger(textfield, "test");

        assertEquals(100, actual);
    }

    @Test
    void parseInteger_whenParsedUnsuccessfully_throwsException() {
        TextField textfield = new TextField();
        textfield.setText("badText");

        assertThrows(
                BusinessException.class,
                () -> Parser.parseInteger(textfield, "test")
        );
    }

    @Test
    void parseLong_whenParsedSuccessfully_returnLong() {
        TextField textfield = new TextField();
        textfield.setText("100");

        Long actual = Parser.parseLong(textfield, "test");

        assertEquals(100L, actual);
    }

    @Test
    void parseLong_whenParsedUnsuccessfully_throwsException() {
        TextField textfield = new TextField();
        textfield.setText("badText");

        assertThrows(
                BusinessException.class,
                () -> Parser.parseLong(textfield, "test")
        );
    }

    @Test
    void parseBigDecimal_whenParsedSuccessfully_returnBigDecimal() {
        TextField textfield = new TextField();
        textfield.setText("100");

        BigDecimal actual = Parser.parseBigDecimal(textfield, "test");

        assertEquals(BigDecimal.valueOf(100.0), actual);
    }

    @Test
    void parseBigDecimal_whenParsedUnsuccessfully_throwsException() {
        TextField textfield = new TextField();
        textfield.setText("badText");

        assertThrows(
                BusinessException.class,
                () -> Parser.parseBigDecimal(textfield, "test")
        );
    }

    @Test
    void parseSugarContent_whenParsedSuccessfully_returnBigDecimal() {
        TextField textfield = new TextField();
        textfield.setText("21");

        BigDecimal actual = Parser.parseSugarContent(textfield, "Min", BigDecimal.ZERO);

        assertEquals(BigDecimal.valueOf(21.0), actual);
    }

    @Test
    void parseSugarContent_whenParsedUnsuccessfully_throwsException() {
        TextField textfield = new TextField();
        textfield.setText("error");

        assertThrows(
                BusinessException.class,
                () -> Parser.parseSugarContent(textfield, "Min", BigDecimal.ZERO)
        );
    }
}
