package newYearGift.logger;

import lombok.Getter;
import newYearGift.NewYearGiftApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggerContainer {

    @Getter
    private final Logger logger;

    public LoggerContainer() {
        logger = LoggerFactory.getLogger(NewYearGiftApplication.class);
    }
}
