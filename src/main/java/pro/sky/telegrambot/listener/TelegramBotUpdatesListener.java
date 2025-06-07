package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import pro.sky.telegrambot.service.ReminderService;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private ReminderService reminderService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            logger.info("Processing update: {}", update);

            if (update.message() != null && update.message().text() != null) {
                String messageText = update.message().text();
                Long chatId = update.message().chat().id();

                if (messageText.equalsIgnoreCase("/start")) {
                    String welcomeMessage = "Добро пожаловать в бота Bel. Чем могу помочь?";
                    SendMessage message = new SendMessage(chatId, welcomeMessage);
                    telegramBot.execute(message);
                } else {
                    try {
                        reminderService.parseAndSaveReminder(chatId, messageText);
                        String confirmation = "Напоминание сохранено!";
                        telegramBot.execute(new SendMessage(chatId, confirmation));
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}