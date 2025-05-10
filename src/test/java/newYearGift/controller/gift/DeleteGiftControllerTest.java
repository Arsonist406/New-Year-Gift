package newYearGift.controller.gift;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import newYearGift.NewYearGiftApplication;
import newYearGift.exception.BusinessException;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Candy;
import newYearGift.model.Gift;
import newYearGift.service.GiftService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class DeleteGiftControllerTest {

    private GiftService giftService;
    private ExceptionHandler exceptionHandler;
    private DeleteGiftController controller;

    @Start
    void start(Stage stage) throws Exception {
        giftService = mock(GiftService.class);
        exceptionHandler = mock(ExceptionHandler.class);
        controller = new DeleteGiftController(giftService, exceptionHandler);

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/gift/deleteGift.fxml"));
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
    void initialize_whenSuccessfully_setTableCells() {
        Assertions.assertThat(controller.getIdColumn().getText())
                .isEqualTo("Id");
        Assertions.assertThat(controller.getNameColumn().getText())
                .isEqualTo("Name");
        Assertions.assertThat(controller.getTrademarkColumn().getText())
                .isEqualTo("Trademark");
        Assertions.assertThat(controller.getSugarColumn().getText())
                .isEqualTo("Sugar");
    }

    @Test
    void findGift_whenFound_showsGiftDataAndEnableDeleteViewElements(FxRobot robot) {
        Long id = 1L;
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(mock(Candy.class), 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(giftService.getGiftById(id))
                .thenReturn(gift);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findButton");


        Assertions.assertThat(controller.getFindNotificationLabel()).hasText("");

        Assertions.assertThat(controller.getNameLabel()).hasText("name");
        Assertions.assertThat(controller.getDescriptionLabel()).hasText("description");
        Assertions.assertThat(controller.getAuthorLabel()).hasText("author");
        Assertions.assertThat(controller.getCandyTable().getItems().size()).isEqualTo(1);

        Assertions.assertThat(controller.getDeleteButton()).isEnabled();
        Assertions.assertThat(controller.getDeleteMessageLabel()).isEnabled();
    }

    @Test
    void findGift_whenBusinessExceptionOccurs_clearsGiftDataFieldsAndEnableDeleteViewElements(FxRobot robot) {
        Long id = 1L;
        when(giftService.getGiftById(id))
                .thenThrow(new BusinessException("test"));
        doNothing().when(exceptionHandler)
                        .handleBusinessException(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findButton");


        Assertions.assertThat(controller.getFindNotificationLabel()).hasText("");

        Assertions.assertThat(controller.getNameLabel()).hasText("-");
        Assertions.assertThat(controller.getDescriptionLabel()).hasText("-");
        Assertions.assertThat(controller.getAuthorLabel()).hasText("-");
        Assertions.assertThat(controller.getCandyTable().getItems()).isEmpty();

        Assertions.assertThat(controller.getDeleteButton()).isDisabled();
        Assertions.assertThat(controller.getDeleteMessageLabel()).isDisabled();
    }

    @Test
    void deleteGift_whenDeleted_showsMessageAndClearsGiftDataFieldsAndDisableDeleteViewElements(FxRobot robot) {
        Long id = 1L;
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(mock(Candy.class), 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(giftService.getGiftById(id))
                .thenReturn(gift);
        doNothing().when(giftService)
                .deleteGift(id);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findButton");
        robot.clickOn("#deleteButton");


        Assertions.assertThat(controller.getDeleteNotificationLabel()).hasText("Gift was deleted");

        Assertions.assertThat(controller.getNameLabel()).hasText("-");
        Assertions.assertThat(controller.getDescriptionLabel()).hasText("-");
        Assertions.assertThat(controller.getAuthorLabel()).hasText("-");
        Assertions.assertThat(controller.getCandyTable().getItems()).isEmpty();

        Assertions.assertThat(controller.getDeleteButton()).isDisabled();
        Assertions.assertThat(controller.getDeleteMessageLabel()).isDisabled();
    }
}