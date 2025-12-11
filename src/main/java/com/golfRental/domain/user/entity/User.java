package com.golfRental.domain.user.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(nullable = true, unique = false, length = 255)
    private String password;

    @Column(nullable = false, unique = false, length = 60)
    private String username;

    @Column(nullable = false, unique = true, length = 30)
    private String phoneNumber;

    @Column(nullable = false, unique = false, length = 60)
    private String address;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private User(
            String email, String password, String username, String phoneNumber, String address, String nickname
    ) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nickname = nickname;
        this.role = UserRole.ROLE_USER;
    }

    public static User create(
            String email, String password, String username, String phoneNumber, String address, String nickname
    ) {
        return new User(email, password, username, phoneNumber, address, nickname);
    }

    public void updateMyInfo(
            String email, String username, String phoneNumber, String address, String nickname
    ) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
