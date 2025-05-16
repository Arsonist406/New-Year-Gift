package newYearGift.controller.gift;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import newYearGift.NewYearGiftApplication;
import newYearGift.exception.BusinessException;
import newYearGift.exception.ExceptionHandler;
import newYearGift.model.Gift;
import newYearGift.parser.Parser;
import newYearGift.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Getter
public class SearchGiftsController {

    @FXML
    private TextField idOrNameField;
    @FXML
    private Button findGiftById;
    @FXML
    private Button findGiftByName;
    @FXML
    private Label findNotificationLabel;

    @FXML
    private GridPane giftsGrid;

    private final GiftService giftService;
    private final ExceptionHandler exceptionHandler;
    private final ConfigurableApplicationContext context;
    private GiftDetailsController giftDetailsController;

    @Autowired
    public SearchGiftsController(
            GiftService giftService,
            ExceptionHandler exceptionHandler,
            ConfigurableApplicationContext context
    ) {
        this.giftService = giftService;
        this.exceptionHandler = exceptionHandler;
        this.context = context;
    }

    @FXML
    public void initialize() {
        List<Gift> gifts = giftService.getGiftsByName("");
        populateGiftsGrid(gifts);
    }

    private void populateGiftsGrid(
            List<Gift> gifts
    ) {
        giftsGrid.getChildren().clear();
        giftsGrid.getRowConstraints().clear();
        giftsGrid.getColumnConstraints().clear();

        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / 3);
            column.setHgrow(Priority.ALWAYS);
            giftsGrid.getColumnConstraints().add(column);
        }

        int row = 0;
        int column = 0;

        int amount = 0;
        for (Gift gift : gifts) {
            amount++;
            VBox giftCard = createGiftCard(gift);

            giftCard.setId("giftCard" + amount);

            GridPane.setValignment(giftCard, VPos.TOP);
            GridPane.setMargin(giftCard, new Insets(5));

            giftsGrid.add(giftCard, column, row);

            column++;
            if (column >= 3) {
                column = 0;
                row++;
            }
        }

        for (int i = 0; i <= row; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setValignment(VPos.TOP);
            giftsGrid.getRowConstraints().add(rc);
        }
    }

    private VBox createGiftCard(
            Gift gift
    ) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);");
        card.setPadding(new Insets(10));
        card.setSpacing(10);

        card.setMinSize(270, 300);
        card.setPrefSize(270, 300);
        card.setMaxSize(270, 300);

        Label nameLabel = new Label(gift.getName());
        nameLabel.setFont(Font.font(16));
        nameLabel.setWrapText(true);

        Label descriptionLabel = new Label(gift.getDescription());
        descriptionLabel.setFont(Font.font(14));
        descriptionLabel.setWrapText(true);

        Label authorLabel = new Label("Автор: " + gift.getAuthor());
        authorLabel.setFont(Font.font(14));

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault());
        String date = formatter.format(gift.getCreationDate());

        Label creationDateLabel = new Label("Створено: " + date);
        creationDateLabel.setFont(Font.font(14));
        creationDateLabel.setWrapText(true);

        Separator separator1 = new Separator();
        Separator separator2 = new Separator();

        card.getChildren().addAll(
                nameLabel,
                separator1,
                descriptionLabel,
                separator2,
                authorLabel,
                creationDateLabel
        );

        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 1) {
                    try {
                        loadGiftDetailsScene(gift);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Додаємо ефект при наведенні
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 3);"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3);"));

        return card;
    }

    private void loadGiftDetailsScene(
            Gift gift
    ) throws IOException {
        FXMLLoader loader = new FXMLLoader(NewYearGiftApplication.class.getResource("../view/gift/giftDetails.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        giftDetailsController = loader.getController();
        giftDetailsController.initialize(gift, giftsGrid.getScene());

        Stage stage = (Stage) giftsGrid.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    @FXML
    private void findGiftById() {
        try {
            Long id = Parser.parseLong(idOrNameField, "Id");
            Gift foundGiftById = giftService.getGiftById(id);
            populateGiftsGrid(List.of(foundGiftById));

            findNotificationLabel.setText("");
        } catch (BusinessException e) {
            exceptionHandler.handleBusinessException(e, findNotificationLabel);
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(findNotificationLabel);
        }
    }

    @FXML
    private void findGiftByName() {
        try {
            String name = idOrNameField.getText().trim();
            List<Gift> foundGiftsByName = giftService.getGiftsByName(name);
            populateGiftsGrid(foundGiftsByName);

            findNotificationLabel.setText("");
            if (foundGiftsByName.isEmpty()) {
                findNotificationLabel.setText("No gifts was found");
            }
        } catch (Exception e) {
            exceptionHandler.handleUnpredictedException(findNotificationLabel);
        }
    }
}
