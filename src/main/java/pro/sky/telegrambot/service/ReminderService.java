package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReminderService {

    private final NotificationTaskRepository notificationTaskRepository;

    public ReminderService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    private static final String PATTERN = "(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)";
    private static final Pattern pattern = Pattern.compile(PATTERN);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public void parseAndSaveReminder(Long chatId, String messageText) {
        Matcher matcher = pattern.matcher(messageText);
        if (matcher.matches()) {
            String dateTimeStr = matcher.group(1);
            String reminderText = matcher.group(3);

            LocalDateTime scheduledTime = LocalDateTime.parse(dateTimeStr, formatter);

            NotificationTask task = new NotificationTask();
            task.setChatId(chatId);
            task.setMessageText(reminderText);
            task.setScheduledTime(scheduledTime);
            task.setStatus("PENDING");

            notificationTaskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Сообщение не соответствует формату напоминания");
        }
    }
}