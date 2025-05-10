package newYearGift.controller.candy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import newYearGift.exception.BusinessException;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Candy;
import newYearGift.parser.Parser;
import newYearGift.service.CandyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DeleteCandyController {

    @FXML
    private TextField idField;
    @FXML
    private Button findButton;
    @FXML
    private Label findNotificationLabel;

    @FXML
    private Label deleteMessageLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Label deleteNotificationLabel;

    @FXML
    private Label nameLabel;
    @FXML
    private Label trademarkLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label weightLabel;
    @FXML
    private Label caloriesLabel;
    @FXML
    private Label sugarLabel;
    @FXML
    private Label fatsLabel;
    @FXML
    private Label proteinsLabel;
    @FXML
    private Label carbohydratesLabel;

    private final CandyService candyService;
    private final ExceptionHandler exceptionHandler;

    private Long currentDeletingCandyId;

    @Autowired
    public DeleteCandyController(
            CandyService candyService,
            ExceptionHandler exceptionHandler
    ) {
        this.candyService = candyService;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    private void findCandy() {
        try {
            currentDeletingCandyId = Parser.parseLong(idField, "Id");
            Candy foundCandy = candyService.getCandyById(currentDeletingCandyId);

            findNotificationLabel.setText("");
            setCandyDataLabels(foundCandy);
            setDeleteButtonAndDeleteLabelDisability(false);
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, findNotificationLabel);
            setCandyDataLabels(null);
            setDeleteButtonAndDeleteLabelDisability(true);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictableException(findNotificationLabel);
        }
    }

    private void setCandyDataLabels(
            Candy candy
    ) {
        if (candy == null) {
            nameLabel.setText("-");
            trademarkLabel.setText("-");
            typeLabel.setText("-");
            weightLabel.setText("-");
            caloriesLabel.setText("-");
            sugarLabel.setText("-");
            fatsLabel.setText("-");
            proteinsLabel.setText("-");
            carbohydratesLabel.setText("-");
        } else {
            nameLabel.setText(candy.getName());
            trademarkLabel.setText(candy.getTrademark());
            typeLabel.setText(String.valueOf(candy.getType()));
            weightLabel.setText(String.valueOf(candy.getWeight()));
            caloriesLabel.setText(String.valueOf(candy.getCalories()));
            sugarLabel.setText(String.valueOf(candy.getSugar()));
            fatsLabel.setText(String.valueOf(candy.getFats()));
            proteinsLabel.setText(String.valueOf(candy.getProteins()));
            carbohydratesLabel.setText(String.valueOf(candy.getCarbohydrates()));
        }
    }

    private void setDeleteButtonAndDeleteLabelDisability(
            boolean bool
    ) {
        deleteButton.setDisable(bool);
        deleteMessageLabel.setDisable(bool);
    }

    @FXML
    private void deleteCandy() {
        try {
            candyService.deleteCandy(currentDeletingCandyId);

            deleteNotificationLabel.setStyle("-fx-text-fill: green;");
            deleteNotificationLabel.setText("Candy was deleted");
            setCandyDataLabels(null);
            setDeleteButtonAndDeleteLabelDisability(true);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictableException(deleteNotificationLabel);
        }
    }
}
