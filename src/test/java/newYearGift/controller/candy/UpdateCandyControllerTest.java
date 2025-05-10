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
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class UpdateCandyControllerTest {

    private CandyService candyService;
    private ExceptionHandler exceptionHandler;
    private UpdateCandyController controller;

    @Start
    void start(Stage stage) throws Exception {
        candyService = mock(CandyService.class);
        exceptionHandler = mock(ExceptionHandler.class);
        controller = new UpdateCandyController(candyService, exceptionHandler);

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/candy/updateCandy.fxml"));
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
    void findCandy_whenFound_showsCandyDataAndEnableViewElements(FxRobot robot) {
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

        Assertions.assertThat(controller.getNameField()).hasText("name");
        Assertions.assertThat(controller.getTrademarkField()).hasText("trademark");
        Assertions.assertThat(controller.getWeightField()).hasText("100");
        Assertions.assertThat(controller.getCaloriesField()).hasText("200");
        Assertions.assertThat(controller.getSugarField()).hasText("10.0");
        Assertions.assertThat(controller.getFatsField()).hasText("5.0");
        Assertions.assertThat(controller.getProteinsField()).hasText("3.0");
        Assertions.assertThat(controller.getCarbohydratesField()).hasText("15.0");

        Assertions.assertThat(controller.getNameField()).isEnabled();
        Assertions.assertThat(controller.getTrademarkField()).isEnabled();
        Assertions.assertThat(controller.getWeightField()).isEnabled();
        Assertions.assertThat(controller.getCaloriesField()).isEnabled();
        Assertions.assertThat(controller.getSugarField()).isEnabled();
        Assertions.assertThat(controller.getFatsField()).isEnabled();
        Assertions.assertThat(controller.getProteinsField()).isEnabled();
        Assertions.assertThat(controller.getCarbohydratesField()).isEnabled();

        Assertions.assertThat(controller.getSugarSlider()).isEnabled();
        Assertions.assertThat(controller.getFatsSlider()).isEnabled();
        Assertions.assertThat(controller.getProteinsSlider()).isEnabled();
        Assertions.assertThat(controller.getCarbohydratesSlider()).isEnabled();

        Assertions.assertThat(controller.getUpdateButton()).isEnabled();
        Assertions.assertThat(controller.getClearDataFieldsButton()).isEnabled();
    }

    @Test
    void findCandy_whenBusinessExceptionOccurs_showsMessageAndClearDataFieldsAndDisableViewElements(FxRobot robot) {
        BusinessException ex = new BusinessException("test");
        when(candyService.getCandyById(any()))
                .thenThrow(ex);
        doNothing().when(exceptionHandler)
                .handleBusinessException(ex, controller.getFindNotificationLabel());


        robot.interact(() -> controller.getIdField().setText("1L"));
        robot.clickOn("#findButton");


        verify(exceptionHandler)
                .handleBusinessException(any(), any());

        Assertions.assertThat(controller.getNameField()).hasText("");
        Assertions.assertThat(controller.getTrademarkField()).hasText("");
        Assertions.assertThat(controller.getWeightField()).hasText("1");
        Assertions.assertThat(controller.getCaloriesField()).hasText("0");
        Assertions.assertThat(controller.getSugarField()).hasText("0");
        Assertions.assertThat(controller.getFatsField()).hasText("0");
        Assertions.assertThat(controller.getProteinsField()).hasText("0");
        Assertions.assertThat(controller.getCarbohydratesField()).hasText("0");

        Assertions.assertThat(controller.getNameField()).isDisabled();
        Assertions.assertThat(controller.getTrademarkField()).isDisabled();
        Assertions.assertThat(controller.getWeightField()).isDisabled();
        Assertions.assertThat(controller.getCaloriesField()).isDisabled();
        Assertions.assertThat(controller.getSugarField()).isDisabled();
        Assertions.assertThat(controller.getFatsField()).isDisabled();
        Assertions.assertThat(controller.getProteinsField()).isDisabled();
        Assertions.assertThat(controller.getCarbohydratesField()).isDisabled();

        Assertions.assertThat(controller.getSugarSlider()).isDisabled();
        Assertions.assertThat(controller.getFatsSlider()).isDisabled();
        Assertions.assertThat(controller.getProteinsSlider()).isDisabled();
        Assertions.assertThat(controller.getCarbohydratesSlider()).isDisabled();

        Assertions.assertThat(controller.getUpdateButton()).isDisabled();
        Assertions.assertThat(controller.getClearDataFieldsButton()).isDisabled();
    }

    @Test
    void updateCandy_whenSuccessfully_showsMessage(FxRobot robot) {
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
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        doNothing().when(candyService)
                .updateCandy(eq(id), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findButton");

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

        robot.clickOn("#updateButton");


        assertTrue(controller.getUpdateNotificationLabel().getText().contains("Candy data was update"));
    }

    @Test
    void updateCandy_whenConstraintViolationExceptionOccurs_showsMessage(FxRobot robot) {
        ConstraintViolationException ex = new ConstraintViolationException("test", null);
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
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        doThrow(ex).when(candyService)
                .updateCandy(any(), any());
        doNothing().when(exceptionHandler)
                .handleConstraintViolationException(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findButton");
        robot.clickOn("#updateButton");


        verify(candyService)
                .updateCandy(any(), any());
        verify(exceptionHandler)
                .handleConstraintViolationException(any(), any());
    }

    @Test
    void updateCandy_whenBusinessExceptionOccurs_showsMessage(FxRobot robot) {
        BusinessException ex = new BusinessException("test");
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
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        doThrow(ex).when(candyService)
                .updateCandy(any(), any());
        doNothing().when(exceptionHandler)
                .handleConstraintViolationException(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findButton");
        robot.clickOn("#updateButton");


        verify(candyService)
                .updateCandy(any(), any());
        verify(exceptionHandler)
                .handleBusinessException(any(), any());
    }

    @Test
    void clearAllFields_whenSuccessfully_setAllFieldsToDefault(FxRobot robot) {
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
        robot.clickOn("#clearDataFieldsButton");


        Assertions.assertThat(controller.getNameField()).hasText("");
        Assertions.assertThat(controller.getTrademarkField()).hasText("");
        Assertions.assertThat(controller.getWeightField()).hasText("1");
        Assertions.assertThat(controller.getCaloriesField()).hasText("0");
        Assertions.assertThat(controller.getSugarField()).hasText("0");
        Assertions.assertThat(controller.getFatsField()).hasText("0");
        Assertions.assertThat(controller.getProteinsField()).hasText("0");
        Assertions.assertThat(controller.getCarbohydratesField()).hasText("0");
        Assertions.assertThat(controller.getUpdateNotificationLabel()).hasText("");
    }
}