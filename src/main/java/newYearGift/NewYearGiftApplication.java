package newYearGift;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NewYearGiftApplication extends Application {

    @Getter
    private static ConfigurableApplicationContext context;

    @Override
    public void init() {
        context = SpringApplication.run(NewYearGiftApplication.class, getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage stage) throws Exception {
        // gets fxml file from the "resources" folder
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/other/about.fxml"));
        // gets appropriate controller from context
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("New Year Gift");
        Image icon = new Image(getClass().getResourceAsStream("../image/icon.png"));
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void stop() {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
