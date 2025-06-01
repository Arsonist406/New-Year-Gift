package newYearGift.controller.gift;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import newYearGift.NewYearGiftApplication;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Candy;
import newYearGift.model.Gift;
import newYearGift.service.GiftService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
@SpringBootTest
class SearchGiftsControllerTest {

    private GiftService giftService;
    private ExceptionHandler exceptionHandler;
    private SearchGiftsController controller;
    @Autowired
    private ConfigurableApplicationContext context;

    private Gift gift1;

    @Start
    void start(Stage stage) throws Exception {
        giftService = mock(GiftService.class);
        gift1 = spy(new Gift(
                1L,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                new HashMap<>()
        ));
        Gift gift2 = spy(new Gift(
                2L,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                new HashMap<>()
        ));
        Gift gift3 = spy(new Gift(
                3L,
                "name",
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                new HashMap<>()
        ));
        doReturn(List.of(gift1, gift2, gift3))
                .when(giftService)
                .getGiftsByName(anyString());

        exceptionHandler = mock(ExceptionHandler.class);
        controller = new SearchGiftsController(giftService, exceptionHandler, context);

        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/gift/searchGifts.fxml"));
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
    void initialize_whenSuccessfully_showGifts() {
        Assertions.assertThat(controller.getGiftsGrid().getColumnConstraints().size())
                .isEqualTo(3);
        Assertions.assertThat(controller.getGiftsGrid().getRowConstraints().size())
                .isEqualTo(2);
    }

    @Test
    void findGiftByName_whenFound_showsGifts(FxRobot robot) {
        String name = "name";
        Map<Candy, Integer> candyWeightMap = new HashMap<>();
        candyWeightMap.put(mock(Candy.class), 100);
        Gift gift = new Gift(
                1L,
                name,
                "description",
                "author",
                Instant.parse("2024-01-01T12:00:00Z"),
                candyWeightMap
        );
        when(giftService.getGiftsByName(name))
                .thenReturn(List.of(gift));


        robot.interact(() -> controller.getNameField().setText(name));
        robot.clickOn("#findGiftByName");


        verify(giftService)
                .getGiftsByName(name);
        Assertions.assertThat(controller.getFindNotificationLabel())
                .hasText("");
    }

    @Test
    void findGiftByName_whenNotFound_showMessage(FxRobot robot) {
        String name = "name";
        when(giftService.getGiftsByName(name))
                .thenReturn(new ArrayList<>());


        robot.interact(() -> controller.getNameField().setText(name));
        robot.clickOn("#findGiftByName");


        verify(giftService)
                .getGiftsByName(name);
        Assertions.assertThat(controller.getFindNotificationLabel())
                .hasText("No gifts was found");
    }

    @Test
    void handleClickOnCard$loadGiftDetailsScene_whenSuccessfully_showsGiftDetails(FxRobot robot) {
        robot.clickOn("#giftCard1");

        Assertions.assertThat(controller.getGiftDetailsController().getGift())
                .isEqualTo(gift1);
    }
}
