package newYearGift.controller.gift;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import newYearGift.NewYearGiftApplication;
import newYearGift.model.Candy;
import newYearGift.model.CandyType;
import newYearGift.model.Gift;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@ExtendWith(ApplicationExtension.class)
class GiftDetailsControllerTest {

    private GiftDetailsController controller;

    @Start
    void start(Stage stage) throws Exception {
        controller = new GiftDetailsController();

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/gift/giftDetails.fxml"));
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
    void initData_whenSuccessfully_setGiftData() throws Exception {
        Long id = 1L;
        Candy candy = spy(new Candy(
                id,
                "name",
                "trademark",
                CandyType.Chocolate,
                100,
                200,
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = spy(new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        ));


        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                controller.initialize(gift, mock(Scene.class));
            } finally {
                latch.countDown();
            }
        });
        latch.await();


        Assertions.assertThat(controller.getNameLabel()).hasText("name");
        Assertions.assertThat(controller.getDescriptionLabel()).hasText("description");
        Assertions.assertThat(controller.getAuthorLabel()).hasText("author");
        Assertions.assertThat(controller.getCreationDateLabel()).hasText("2024-01-01T12:00:00Z");
        Assertions.assertThat(controller.getCandyTable().getItems().size()).isEqualTo(1);

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

        Assertions.assertThat(controller.getTotalWeightLabel())
                .hasText("100");
    }

    @Test
    void applyCriteria_whenMinSugarIsInvalid_makeMinSugarRed(FxRobot robot) {

        robot.interact(() -> controller.getMinSugarContentField().setText("error"));
        robot.clickOn("#applyCriteriaButton");

        Assertions.assertThat(controller.getMinSugarContentField())
                .hasStyle("-fx-text-fill: red;");
    }

    @Test
    void applyCriteria_whenMaxSugarIsInvalid_makeMaxSugarRed(FxRobot robot) {

        robot.interact(() -> controller.getMaxSugarContentField().setText("error"));
        robot.clickOn("#applyCriteriaButton");

        Assertions.assertThat(controller.getMaxSugarContentField())
                .hasStyle("-fx-text-fill: red;");
    }
}
