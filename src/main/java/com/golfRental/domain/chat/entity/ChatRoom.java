package com.golfRental.domain.chat.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.reservation.entity.Reservation;
import com.golfRental.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false, unique = true)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_user_id", nullable = false)
    private User guestUser;

    @Builder
    private ChatRoom(Reservation reservation, User hostUser, User guestUser) {
        this.reservation = reservation;
        this.hostUser = hostUser;
        this.guestUser = guestUser;
    }

    public boolean isParticipant(Long userId) {
        return hostUser.getId().equals(userId) || guestUser.getId().equals(userId);
    }
}