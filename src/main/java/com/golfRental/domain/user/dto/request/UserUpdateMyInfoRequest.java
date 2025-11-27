package com.golfRental.domain.user.dto.request;

import com.golfRental.common.validation.ValidationRegex;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateMyInfoRequest {

    @Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
    @Pattern(regexp = ValidationRegex.EMAIL_REGEX, message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Size(min = 2, max = 50, message = "이름은 2~50자 사이여야 합니다.")
    @Pattern(regexp = ValidationRegex.USERNAME_REGEX, message = "이름 내 공백은 불가합니다.")
    private String name;

    @Pattern(regexp = ValidationRegex.PHONE_NUMBER_REGEX, message = "전화번호 형식은 01012345678 형태여야 합니다.")
    private String phoneNumber;

    private String address;

    @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.")
    @Pattern(regexp = ValidationRegex.NICKNAME_REGEX, message = "닉네임 전후 공백은 불가합니다.")
    private String nickname;
}
