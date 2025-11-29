package com.golfRental.domain.notification.repository;

import com.golfRental.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdAndDeletedAtIsNull(Long id);

    Page<Notification> findByReceiverIdAndDeletedAtIsNull(Long receiverId, Pageable pageable);
}