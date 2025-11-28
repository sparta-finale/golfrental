package com.golfRental.domain.notification.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.notification.enums.NotificationType;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationType type;

    @Column
    private Long referenceId;

    @Column(nullable = false)
    private boolean isRead;

    @Builder
    private Notification(User receiver, String title, String content, NotificationType type, Long referenceId) {
        this.receiver = receiver;
        this.title = title;
        this.content = content;
        this.type = type;
        this.referenceId = referenceId;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}