package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    @Query("SELECT n FROM NotificationTask n WHERE n.scheduledTime = :time AND n.status = 'PENDING'")
    List<NotificationTask> findTasksToSend(LocalDateTime time);
}