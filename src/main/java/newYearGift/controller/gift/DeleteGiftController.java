package newYearGift.controller.gift;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import newYearGift.exception.BusinessException;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Candy;
import newYearGift.model.CandyWrapper;
import newYearGift.model.Gift;
import newYearGift.parser.Parser;
import newYearGift.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class DeleteGiftController {

    @FXML
    private TextField idField;
    @FXML
    private Label findNotificationLabel;
    @FXML
    private Button findButton;

    @FXML
    private Label nameLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label descriptionLabel;

    @FXML
    private TableView<CandyWrapper> candyTable;
    @FXML
    private TableColumn<CandyWrapper, Long> idColumn;
    @FXML
    private TableColumn<CandyWrapper, String> nameColumn;
    @FXML
    private TableColumn<CandyWrapper, String> trademarkColumn;
    @FXML
    private TableColumn<CandyWrapper, BigDecimal> sugarColumn;
    @FXML
    private TableColumn<CandyWrapper, Integer> weightInGiftColumn;

    @FXML
    private Label deleteMessageLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Label deleteNotificationLabel;

    private Long currentDeletingGiftId;

    private final GiftService giftService;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public DeleteGiftController(
            GiftService giftService,
            ExceptionHandler exceptionHandler
    ) {
        this.giftService = giftService;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        trademarkColumn.setCellValueFactory(new PropertyValueFactory<>("trademark"));
        sugarColumn.setCellValueFactory(new PropertyValueFactory<>("sugar"));
        weightInGiftColumn.setCellValueFactory(new PropertyValueFactory<>("weightInGift"));
    }

    @FXML
    private void findGift() {
        try {
            currentDeletingGiftId = Parser.parseLong(idField, "Id");
            Gift gift = giftService.getGiftById(currentDeletingGiftId);

            findNotificationLabel.setText("");
            setGiftDataFields(gift);
            setDeleteButtonAndDeleteLabelDisability(false);
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, findNotificationLabel);
            setGiftDataFields(null);
            setDeleteButtonAndDeleteLabelDisability(true);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(findNotificationLabel);
        }
    }

    private void setDeleteButtonAndDeleteLabelDisability(
            boolean bool
    ) {
        deleteButton.setDisable(bool);
        deleteMessageLabel.setDisable(bool);
    }

    private void setGiftDataFields(
            Gift gift
    ) {
        if (gift == null) {
            nameLabel.setText("-");
            authorLabel.setText("-");
            descriptionLabel.setText("-");

            candyTable.getItems().clear();
        } else {
            nameLabel.setText(gift.getName());
            authorLabel.setText(gift.getAuthor());
            descriptionLabel.setText(gift.getDescription());

            candyTable.getItems().clear();
            candyTable.setItems(FXCollections.observableArrayList(
                    candyWeightMapToCandyWrapper(gift.getCandyWeightMap())
            ));
        }
    }

    private List<CandyWrapper> candyWeightMapToCandyWrapper(
            Map<Candy, Integer> candyWeightMap
    ) {
        return candyWeightMap
                .entrySet()
                .stream()
                .map((candyWeightEntry) ->
                        new CandyWrapper(candyWeightEntry.getKey(), candyWeightEntry.getValue()))
                .toList();
    }

    @FXML
    private void deleteGift() {
        try {
            giftService.deleteGift(currentDeletingGiftId);

            deleteNotificationLabel.setStyle("-fx-text-fill: green;");
            deleteNotificationLabel.setText("Gift was deleted");
            setGiftDataFields(null);
            setDeleteButtonAndDeleteLabelDisability(true);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(deleteNotificationLabel);
        }
    }
}
