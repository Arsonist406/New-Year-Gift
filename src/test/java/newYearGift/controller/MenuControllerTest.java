package newYearGift.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import newYearGift.NewYearGiftApplication;
import newYearGift.controller.other.MenuController;
import newYearGift.logger.LoggerContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
@SpringBootTest
class MenuControllerTest {

    private Logger logger;
    private MenuController controller;
    @Autowired
    private ConfigurableApplicationContext context;

    @Start
    public void start(Stage stage) throws Exception {
        LoggerContainer loggerContainer = spy(new LoggerContainer());
        logger = mock(Logger.class);
        when(loggerContainer.getLogger())
                .thenReturn(logger);
        controller = new MenuController(loggerContainer, context);

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/header.fxml"));
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
    void switchToCreateGiftScene_whenSuccessfully_openCreateGiftScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#giftMenu");
        robot.clickOn("#switchToCreateGiftScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "createGift");
    }

    @Test
    void switchToSearchGiftsScene_whenSuccessfully_openSearchGiftsScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#giftMenu");
        robot.clickOn("#switchToSearchGiftsScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "searchGifts");
    }

    @Test
    void switchToUpdateGiftScene_whenSuccessfully_openUpdateGiftScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#giftMenu");
        robot.clickOn("#switchToUpdateGiftScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "updateGift");
    }

    @Test
    void switchToDeleteGiftScene_whenSuccessfully_openDeleteGiftScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#giftMenu");
        robot.clickOn("#switchToDeleteGiftScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "deleteGift");
    }

    @Test
    void switchToCreateCandyScene_whenSuccessfully_openCreateCandyScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#candyMenu");
        robot.clickOn("#switchToCreateCandyScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "createCandy");
    }

    @Test
    void switchToSearchCandisScene_whenSuccessfully_openSearchCandisScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#candyMenu");
        robot.clickOn("#switchToSearchCandiesScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "searchCandies");
    }

    @Test
    void switchToUpdateCandyScene_whenSuccessfully_openUpdateCandyScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#candyMenu");
        robot.clickOn("#switchToUpdateCandyScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "updateCandy");
    }

    @Test
    void switchToDeleteCandyScene_whenSuccessfully_openDeleteCandyScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#candyMenu");
        robot.clickOn("#switchToDeleteCandyScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "deleteCandy");
    }

    @Test
    void switchToAboutScene_whenSuccessfully_openAboutScene(FxRobot robot) {
        doNothing().when(logger)
                .info(any());


        robot.clickOn("#helpMenu");
        robot.clickOn("#switchToAboutScene");


        verify(controller.getLogger())
                .info("★ Switch to \"{}\" scene", "about");
    }
}