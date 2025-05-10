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
public class UpdateCandyController {

    @FXML
    private TextField idField;
    @FXML
    private Label findNotificationLabel;
    @FXML
    private Button findButton;

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
    private Button updateButton;
    @FXML
    private Label updateNotificationLabel;

    @FXML
    private Button clearDataFieldsButton;

    private final CandyService candyService;
    private final ExceptionHandler exceptionHandler;

    private Long currentUpdatingCandyId;

    @Autowired
    public UpdateCandyController(
            CandyService candyService,
            ExceptionHandler exceptionHandler
    ) {
        this.candyService = candyService;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    private void initialize() {
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
    private void findCandy() {
        try {
            currentUpdatingCandyId = Parser.parseLong(idField, "Id");
            Candy foundCandy = candyService.getCandyById(currentUpdatingCandyId);

            findNotificationLabel.setText("");
            setCandyDataFields(foundCandy);
            setViewElementsDisability(false);
        } catch (BusinessException e) {
            clearDataFields();
            exceptionHandler.handleBusinessException(e, findNotificationLabel);
            setViewElementsDisability(true);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictableException(findNotificationLabel);
        }
    }

    private void setCandyDataFields(
            Candy candy
    ) {
        nameField.setText(candy.getName());
        trademarkField.setText(candy.getTrademark());
        typeBox.setValue(candy.getType());
        weightField.setText(String.valueOf(candy.getWeight()));
        caloriesField.setText(String.valueOf(candy.getCalories()));
        sugarField.setText(String.valueOf(candy.getSugar()));
        fatsField.setText(String.valueOf(candy.getFats()));
        proteinsField.setText(String.valueOf(candy.getProteins()));
        carbohydratesField.setText(String.valueOf(candy.getCarbohydrates()));
    }

    private void setViewElementsDisability(
            boolean bool
    ) {
        nameField.setDisable(bool);
        trademarkField.setDisable(bool);
        typeBox.setDisable(bool);
        weightField.setDisable(bool);
        caloriesField.setDisable(bool);
        sugarField.setDisable(bool);
        fatsField.setDisable(bool);
        proteinsField.setDisable(bool);
        carbohydratesField.setDisable(bool);

        sugarSlider.setDisable(bool);
        fatsSlider.setDisable(bool);
        proteinsSlider.setDisable(bool);
        carbohydratesSlider.setDisable(bool);

        updateButton.setDisable(bool);
        clearDataFieldsButton.setDisable(bool);
    }

    @FXML
    private void updateCandy() {
        try {
            candyService.updateCandy(currentUpdatingCandyId, buildCandyDTO());

            updateNotificationLabel.setStyle("-fx-text-fill: green;");
            updateNotificationLabel.setText("Candy data was update");
        } catch (ConstraintViolationException e) {
            exceptionHandler.handleConstraintViolationException(e, updateNotificationLabel);
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, updateNotificationLabel);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictableException(updateNotificationLabel);
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
        updateNotificationLabel.setText("");
    }
}
