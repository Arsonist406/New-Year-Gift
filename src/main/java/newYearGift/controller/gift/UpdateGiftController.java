package newYearGift.controller.gift;

import jakarta.validation.ConstraintViolationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import newYearGift.dto.GiftDTO;
import newYearGift.exception.BusinessException;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Candy;
import newYearGift.model.CandyWrapper;
import newYearGift.model.Gift;
import newYearGift.parser.Parser;
import newYearGift.service.CandyService;
import newYearGift.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Getter
public class UpdateGiftController {

    @FXML
    private TextField idField;
    @FXML
    private Button findGiftButton;
    @FXML
    private Label findGiftNotificationLabel;

    @FXML
    private TextField giftNameField;
    @FXML
    private TextArea giftDescriptionArea;
    @FXML
    private TextField giftAuthorField;

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
    private TextField candyIdToUpdateField;
    @FXML
    private TextField newWeightOfCandyField;
    @FXML
    private Button updateCandyWeightButton;
    @FXML
    private TextField candyIdToDeleteField;
    @FXML
    private Button deleteCandyButton;
    @FXML
    private Label updateAndDeleteCandyNotificationLabel;

    @FXML
    private TextField candyIdToFoundField;
    @FXML
    private Button findCandyButton;
    @FXML
    private Label findCandyNotificationLabel;

    @FXML
    private Label candyNameLabel;
    @FXML
    private Label candyTrademarkLabel;
    @FXML
    private Label candyTypeLabel;
    @FXML
    private Label candyWeightLabel;
    @FXML
    private Label candyCaloriesLabel;
    @FXML
    private Label candySugarLabel;
    @FXML
    private Label candyFatsLabel;
    @FXML
    private Label candyProteinsLabel;
    @FXML
    private Label candyCarbohydratesLabel;

    @FXML
    private Label weightOfCandyInGiftMessageLabel;
    @FXML
    private TextField weightOfCandyInGiftField;
    @FXML
    private Button addCandyButton;

    @FXML
    private Label addCandyNotificationLabel;

    @FXML
    private Button updateGiftButton;
    @FXML
    private Label updateGiftNotificationLabel;

    @FXML
    private Button clearDataFieldsButton;

    private final GiftService giftService;
    private final CandyService candyService;
    private final ExceptionHandler exceptionHandler;

    private Candy newAddedCandy;
    private Map<Long, Integer> candyIdWeightMap;
    private Long currentUpdatingGiftId;


    @Autowired
    public UpdateGiftController(
            GiftService giftService,
            CandyService candyService,
            ExceptionHandler exceptionHandler
    ) {
        this.giftService = giftService;
        this.candyService = candyService;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    private void initialize() {
        candyIdWeightMap = new HashMap<>();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        trademarkColumn.setCellValueFactory(new PropertyValueFactory<>("trademark"));
        sugarColumn.setCellValueFactory(new PropertyValueFactory<>("sugar"));
        weightInGiftColumn.setCellValueFactory(new PropertyValueFactory<>("weightInGift"));
    }

    @FXML
    private void findGift() {
        try {
            currentUpdatingGiftId = Parser.parseLong(idField, "Id");
            Gift gift = giftService.getGiftById(currentUpdatingGiftId);

            findGiftNotificationLabel.setText("");
            setViewElementsDisability(false);
            setGiftDataFields(gift);
            setCandyIdWeightMap(gift);

            if (candyIdWeightMap.isEmpty()) {
                setDeleteCandyAndChangeWeightPartsDisability(true);
            } else {
                setDeleteCandyAndChangeWeightPartsDisability(false);
            }
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, findGiftNotificationLabel);
            setViewElementsDisability(true);
            clearDataFields();
            candyIdWeightMap.clear();
            setDeleteCandyAndChangeWeightPartsDisability(true);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(findGiftNotificationLabel);
        }
    }

