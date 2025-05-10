package newYearGift.controller.candy;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import newYearGift.NewYearGiftApplication;
import newYearGift.exception.BusinessException;
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

import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class DeleteCandyControllerTest {

    private CandyService candyService;
    private ExceptionHandler exceptionHandler;
    private DeleteCandyController controller;

    @Start
    void start(Stage stage) throws Exception {
        candyService = mock(CandyService.class);
        exceptionHandler = mock(ExceptionHandler.class);
        controller = new DeleteCandyController(candyService, exceptionHandler);

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/candy/deleteCandy.fxml"));
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
    void findCandy_whenFound_showsCandyDataAndEnableDeleteViewElements(FxRobot robot) {
        Long id = 1L;
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Fudge,
                100,
                200,
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandyById(id))
                .thenReturn(candy);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findButton");


        Assertions.assertThat(controller.getFindNotificationLabel()).hasText("");

        Assertions.assertThat(controller.getNameLabel()).hasText("name");
        Assertions.assertThat(controller.getTrademarkLabel()).hasText("trademark");
        Assertions.assertThat(controller.getTypeLabel()).hasText("Fudge");
        Assertions.assertThat(controller.getWeightLabel()).hasText("100");
        Assertions.assertThat(controller.getCaloriesLabel()).hasText("200");
        Assertions.assertThat(controller.getSugarLabel()).hasText("10.0");
        Assertions.assertThat(controller.getFatsLabel()).hasText("5.0");
        Assertions.assertThat(controller.getProteinsLabel()).hasText("3.0");
        Assertions.assertThat(controller.getCarbohydratesLabel()).hasText("15.0");

        Assertions.assertThat(controller.getDeleteButton()).isEnabled();
        Assertions.assertThat(controller.getDeleteMessageLabel()).isEnabled();
    }


    @Test
    void findCandy_whenBusinessExceptionOccurs_clearsCandyDataFieldsAndDisableDeleteViewElements(FxRobot robot) {
        Long workingId = 1L;
        Long exceptionTrowingId = 2L;
        BusinessException ex = new BusinessException("test");
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Fudge,
                100,
                200,
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandyById(workingId))
                .thenReturn(candy);
        when(candyService.getCandyById(exceptionTrowingId))
                .thenThrow(ex);
        doNothing().when(exceptionHandler)
                .handleBusinessException(ex, controller.getFindNotificationLabel());


        robot.interact(() -> controller.getIdField().setText(workingId.toString()));
        robot.clickOn("#findButton");
        robot.interact(() -> controller.getIdField().setText(exceptionTrowingId.toString()));
        robot.clickOn("#findButton");


        Assertions.assertThat(controller.getNameLabel()).hasText("-");
        Assertions.assertThat(controller.getTrademarkLabel()).hasText("-");
        Assertions.assertThat(controller.getTypeLabel()).hasText("-");
        Assertions.assertThat(controller.getWeightLabel()).hasText("-");
        Assertions.assertThat(controller.getCaloriesLabel()).hasText("-");
        Assertions.assertThat(controller.getSugarLabel()).hasText("-");
        Assertions.assertThat(controller.getFatsLabel()).hasText("-");
        Assertions.assertThat(controller.getProteinsLabel()).hasText("-");
        Assertions.assertThat(controller.getCarbohydratesLabel()).hasText("-");

        Assertions.assertThat(controller.getDeleteButton()).isDisabled();
        Assertions.assertThat(controller.getDeleteMessageLabel()).isDisabled();
    }

    @Test
    void deleteCandy_whenDeleted_showsMessageAndClearsCandyDataFieldsAndDisableDeleteViewElements(FxRobot robot) {
        Long id = 1L;
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Fudge,
                100,
                200,
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        doNothing().when(candyService)
                .deleteCandy(id);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findButton");
        robot.clickOn("#deleteButton");


        Assertions.assertThat(controller.getDeleteNotificationLabel()).hasText("Candy was deleted");

        Assertions.assertThat(controller.getNameLabel()).hasText("-");
        Assertions.assertThat(controller.getTrademarkLabel()).hasText("-");
        Assertions.assertThat(controller.getTypeLabel()).hasText("-");
        Assertions.assertThat(controller.getWeightLabel()).hasText("-");
        Assertions.assertThat(controller.getCaloriesLabel()).hasText("-");
        Assertions.assertThat(controller.getSugarLabel()).hasText("-");
        Assertions.assertThat(controller.getFatsLabel()).hasText("-");
        Assertions.assertThat(controller.getProteinsLabel()).hasText("-");
        Assertions.assertThat(controller.getCarbohydratesLabel()).hasText("-");

        Assertions.assertThat(controller.getDeleteButton()).isDisabled();
        Assertions.assertThat(controller.getDeleteMessageLabel()).isDisabled();
    }
}
