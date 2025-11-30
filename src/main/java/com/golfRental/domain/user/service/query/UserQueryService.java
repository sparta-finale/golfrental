package com.golfRental.domain.user.service.query;

import com.golfRental.common.response.PageResponse;
import com.golfRental.domain.user.dto.response.UserGetAllResponse;
import com.golfRental.domain.user.dto.response.UserGetInfoResponse;
import com.golfRental.domain.user.dto.response.UserGetMyInfoResponse;
import com.golfRental.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface UserQueryService {

    /**
     * 내 정보 조회 API
     *
     * @param myId 내 ID
     * @return UserGetMyInfoResponse
     */
    UserGetMyInfoResponse getMyInfo(Long myId);

    /**
     * 유저 정보 조회 API
     *
     * @param userId 유저 ID
     * @return UserGetInfoResponse
     */
    UserGetInfoResponse getInfo(Long userId);

    /**
     * 유저 조회 (ADMIN) API
     *
     * @param pageable 페이지 정보
     * @return PageResponse<UserGetAllResponse>
     */
    PageResponse<UserGetAllResponse> getAll(Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    User findById(Long userId);
}