    private void setViewElementsDisability(
            boolean bool
    ) {
        giftNameField.setDisable(bool);
        giftDescriptionArea.setDisable(bool);
        giftAuthorField.setDisable(bool);
        candyTable.setDisable(bool);

        updateGiftButton.setDisable(bool);
        clearDataFieldsButton.setDisable(bool);

        candyIdToFoundField.setDisable(bool);
        findCandyButton.setDisable(bool);
    }

    private void setGiftDataFields(
            Gift gift
    ) {
        giftNameField.setText(gift.getName());
        giftDescriptionArea.setText(gift.getDescription());
        giftAuthorField.setText(gift.getAuthor());

        candyTable.getItems().clear();
        candyTable.setItems(FXCollections.observableArrayList(
                candyWeightMapToCandyWrapper(gift.getCandyWeightMap())
        ));
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

    private void setCandyIdWeightMap(
            Gift gift
    ) {
        candyIdWeightMap.clear();
        candyIdWeightMap = gift
                .getCandyWeightMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        candyWeightEntry -> candyWeightEntry.getKey().getId(),
                        candyWeightEntry -> candyWeightEntry.getValue()));
    }

    private void setDeleteCandyAndChangeWeightPartsDisability(
            boolean bool
    ) {
        candyIdToUpdateField.setDisable(bool);
        newWeightOfCandyField.setDisable(bool);
        updateCandyWeightButton.setDisable(bool);
        candyIdToDeleteField.setDisable(bool);
        deleteCandyButton.setDisable(bool);
    }

    @FXML
    private void updateCandyWeight() {
        try {
            Long id = Parser.parseLong(candyIdToUpdateField, "Id");
            Integer newCandyWeightInGift = Parser.parseInteger(newWeightOfCandyField, "Weight of candy in gift");

            if (newCandyWeightInGift <= 0) {
                throw new BusinessException("Weight of candy in gift must be bigger than 0");
            }

            if (candyIdWeightMap.containsKey(id)) {
                candyIdWeightMap.put(id, newCandyWeightInGift);
            } else {
                throw new BusinessException("Candy not found by id " + id);
            }

            getCandyFromTable(id).setWeightInGift(newCandyWeightInGift);
            candyTable.refresh();
            candyIdToUpdateField.clear();
            newWeightOfCandyField.clear();
            updateAndDeleteCandyNotificationLabel.setText("");
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, updateAndDeleteCandyNotificationLabel);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(updateAndDeleteCandyNotificationLabel);
        }
    }

    private CandyWrapper getCandyFromTable(
            Long id
    ) {
        return candyTable
                .getItems()
                .stream()
                .filter(candyWrapper ->
                        Objects.equals(candyWrapper.getId(), id))
                .findFirst()
                .get();
    }

    @FXML
    private void deleteCandy() {
        try {
            Long id = Parser.parseLong(candyIdToDeleteField, "Id");

            if (candyIdWeightMap.containsKey(id)) {
                candyIdWeightMap.remove(id);
            } else {
                throw new BusinessException("Candy not found by id " + id);
            }

            candyTable.getItems().remove(getCandyFromTable(id));
            candyTable.refresh();
            candyIdToDeleteField.clear();
            updateAndDeleteCandyNotificationLabel.setText("");
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, updateAndDeleteCandyNotificationLabel);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(updateAndDeleteCandyNotificationLabel);
        }

        if (candyIdWeightMap.isEmpty()) {
            setDeleteCandyAndChangeWeightPartsDisability(true);
        }
    }

    @FXML
    private void findCandy() {
        addCandyNotificationLabel.setText("");

        try {
            Long id = Parser.parseLong(candyIdToFoundField, "Id");
            newAddedCandy = candyService.getCandyById(id);

            findCandyNotificationLabel.setText("");
            setCandyDataLabels(newAddedCandy);
            setAddCandyPartsDisability(false);
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, findCandyNotificationLabel);
            setCandyDataLabels(null);
            setAddCandyPartsDisability(true);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(findCandyNotificationLabel);
        }
    }

    private void setCandyDataLabels(
            Candy candy
    ) {
        if (candy == null) {
            candyNameLabel.setText("-");
            candyTrademarkLabel.setText("-");
            candyTypeLabel.setText("-");
            candyWeightLabel.setText("-");
            candyCaloriesLabel.setText("-");
            candySugarLabel.setText("-");
            candyFatsLabel.setText("-");
            candyProteinsLabel.setText("-");
            candyCarbohydratesLabel.setText("-");
        } else {
            candyNameLabel.setText(candy.getName());
            candyTrademarkLabel.setText(candy.getTrademark());
            candyTypeLabel.setText(String.valueOf(candy.getType()));
            candyWeightLabel.setText(String.valueOf(candy.getWeight()));
            candyCaloriesLabel.setText(String.valueOf(candy.getCalories()));
            candySugarLabel.setText(String.valueOf(candy.getSugar()));
            candyFatsLabel.setText(String.valueOf(candy.getFats()));
            candyProteinsLabel.setText(String.valueOf(candy.getProteins()));
            candyCarbohydratesLabel.setText(String.valueOf(candy.getCarbohydrates()));
        }
    }

    private void setAddCandyPartsDisability(
            boolean bool
    ) {
        weightOfCandyInGiftMessageLabel.setDisable(bool);
        weightOfCandyInGiftField.setDisable(bool);
        addCandyButton.setDisable(bool);
    }

    @FXML
    private void addCandy() {
        try {
            Integer candyWeightInGift = Parser.parseInteger(weightOfCandyInGiftField, "Weight of candy in gift");

            if (candyWeightInGift <= 0) {
                throw new BusinessException("Weight of candy in gift must be bigger than 0");
            }

            candyIdWeightMap.put(newAddedCandy.getId(), candyWeightInGift);

            if (isCandyAlreadyInTable()) {
                changeOldCandyWeight(candyWeightInGift);

                candyTable.refresh();
            } else {
                CandyWrapper newCandyInGift = new CandyWrapper(newAddedCandy, candyWeightInGift);

                candyTable.getItems().addFirst(newCandyInGift);
            }

            newAddedCandy = null;
            candyIdToFoundField.clear();
            setCandyDataLabels(null);
            setAddCandyPartsDisability(true);
            weightOfCandyInGiftField.clear();
            addCandyNotificationLabel.setText("");
            setDeleteCandyAndChangeWeightPartsDisability(false);
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, addCandyNotificationLabel);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(addCandyNotificationLabel);
        }
    }

    private boolean isCandyAlreadyInTable() {
        return candyTable
                .getItems()
                .stream()
                .anyMatch(candyWrapper ->
                        candyWrapper.getId().equals(newAddedCandy.getId()));
    }

    private void changeOldCandyWeight(
            Integer candyWeightInGift
    ) {
        candyTable.getItems()
                .stream()
                .filter(candyWrapper ->
                        candyWrapper.getId().equals(newAddedCandy.getId()))
                .forEach(candyWrapper ->
                        candyWrapper.setWeightInGift(candyWeightInGift));
    }

    @FXML
    private void updateGift() {
        try {
            giftService.updateGift(currentUpdatingGiftId, buildGiftDTO());

            updateGiftNotificationLabel.setStyle("-fx-text-fill: green;");
            updateGiftNotificationLabel.setText("Gift data was update");
        } catch (ConstraintViolationException e) {
            exceptionHandler.handleConstraintViolationException(e, updateGiftNotificationLabel);
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, updateGiftNotificationLabel);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(updateGiftNotificationLabel);
        }
    }

    private GiftDTO buildGiftDTO() {
        return new GiftDTO(
                null,
                giftNameField.getText().trim(),
                giftDescriptionArea.getText().trim(),
                giftAuthorField.getText().trim(),
                Instant.now(),
                candyIdWeightMap
        );
    }

    @FXML
    private void clearDataFields() {
        giftNameField.clear();
        giftDescriptionArea.clear();
        giftAuthorField.clear();
        candyTable.getItems().clear();
    }

}
