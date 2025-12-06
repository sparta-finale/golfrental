package com.golfRental.domain.user.repository;

import com.golfRental.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    long countByEmailAndIdNot(String email, Long id);

    long countByPhoneNumberAndIdNot(String phoneNumber, Long id);

    long countByNicknameAndIdNot(String nickname, Long id);

    Page<User> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    List<User> findAllUsers();
}
