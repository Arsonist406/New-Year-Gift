package newYearGift.controller.other;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import newYearGift.NewYearGiftApplication;
import newYearGift.logger.LoggerContainer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
@Getter
public class MenuController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Menu giftMenu;
    @FXML
    private MenuItem switchToCreateGiftScene;
    @FXML
    private MenuItem switchToSearchGiftsScene;
    @FXML
    private MenuItem switchToUpdateGiftScene;
    @FXML
    private MenuItem switchToDeleteGiftScene;

    @FXML
    private Menu candyMenu;
    @FXML
    private MenuItem switchToCreateCandyScene;
    @FXML
    private MenuItem switchToSearchCandiesScene;
    @FXML
    private MenuItem switchToUpdateCandyScene;
    @FXML
    private MenuItem switchToDeleteCandyScene;

    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem switchToAboutScene;
    @FXML
    private MenuItem switchToCheckErrorMessageSendingOnMailScene;

    private final Logger logger;
    private final ConfigurableApplicationContext context;

    @Autowired
    public MenuController(
            LoggerContainer loggerContainer,
            ConfigurableApplicationContext context
    ) {
        this.logger = loggerContainer.getLogger();
        this.context = context;
    }

    @FXML
    private void switchScene(
            String path
    ) throws IOException {
        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource(path));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Stage stage = (Stage) rootPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();

        logger.info("★ Switch to \"{}\" scene", getSceneName(path));
    }

    private String getSceneName(
            String path
    ) {
        int lastSlashIndex = path.lastIndexOf('/');
        int fxmlIndex = path.lastIndexOf(".fxml");
        return path.substring(lastSlashIndex + 1, fxmlIndex);
    }

    /****************Switch to gifts scenes*******************/

    @FXML
    private void switchToCreateGiftScene() throws IOException {
        switchScene("../view/gift/createGift.fxml");
    }

    @FXML
    private void switchToUsersGiftsScene() throws IOException {
        switchScene("../view/gift/searchGifts.fxml");
    }

    @FXML
    private void switchToUpdateGiftsScene() throws IOException {
        switchScene("../view/gift/updateGift.fxml");
    }

    @FXML
    private void switchToDeleteGiftsScene() throws IOException {
        switchScene("../view/gift/deleteGift.fxml");
    }

    /****************Switch to candies scenes*******************/

    @FXML
    public void switchToCreateCandyScene() throws IOException {
        switchScene("../view/candy/createCandy.fxml");
    }

    @FXML
    private void switchToSearchCandyScene() throws IOException {
        switchScene("../view/candy/searchCandies.fxml");
    }

    @FXML
    private void switchToUpdateCandyScene() throws IOException {
        switchScene("../view/candy/updateCandy.fxml");
    }

    @FXML
    private void switchToDeleteCandyScene() throws IOException {
        switchScene("../view/candy/deleteCandy.fxml");
    }

    /****************Switch to Other scenes*******************/

    @FXML
    private void switchToAboutScene() throws IOException {
        switchScene("../view/other/about.fxml");
    }

    @FXML
    private void switchToCheckErrorMessageSendingOnMailScene() throws IOException {
        switchScene("../view/other/checkErrorMessageSendingOnMail.fxml");
    }
}
