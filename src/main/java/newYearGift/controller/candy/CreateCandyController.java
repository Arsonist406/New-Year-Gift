package newYearGift.controller.candy;

import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import newYearGift.dto.CandyDTO;
import newYearGift.exception.BusinessException;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Candy;
import newYearGift.model.CandyType;
import newYearGift.parser.Parser;
import newYearGift.service.CandyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Getter
public class CreateCandyController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField trademarkField;
    @FXML
    private ChoiceBox<CandyType> typeBox;
    @FXML
    private TextField weightField;
    @FXML
    private TextField caloriesField;

    @FXML
    private TextField sugarField;
    @FXML
    private Slider sugarSlider;
    @FXML
    private TextField fatsField;
    @FXML
    private Slider fatsSlider;
    @FXML
    private TextField proteinsField;
    @FXML
    private Slider proteinsSlider;
    @FXML
    private TextField carbohydratesField;
    @FXML
    private Slider carbohydratesSlider;

    @FXML
    private Button createButton;
    @FXML
    private Label notificationLabel;
    @FXML
    private Button clearDataFieldsButton;

    private final CandyService candyService;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public CreateCandyController(
            CandyService candyService,
            ExceptionHandler exceptionHandler
    ) {
        this.candyService = candyService;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    public void initialize() {

        typeBox.getItems().addAll(
                CandyType.Chocolate,
                CandyType.Caramel,
                CandyType.Gummies,
                CandyType.Lollipop,
                CandyType.Toffee,
                CandyType.Fudge,
                CandyType.Wafer);
        typeBox.setValue(CandyType.Chocolate);

        initListenersForSliders();
    }

    private void initListenersForSliders() {

        sugarSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) ->
                        sugarField.setText(String.format(Locale.US, "%.2f", newValue.doubleValue()))
        );

        fatsSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) ->
                        fatsField.setText(String.format(Locale.US, "%.2f", newValue.doubleValue()))
        );

        proteinsSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) ->
                        proteinsField.setText(String.format(Locale.US, "%.2f", newValue.doubleValue()))
        );

        carbohydratesSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) ->
                        carbohydratesField.setText(String.format(Locale.US, "%.2f", newValue.doubleValue()))
        );
    }

    @FXML
    public void createCandy() {
        try {
            candyService.createCandy(buildCandyDTO());

            notificationLabel.setStyle("-fx-text-fill: green;");
            notificationLabel.setText("New candy has been created");
        } catch (ConstraintViolationException e) {
            exceptionHandler.handleConstraintViolationException(e, notificationLabel);
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, notificationLabel);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(notificationLabel);
        }
    }

    private CandyDTO buildCandyDTO() {
        return new CandyDTO(
                null,
                nameField.getText().trim(),
                trademarkField.getText().trim(),
                typeBox.getValue(),
                Parser.parseInteger(weightField, "Weight"),
                Parser.parseInteger(caloriesField, "Calories"),
                Parser.parseBigDecimal(sugarField, "Sugar"),
                Parser.parseBigDecimal(fatsField, "Fats"),
                Parser.parseBigDecimal(proteinsField, "Proteins"),
                Parser.parseBigDecimal(carbohydratesField, "Carbohydrates")
        );
    }

    @FXML
    private void clearDataFields() {
        nameField.clear();
        trademarkField.clear();
        weightField.setText("1");
        caloriesField.setText("0");
        sugarField.setText("0");
        fatsField.setText("0");
        proteinsField.setText("0");
        carbohydratesField.setText("0");
        notificationLabel.setText("");
    }
}
