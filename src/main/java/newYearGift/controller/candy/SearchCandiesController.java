package newYearGift.controller.candy;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import newYearGift.dto.SearchCandiesDTO;
import newYearGift.exception.BusinessException;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Candy;
import newYearGift.model.CandyType;
import newYearGift.parser.Parser;
import newYearGift.service.CandyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Getter
public class SearchCandiesController {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField trademarkField;
    @FXML
    private ChoiceBox<String> typeBox;
    @FXML
    private TextField minSugarContentField;
    @FXML
    private TextField maxSugarContentField;
    @FXML
    private ChoiceBox<String> sortBox;
    @FXML
    private ChoiceBox<String> orderBox;

    @FXML
    private Button searchButton;
    @FXML
    private Label notificationLabel;
    @FXML
    private Button clearCriteriaFieldsButton;

    @FXML
    private TableView<Candy> candyTable;
    @FXML
    private TableColumn<Candy, Long> idColumn;
    @FXML
    private TableColumn<Candy, String> nameColumn;
    @FXML
    private TableColumn<Candy, String> trademarkColumn;
    @FXML
    private TableColumn<Candy, CandyType> typeColumn;
    @FXML
    private TableColumn<Candy, Integer> weightColumn;
    @FXML
    private TableColumn<Candy, Integer> caloriesColumn;
    @FXML
    private TableColumn<Candy, BigDecimal> fatsColumn;
    @FXML
    private TableColumn<Candy, BigDecimal> proteinsColumn;
    @FXML
    private TableColumn<Candy, BigDecimal> carbohydratesColumn;
    @FXML
    private TableColumn<Candy, BigDecimal> sugarColumn;

    private final CandyService candyService;
    private final ExceptionHandler exceptionHandler;

    @Autowired
    public SearchCandiesController(
            CandyService candyService,
            ExceptionHandler exceptionHandler
    ) {
        this.candyService = candyService;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    private void initialize() {
        typeBox.getItems().addAll(
                "Not specified",
                CandyType.Chocolate.toString(),
                CandyType.Caramel.toString(),
                CandyType.Gummies.toString(),
                CandyType.Lollipop.toString(),
                CandyType.Toffee.toString(),
                CandyType.Fudge.toString(),
                CandyType.Wafer.toString());
        typeBox.setValue("Not specified");

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

        orderBox.getItems().addAll(
                "Ascending",
                "Descending");
        orderBox.setValue("Ascending");

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
    }

    @FXML
    private void searchCandies() {
        List<Candy> candies;
        try {
            if (!idField.getText().isEmpty()) {
                candies = searchById();
            } else {
                candies = searchByCriteria();
            }

            notificationLabel.setText("");
            updateCandyTable(candies);
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, notificationLabel);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(notificationLabel);
        }
    }

    private List<Candy> searchById() {
        Long id = Parser.parseLong(idField, "Id");
        return List.of(candyService.getCandyById(id));
    }

    private List<Candy> searchByCriteria() {
        SearchCandiesDTO dto = buildSearchDTO();
        return candyService.getCandies(dto, sortBox.getValue(), orderBox.getValue());
    }

    private SearchCandiesDTO buildSearchDTO() {
        BigDecimal minSugar = Parser.parseSugarContent(minSugarContentField, "Min", BigDecimal.ZERO);
        BigDecimal maxSugar = Parser.parseSugarContent(maxSugarContentField, "Max", new BigDecimal("100"));

        return new SearchCandiesDTO(
                nameField.getText().trim(),
                trademarkField.getText().trim(),
                getCandyType(typeBox),
                minSugar,
                maxSugar
        );
    }

    private CandyType getCandyType(
            ChoiceBox<String> typeBox
    ) {
        return switch (typeBox.getValue()) {
            case "Not specified" -> null;
            case "Chocolate" -> CandyType.Chocolate;
            case "Caramel" -> CandyType.Caramel;
            case "Gummies" -> CandyType.Gummies;
            case "Lollipop" -> CandyType.Lollipop;
            case "Toffee" -> CandyType.Toffee;
            case "Fudge" -> CandyType.Fudge;
            case "Wafer" -> CandyType.Wafer;
            default -> null;
        };
    }

    private void updateCandyTable(
            List<Candy> candies
    ) {
        if (candies.isEmpty()) {
            notificationLabel.setText("No candies found");
        } else {
            candyTable.setItems(FXCollections.observableList(candies));
        }
    }

    @FXML
    private void clearCriteriaFields() {
        idField.clear();
        nameField.clear();
        trademarkField.clear();
        typeBox.setValue("Not specified");
        minSugarContentField.clear();
        maxSugarContentField.clear();
        sortBox.setValue("Id");
        orderBox.setValue("Ascending");
        notificationLabel.setText("");
    }
}
