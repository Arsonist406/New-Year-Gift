package newYearGift.controller.candy;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import newYearGift.NewYearGiftApplication;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Candy;
import newYearGift.model.CandyType;
import newYearGift.service.CandyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class SearchCandiesControllerTest {

    private CandyService candyService;
    private ExceptionHandler exceptionHandler;
    private SearchCandiesController controller;

    @Start
    void start(Stage stage) throws Exception {
        candyService = mock(CandyService.class);
        exceptionHandler = mock(ExceptionHandler.class);
        controller = new SearchCandiesController(candyService, exceptionHandler);

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/candy/searchCandies.fxml"));
        loader.setControllerFactory(type -> controller);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Test New Year Gift");
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setResizable(false);
        stage.setScene(scene);

        stage.show();
    }

    @Test
    void initialize_whenSuccessfully_setTypeBoxAndSortBoxAndOrderBoxAndTableCells() {
        Assertions.assertThat(controller.getTypeBox().getItems())
                .containsExactly(
                        "Not specified",
                        CandyType.Chocolate.toString(),
                        CandyType.Caramel.toString(),
                        CandyType.Gummies.toString(),
                        CandyType.Lollipop.toString(),
                        CandyType.Toffee.toString(),
                        CandyType.Fudge.toString(),
                        CandyType.Wafer.toString());

        Assertions.assertThat(controller.getTypeBox().getValue())
                .isEqualTo("Not specified");


        Assertions.assertThat(controller.getSortBox().getItems())
                .containsExactly(
                        "Id",
                        "Name",
                        "Trademark",
                        "Weight",
                        "Calories",
                        "Sugar",
                        "Fats",
                        "Proteins",
                        "Carbohydrates");

        Assertions.assertThat(controller.getSortBox().getValue())
                .isEqualTo("Id");


        Assertions.assertThat(controller.getOrderBox().getItems())
                .containsExactly(
                        "Ascending",
                        "Descending");

        Assertions.assertThat(controller.getOrderBox().getValue())
                .isEqualTo("Ascending");


        Assertions.assertThat(controller.getIdColumn().getText())
                .isEqualTo("Id");
        Assertions.assertThat(controller.getNameColumn().getText())
                .isEqualTo("Name");
        Assertions.assertThat(controller.getTrademarkColumn().getText())
                .isEqualTo("Trademark");
        Assertions.assertThat(controller.getTypeColumn().getText())
                .isEqualTo("Type");
        Assertions.assertThat(controller.getWeightColumn().getText())
                .isEqualTo("Weight");
        Assertions.assertThat(controller.getCaloriesColumn().getText())
                .isEqualTo("Calories");
        Assertions.assertThat(controller.getFatsColumn().getText())
                .isEqualTo("Fats");
        Assertions.assertThat(controller.getProteinsColumn().getText())
                .isEqualTo("Proteins");
        Assertions.assertThat(controller.getCarbohydratesColumn().getText())
                .isEqualTo("Carbohydrates");
        Assertions.assertThat(controller.getSugarColumn().getText())
                .isEqualTo("Sugar");
    }

    @Test
    void searchCandies_whenSearchingByNotSpecifiedAndSuccessfully_showsCandiesData(FxRobot robot) {
        Candy candy1 = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Chocolate,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        Candy candy2 = spy(new Candy(
                2L,
                "name",
                "trademark",
                CandyType.Chocolate,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        Candy candy3 = spy(new Candy(
                3L,
                "name",
                "trademark",
                CandyType.Chocolate,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandies(any(), any(), any()))
                .thenReturn(List.of(candy1, candy2, candy3));


        robot.interact(() -> controller.getTypeBox().setValue("Not specified"));
        robot.clickOn("#searchButton");


        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEqualTo(FXCollections.observableList(List.of(candy1, candy2, candy3)));
    }

    @Test
    void searchCandies_whenSearchingByChocolateAndSuccessfully_showsCandiesData(FxRobot robot) {
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Chocolate,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandies(any(), any(), any()))
                .thenReturn(List.of(candy));


        robot.interact(() -> controller.getTypeBox().setValue(CandyType.Chocolate.toString()));
        robot.clickOn("#searchButton");


        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEqualTo(FXCollections.observableList(List.of(candy)));
    }

    @Test
    void searchCandies_whenSearchingByCaramelAndSuccessfully_showsCandiesData(FxRobot robot) {
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Caramel,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandies(any(), any(), any()))
                .thenReturn(List.of(candy));


        robot.interact(() -> controller.getTypeBox().setValue(CandyType.Caramel.toString()));
        robot.clickOn("#searchButton");


        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEqualTo(FXCollections.observableList(List.of(candy)));
    }

    @Test
    void searchCandies_whenSearchingByGummiesAndSuccessfully_showsCandiesData(FxRobot robot) {
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Gummies,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandies(any(), any(), any()))
                .thenReturn(List.of(candy));


        robot.interact(() -> controller.getTypeBox().setValue(CandyType.Gummies.toString()));
        robot.clickOn("#searchButton");


        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEqualTo(FXCollections.observableList(List.of(candy)));
    }

    @Test
    void searchCandies_whenSearchingByLollipopAndSuccessfully_showsCandiesData(FxRobot robot) {
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Lollipop,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandies(any(), any(), any()))
                .thenReturn(List.of(candy));


        robot.interact(() -> controller.getTypeBox().setValue(CandyType.Lollipop.toString()));
        robot.clickOn("#searchButton");


        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEqualTo(FXCollections.observableList(List.of(candy)));
    }

    @Test
    void searchCandies_whenSearchingByToffeeAndSuccessfully_showsCandiesData(FxRobot robot) {
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Toffee,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandies(any(), any(), any()))
                .thenReturn(List.of(candy));


        robot.interact(() -> controller.getTypeBox().setValue(CandyType.Toffee.toString()));
        robot.clickOn("#searchButton");


        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEqualTo(FXCollections.observableList(List.of(candy)));
    }

    @Test
    void searchCandies_whenSearchingByFudgeAndSuccessfully_showsCandiesData(FxRobot robot) {
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Fudge,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandies(any(), any(), any()))
                .thenReturn(List.of(candy));


        robot.interact(() -> controller.getTypeBox().setValue(CandyType.Fudge.toString()));
        robot.clickOn("#searchButton");


        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEqualTo(FXCollections.observableList(List.of(candy)));
    }

    @Test
    void searchCandies_whenSearchingByWaferAndSuccessfully_showsCandiesData(FxRobot robot) {
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Wafer,
                1,
                300,
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandies(any(), any(), any()))
                .thenReturn(List.of(candy));


        robot.interact(() -> controller.getTypeBox().setValue(CandyType.Wafer.toString()));
        robot.clickOn("#searchButton");


        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEqualTo(FXCollections.observableList(List.of(candy)));
    }

    @Test
    void searchCandies_whenSearchingByCriteriaAndSugarContentParsingFailed_showsMessage(FxRobot robot) {
        doNothing().when(exceptionHandler)
                        .handleBusinessException(any(), any());


        robot.interact(() -> controller.getMinSugarContentField().setText("error"));
        robot.clickOn("#searchButton");


        verify(exceptionHandler)
                .handleBusinessException(any(), any());
    }

    @Test
    void clearAllFields_whenSuccessfully_setCriteriaFieldsToDefault(FxRobot robot) {

        robot.clickOn("#searchButton");

        robot.interact(() -> {
            controller.getNameField().setText("name");
            controller.getTrademarkField().setText("trademark");
            controller.getTypeBox().setValue("Toffee");
            controller.getMinSugarContentField().setText("200");
            controller.getMaxSugarContentField().setText("10.0");
            controller.getSortBox().setValue("Sugar");
            controller.getOrderBox().setValue("Descending");
        });

        robot.clickOn("#clearCriteriaFieldsButton");


        Assertions.assertThat(controller.getNameField()).hasText("");
        Assertions.assertThat(controller.getTrademarkField()).hasText("");
        assertTrue(controller.getTypeBox().getValue().contains("Not specified"));
        Assertions.assertThat(controller.getMinSugarContentField()).hasText("");
        Assertions.assertThat(controller.getMaxSugarContentField()).hasText("");
        assertTrue(controller.getSortBox().getValue().contains("Id"));
        assertTrue(controller.getOrderBox().getValue().contains("Ascending"));
        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
    }
}