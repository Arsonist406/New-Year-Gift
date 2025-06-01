package newYearGift.controller.candy;

import jakarta.validation.ConstraintViolationException;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class CreateCandyControllerTest {

    private CandyService candyService;
    private ExceptionHandler exceptionHandler;
    private CreateCandyController controller;

    @Start
    void start(Stage stage) throws Exception {
        candyService = mock(CandyService.class);
        exceptionHandler = mock(ExceptionHandler.class);
        controller = new CreateCandyController(candyService, exceptionHandler);

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/candy/createCandy.fxml"));
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
    void initialize_whenSuccessfully_setTypeBox() {
        Assertions.assertThat(controller.getTypeBox().getItems())
                .containsExactly(
                        CandyType.Chocolate,
                        CandyType.Caramel,
                        CandyType.Gummies,
                        CandyType.Lollipop,
                        CandyType.Toffee,
                        CandyType.Fudge,
                        CandyType.Wafer);
        
        Assertions.assertThat(controller.getTypeBox().getValue())
                .isEqualTo(CandyType.Chocolate);
    }

    @Test
    void initialize_whenSuccessfully_setListenersForSliders(FxRobot robot) {
        robot.interact(() -> controller.getSugarSlider().setValue(25.5));
        assertTrue(controller.getSugarField().getText().contains("25.50"));
        
        robot.interact(() -> controller.getFatsSlider().setValue(10.25));
        assertTrue(controller.getFatsField().getText().contains("10.25"));
        
        robot.interact(() -> controller.getProteinsSlider().setValue(7.75));
        assertTrue(controller.getProteinsField().getText().contains("7.75"));
        
        robot.interact(() -> controller.getCarbohydratesSlider().setValue(15.0));
        assertTrue(controller.getCarbohydratesField().getText().contains("15.0"));
    }

    @Test
    void createCandy_whenSuccessfully_showsNewCandyId(FxRobot robot) {
        Candy candy = spy(new Candy(
                1L,
                "name",
                "trademark",
                CandyType.Chocolate,
                100,
                200,
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(3.0),
                BigDecimal.valueOf(15.0)));
        when(candyService.createCandy(any()))
                .thenReturn(candy);

        robot.interact(() -> {
            controller.getNameField().setText("name");
            controller.getTrademarkField().setText("trademark");
            controller.getTypeBox().setValue(CandyType.Chocolate);
            controller.getWeightField().setText("100");
            controller.getCaloriesField().setText("200");
            controller.getSugarField().setText("10.0");
            controller.getFatsField().setText("5.0");
            controller.getProteinsField().setText("3.0");
            controller.getCarbohydratesField().setText("15.0");
        });


        robot.clickOn("#createButton");


        assertTrue(controller.getNotificationLabel().getText().contains("New candy has been created"));
    }

    @Test
    void createCandy_whenConstraintViolationExceptionOccurs_showsMessage(FxRobot robot) {
        ConstraintViolationException ex = new ConstraintViolationException("test", null);
        when(candyService.createCandy(any()))
                .thenThrow(ex);
        doNothing().when(exceptionHandler)
                .handleConstraintViolationException(ex, controller.getNotificationLabel());


        robot.clickOn("#createButton");


        verify(candyService)
                .createCandy(any());
        verify(exceptionHandler)
                .handleConstraintViolationException(ex, controller.getNotificationLabel());
    }

    @Test
    void createCandy_whenBusinessExceptionOccurs_showsMessage(FxRobot robot) {
        BusinessException ex = new BusinessException("test");
        when(candyService.createCandy(any()))
                .thenThrow(ex);
        doNothing().when(exceptionHandler)
                .handleBusinessException(ex, controller.getNotificationLabel());


        robot.clickOn("#createButton");


        verify(candyService)
                .createCandy(any());
        verify(exceptionHandler)
                .handleBusinessException(ex, controller.getNotificationLabel());
    }

    @Test
    void clearAllFields_whenSuccessfully_setDataFieldsToDefault(FxRobot robot) {
        robot.interact(() -> {
            controller.getNameField().setText("name");
            controller.getTrademarkField().setText("trademark");
            controller.getWeightField().setText("100");
            controller.getCaloriesField().setText("200");
            controller.getSugarField().setText("10.0");
            controller.getNotificationLabel().setText("test");
        });


        robot.clickOn("#clearDataFieldsButton");


        Assertions.assertThat(controller.getNameField()).hasText("");
        Assertions.assertThat(controller.getTrademarkField()).hasText("");
        Assertions.assertThat(controller.getWeightField()).hasText("1");
        Assertions.assertThat(controller.getCaloriesField()).hasText("0");
        Assertions.assertThat(controller.getSugarField()).hasText("0");
        Assertions.assertThat(controller.getFatsField()).hasText("0");
        Assertions.assertThat(controller.getProteinsField()).hasText("0");
        Assertions.assertThat(controller.getCarbohydratesField()).hasText("0");
        Assertions.assertThat(controller.getNotificationLabel()).hasText("");
    }
}