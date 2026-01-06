package org.appsdeveloperblog.ws.emailnotification.io;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProcessedEventRepository extends JpaRepository<@NotNull ProcessedEventEntity, @NotNull Long> {

    Optional<ProcessedEventEntity> findByMessageId(String messageId);
}
