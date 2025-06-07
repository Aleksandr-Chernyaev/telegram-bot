package pro.sky.telegrambot.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationScheduler {

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationScheduler(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendDueNotifications() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasksToSend = notificationTaskRepository.findTasksToSend(now);

        for (NotificationTask task : tasksToSend) {
            task.setStatus("SENT");
            notificationTaskRepository.save(task);
        }
    }
}