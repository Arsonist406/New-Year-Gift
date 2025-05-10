package newYearGift.controller.gift;

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
import newYearGift.model.Gift;
import newYearGift.service.CandyService;
import newYearGift.service.GiftService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class UpdateGiftControllerTest {

    private GiftService giftService;
    private CandyService candyService;
    private ExceptionHandler exceptionHandler;
    private UpdateGiftController controller;

    @Start
    void start(Stage stage) throws Exception {
        giftService = mock(GiftService.class);
        candyService = mock(CandyService.class);
        exceptionHandler = mock(ExceptionHandler.class);
        controller = new UpdateGiftController(giftService, candyService, exceptionHandler);

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/gift/updateGift.fxml"));
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
        assertTrue(controller.getCandyIdWeightMap().isEmpty());

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
    void findGift_whenFound_showsGiftDataAndEnableViewElements(FxRobot robot) {
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
        robot.clickOn("#findGiftButton");

        Assertions.assertThat(controller.getGiftNameField())
                .isEnabled();
        Assertions.assertThat(controller.getGiftDescriptionArea())
                .isEnabled();
        Assertions.assertThat(controller.getGiftAuthorField())
                .isEnabled();
        Assertions.assertThat(controller.getCandyTable())
                .isEnabled();

        Assertions.assertThat(controller.getUpdateGiftButton())
                .isEnabled();
        Assertions.assertThat(controller.getClearDataFieldsButton())
                .isEnabled();

        Assertions.assertThat(controller.getCandyIdToFoundField())
                .isEnabled();
        Assertions.assertThat(controller.getFindCandyButton())
                .isEnabled();

        Assertions.assertThat(controller.getGiftNameField())
                .hasText("name");
        Assertions.assertThat(controller.getGiftDescriptionArea())
                .hasText("description");
        Assertions.assertThat(controller.getGiftAuthorField())
                .hasText("author");
        Assertions.assertThat(controller.getCandyTable().getItems().size())
                .isEqualTo(1);

        Assertions.assertThat(controller.getCandyIdWeightMap().size())
                .isEqualTo(1);

        Assertions.assertThat(controller.getCandyIdToUpdateField())
                .isEnabled();
        Assertions.assertThat(controller.getNewWeightOfCandyField())
                .isEnabled();
        Assertions.assertThat(controller.getUpdateCandyWeightButton())
                .isEnabled();
        Assertions.assertThat(controller.getCandyIdToDeleteField())
                .isEnabled();
        Assertions.assertThat(controller.getDeleteCandyButton())
                .isEnabled();
    }

    @Test
    void findGift_whenBusinessExceptionOccurs_showMessageAndClearsDataFieldsAndDisableViewElementsAndElse$_$(FxRobot robot) {
        Long normId = 1L;
        Long errorId = 2L;
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(mock(Candy.class), 100);
        Gift gift = new Gift(
                normId,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(giftService.getGiftById(normId))
                .thenReturn(gift);
        when(giftService.getGiftById(errorId))
                .thenThrow(new BusinessException("test"));


        robot.interact(() -> controller.getIdField().setText(normId.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getIdField().setText(errorId.toString()));
        robot.clickOn("#findGiftButton");


        Assertions.assertThat(controller.getGiftNameField())
                .isDisabled();
        Assertions.assertThat(controller.getGiftDescriptionArea())
                .isDisabled();
        Assertions.assertThat(controller.getGiftAuthorField())
                .isDisabled();
        Assertions.assertThat(controller.getCandyTable())
                .isDisabled();

        Assertions.assertThat(controller.getUpdateGiftButton())
                .isDisabled();
        Assertions.assertThat(controller.getClearDataFieldsButton())
                .isDisabled();

        Assertions.assertThat(controller.getCandyIdToFoundField())
                .isDisabled();
        Assertions.assertThat(controller.getFindCandyButton())
                .isDisabled();

        Assertions.assertThat(controller.getGiftNameField())
                .hasText("");
        Assertions.assertThat(controller.getGiftDescriptionArea())
                .hasText("");
        Assertions.assertThat(controller.getGiftAuthorField())
                .hasText("");
        Assertions.assertThat(controller.getCandyTable().getItems().size())
                .isEqualTo(0);

        Assertions.assertThat(controller.getCandyIdWeightMap().size())
                .isEqualTo(0);

        Assertions.assertThat(controller.getCandyIdToUpdateField())
                .isDisabled();
        Assertions.assertThat(controller.getNewWeightOfCandyField())
                .isDisabled();
        Assertions.assertThat(controller.getUpdateCandyWeightButton())
                .isDisabled();
        Assertions.assertThat(controller.getCandyIdToDeleteField())
                .isDisabled();
        Assertions.assertThat(controller.getDeleteCandyButton())
                .isDisabled();
    }

    @Test
    void updateCandyWeight_whenSuccessfully_changeCandyWeight(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        when(giftService.getGiftById(id))
                .thenReturn(gift);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");
        robot.interact(() -> controller.getCandyIdToUpdateField().setText(id.toString()));
        robot.interact(() -> controller.getNewWeightOfCandyField().setText("1"));
        robot.clickOn("#updateCandyWeightButton");


        assertEquals(1, controller.getCandyIdWeightMap().get(id));
        Assertions.assertThat(controller.getCandyIdToUpdateField())
                .hasText("");
        Assertions.assertThat(controller.getNewWeightOfCandyField())
                .hasText("");
        Assertions.assertThat(controller.getUpdateAndDeleteCandyNotificationLabel())
                .hasText("");
    }

    @Test
    void updateCandyWeight_whenBusinessExceptionOccurs_showMessage(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        when(giftService.getGiftById(id))
                .thenReturn(gift);
        doNothing().when(exceptionHandler)
                .handleBusinessException(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");
        robot.interact(() -> controller.getCandyIdToUpdateField().setText(id.toString()));
        robot.interact(() -> controller.getNewWeightOfCandyField().setText("-5"));
        robot.clickOn("#updateCandyWeightButton");


        verify(exceptionHandler)
                .handleBusinessException(any(), any());
    }

    @Test
    void deleteCandyFromTable_whenSuccessfully_deleteCandy(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        when(giftService.getGiftById(id))
                .thenReturn(gift);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");
        robot.interact(() -> controller.getCandyIdToDeleteField().setText(id.toString()));
        robot.clickOn("#deleteCandyButton");


        assertFalse(controller.getCandyIdWeightMap().containsKey(id));
        Assertions.assertThat(controller.getCandyTable().getItems())
                .isEmpty();
        Assertions.assertThat(controller.getCandyIdToDeleteField())
                .hasText("");
        Assertions.assertThat(controller.getUpdateAndDeleteCandyNotificationLabel())
                .hasText("");
    }

    @Test
    void deleteCandy_whenBusinessExceptionOccurs_showsMessage(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        when(giftService.getGiftById(id))
                .thenReturn(gift);
        doNothing().when(exceptionHandler)
                .handleConstraintViolationException(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");
        robot.interact(() -> controller.getCandyIdToDeleteField().setText("error"));
        robot.clickOn("#deleteCandyButton");


        verify(exceptionHandler)
                .handleBusinessException(any(), any());
    }

    @Test
    void findCandy_whenFound_showsCandyDataAndEnableAddCandyElements(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        when(giftService.getGiftById(id))
                .thenReturn(gift);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");


        Assertions.assertThat(controller.getFindCandyNotificationLabel()).hasText("");

        Assertions.assertThat(controller.getCandyNameLabel()).hasText("name");
        Assertions.assertThat(controller.getCandyTrademarkLabel()).hasText("trademark");
        Assertions.assertThat(controller.getCandyWeightLabel()).hasText("100");
        Assertions.assertThat(controller.getCandyCaloriesLabel()).hasText("200");
        Assertions.assertThat(controller.getCandySugarLabel()).hasText("10.0");
        Assertions.assertThat(controller.getCandyFatsLabel()).hasText("5.0");
        Assertions.assertThat(controller.getCandyProteinsLabel()).hasText("3.0");
        Assertions.assertThat(controller.getCandyCarbohydratesLabel()).hasText("15.0");

        Assertions.assertThat(controller.getWeightOfCandyInGiftMessageLabel()).isEnabled();
        Assertions.assertThat(controller.getWeightOfCandyInGiftField()).isEnabled();
        Assertions.assertThat(controller.getAddCandyButton()).isEnabled();
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                workingId,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(workingId))
                .thenReturn(candy);
        when(giftService.getGiftById(workingId))
                .thenReturn(gift);
        when(candyService.getCandyById(exceptionTrowingId))
                .thenThrow(ex);
        doNothing().when(exceptionHandler)
                .handleBusinessException(ex, controller.getFindCandyNotificationLabel());


        robot.interact(() -> controller.getIdField().setText(workingId.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(workingId.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(exceptionTrowingId.toString()));
        robot.clickOn("#findCandyButton");


        Assertions.assertThat(controller.getCandyNameLabel()).hasText("-");
        Assertions.assertThat(controller.getCandyTrademarkLabel()).hasText("-");
        Assertions.assertThat(controller.getCandyWeightLabel()).hasText("-");
        Assertions.assertThat(controller.getCandyCaloriesLabel()).hasText("-");
        Assertions.assertThat(controller.getCandySugarLabel()).hasText("-");
        Assertions.assertThat(controller.getCandyFatsLabel()).hasText("-");
        Assertions.assertThat(controller.getCandyProteinsLabel()).hasText("-");
        Assertions.assertThat(controller.getCandyCarbohydratesLabel()).hasText("-");

        Assertions.assertThat(controller.getWeightOfCandyInGiftMessageLabel()).isDisabled();
        Assertions.assertThat(controller.getWeightOfCandyInGiftField()).isDisabled();
        Assertions.assertThat(controller.getAddCandyButton()).isDisabled();
    }

    @Test
    void addCandy_whenAddedNewCandy_addCandyToTheTableAndEnableDeleteCandyViewElements(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        when(giftService.getGiftById(id))
                .thenReturn(gift);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");


        assertTrue(controller.getCandyIdWeightMap().containsKey(id));
        assertTrue(controller.getCandyIdWeightMap().containsValue(100));
        assertEquals(1, controller.getCandyTable().getItems().size());
        Assertions.assertThat(controller.getDeleteCandyButton()).isEnabled();
    }

    @Test
    void addCandy_whenAddedCandyThatInTable_replaceCandyWeightInTheTableAndEnableDeleteCandyViewElements(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        when(giftService.getGiftById(id))
                .thenReturn(gift);


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("1"));
        robot.clickOn("#addCandyButton");


        assertTrue(controller.getCandyIdWeightMap().containsKey(id));
        assertTrue(controller.getCandyIdWeightMap().containsValue(1));
        assertEquals(1, controller.getCandyTable().getItems().size());
        Assertions.assertThat(controller.getDeleteCandyButton()).isEnabled();
    }

    @Test
    void addCandy_whenBusinessExceptionOccurs_showsMessage(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
        Gift gift = new Gift(
                id,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        when(giftService.getGiftById(id))
                .thenReturn(gift);
        doNothing().when(exceptionHandler)
                .handleBusinessException(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("-5"));
        robot.clickOn("#addCandyButton");


        verify(exceptionHandler)
                .handleBusinessException(any(), any());
    }

    @Test
    void updateGift_whenSuccessfully_showsMessage(FxRobot robot) {
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
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(candy, 100);
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
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        doNothing().when(giftService)
                .updateGift(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");
        robot.interact(() -> {
            controller.getGiftNameField().setText("name");
            controller.getGiftDescriptionArea().setText("description");
            controller.getGiftAuthorField().setText("author");
        });
        robot.clickOn("#updateGiftButton");


        Assertions.assertThat(controller.getUpdateGiftNotificationLabel())
                .hasText("Gift data was update");
    }

    @Test
    void updateGift_whenConstraintViolationExceptionOccurs_showsMessage(FxRobot robot) {
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
        Gift gift = spy(new Gift());
        gift.setId(1L);
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        doThrow(new ConstraintViolationException("test", null))
                .when(giftService)
                .updateGift(any(), any());
        doNothing().when(exceptionHandler)
                .handleConstraintViolationException(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");
        robot.interact(() -> {
            controller.getGiftNameField().setText("name");
            controller.getGiftDescriptionArea().setText("description");
            controller.getGiftAuthorField().setText("author");
        });
        robot.clickOn("#updateGiftButton");


        verify(exceptionHandler)
                .handleConstraintViolationException(any(), any());
    }

    @Test
    void updateGift_whenBusinessExceptionExceptionOccurs_showsMessage(FxRobot robot) {
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
        Gift gift = spy(new Gift());
        gift.setId(1L);
        when(candyService.getCandyById(id))
                .thenReturn(candy);
        doThrow(new BusinessException("test"))
                .when(giftService)
                .updateGift(any(), any());
        doNothing().when(exceptionHandler)
                .handleBusinessException(any(), any());


        robot.interact(() -> controller.getIdField().setText(id.toString()));
        robot.clickOn("#findGiftButton");
        robot.interact(() -> controller.getCandyIdToFoundField().setText(id.toString()));
        robot.clickOn("#findCandyButton");
        robot.interact(() -> controller.getWeightOfCandyInGiftField().setText("100"));
        robot.clickOn("#addCandyButton");
        robot.interact(() -> {
            controller.getGiftNameField().setText("name");
            controller.getGiftDescriptionArea().setText("description");
            controller.getGiftAuthorField().setText("author");
        });
        robot.clickOn("#updateGiftButton");


        verify(exceptionHandler)
                .handleBusinessException(any(), any());
    }
}