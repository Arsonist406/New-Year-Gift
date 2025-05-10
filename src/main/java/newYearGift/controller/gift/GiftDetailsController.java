package newYearGift.controller.gift;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Getter;
import newYearGift.exception.BusinessException;
import newYearGift.model.Candy;
import newYearGift.model.CandyType;
import newYearGift.model.CandyWrapper;
import newYearGift.model.Gift;
import newYearGift.parser.Parser;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Getter
public class GiftDetailsController {

    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label creationDateLabel;

    @FXML
    private TableView<CandyWrapper> candyTable;
    @FXML
    private TableColumn<CandyWrapper, Long> idColumn;
    @FXML
    private TableColumn<CandyWrapper, String> nameColumn;
    @FXML
    private TableColumn<CandyWrapper, String> trademarkColumn;
    @FXML
    private TableColumn<CandyWrapper, CandyType> typeColumn;
    @FXML
    private TableColumn<CandyWrapper, Integer> weightColumn;
    @FXML
    private TableColumn<CandyWrapper, Integer> caloriesColumn;
    @FXML
    private TableColumn<CandyWrapper, BigDecimal> fatsColumn;
    @FXML
    private TableColumn<CandyWrapper, BigDecimal> proteinsColumn;
    @FXML
    private TableColumn<CandyWrapper, BigDecimal> carbohydratesColumn;
    @FXML
    private TableColumn<CandyWrapper, BigDecimal> sugarColumn;
    @FXML
    private TableColumn<CandyWrapper, Integer> weightInGiftColumn;

    @FXML
    private Label totalWeightLabel;

    @FXML
    private ChoiceBox<String> sortBox;
    @FXML
    private TextField minSugarContentField;
    @FXML
    private TextField maxSugarContentField;
    @FXML
    private Button applyCriteriaButton;
    @FXML
    private Button backButton;

    private Gift gift;
    private Scene previousScene;

    @FXML
    public void initialize(
            Gift gift,
            Scene previousScene
    ) {
        this.gift = gift;
        this.previousScene = previousScene;

        idLabel.setText(String.valueOf(gift.getId()));
        nameLabel.setText(gift.getName());
        authorLabel.setText(gift.getAuthor());
        descriptionLabel.setText(gift.getDescription());
        creationDateLabel.setText(String.valueOf(gift.getCreationDate()));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        trademarkColumn.setCellValueFactory(new PropertyValueFactory<>("trademark"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
        fatsColumn.setCellValueFactory(new PropertyValueFactory<>("fats"));
        proteinsColumn.setCellValueFactory(new PropertyValueFactory<>("proteins"));
        carbohydratesColumn.setCellValueFactory(new PropertyValueFactory<>("carbohydrates"));
        sugarColumn.setCellValueFactory(new PropertyValueFactory<>("sugar"));
        weightInGiftColumn.setCellValueFactory(new PropertyValueFactory<>("weightInGift"));

        sortBox.getItems().addAll(
                "Id",
                "Name",
                "Trademark",
                "Weight",
                "Calories",
                "Sugar",
                "Fats",
                "Proteins",
                "Carbohydrates");
        sortBox.setValue("Id");

        applyCriteria();

        totalWeightLabel.setText(String.valueOf(
                getGiftTotalWeight(gift.getCandyWeightMap()))
        );
    }

    private Integer getGiftTotalWeight(
            Map<Candy, Integer> candyWeightMap
    ) {
        if (candyWeightMap.isEmpty()) {
            return 0;
        } else {
            return candyWeightMap
                    .values()
                    .stream()
                    .reduce(0, Integer::sum);
        }
    }

    @FXML
    private void applyCriteria() {
        BigDecimal minSugar;
        try {
            minSugar = Parser.parseSugarContent(minSugarContentField, "Min", BigDecimal.ZERO);
            minSugarContentField.setStyle("-fx-text-fill: #4a6fa5;");
        } catch (BusinessException e) {
            minSugarContentField.setStyle("-fx-text-fill: red;");
            return;
        }

        BigDecimal maxSugar;
        try {
            maxSugar = Parser.parseSugarContent(maxSugarContentField, "Max", new BigDecimal("100"));
            maxSugarContentField.setStyle("-fx-text-fill: #4a6fa5;");
        } catch (BusinessException e) {
            maxSugarContentField.setStyle("-fx-text-fill: red;");
            return;
        }

        Map<Candy, Integer> tempMap = removeCandiesNotInRange(gift.getCandyWeightMap(), minSugar, maxSugar);
        List<CandyWrapper> tempList = candyWeightMapToCandyWrapper(tempMap);
        List<CandyWrapper> finalList = sortCandies(tempList, sortBox.getValue(), "Ascending");

        candyTable.getItems().clear();
        candyTable.setItems(FXCollections.observableArrayList(finalList));
    }

    private Map<Candy, Integer> removeCandiesNotInRange(
            Map<Candy, Integer> candyWeightMap,
            BigDecimal minSugar,
            BigDecimal maxSugar
    ) {
        return candyWeightMap
                .entrySet()
                .stream()
                .filter(entry -> minSugar.compareTo(entry.getKey().getSugar()) <= 0)
                .filter(entry -> maxSugar.compareTo(entry.getKey().getSugar()) >= 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
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

    private List<CandyWrapper> sortCandies(
            List<CandyWrapper> candies,
            String sortBy,
            String orderBy
    ) {
        Comparator<CandyWrapper> comparator = switch (sortBy) {
            case "Id"       -> Comparator.comparing(CandyWrapper::getId);
            case "Name"     -> Comparator.comparing(CandyWrapper::getName);
            case "Trademark"    -> Comparator.comparing(CandyWrapper::getTrademark);
            case "Weight"    -> Comparator.comparing(CandyWrapper::getWeight);
            case "Calories" -> Comparator.comparing(CandyWrapper::getCalories);
            case "Sugar"    -> Comparator.comparing(CandyWrapper::getSugar);
            case "Fats"    -> Comparator.comparing(CandyWrapper::getFats);
            case "Proteins"    -> Comparator.comparing(CandyWrapper::getProteins);
            case "Carbohydrates"    -> Comparator.comparing(CandyWrapper::getCarbohydrates);
            default -> Comparator.comparing(CandyWrapper::getId);
        };

        if ("Descending".equals(orderBy)) {
            comparator = comparator.reversed();
        }

        return candies
                .stream()
                .sorted(comparator)
                .toList();
    }

    @FXML
    private void handleBackButton() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(previousScene);
    }
}
