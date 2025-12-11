package com.golfRental.domain.user.dto.request;

import com.golfRental.common.validation.ValidationRegex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateMyInfoRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Size(max = 50, message = "이메일은 최대 50자까지 가능합니다.")
        @Pattern(regexp = ValidationRegex.EMAIL_REGEX, message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "이름은 필수 입력값입니다.")
        @Size(min = 2, max = 50, message = "이름은 2~50자 사이여야 합니다.")
        @Pattern(regexp = ValidationRegex.USERNAME_REGEX, message = "이름 내 공백은 불가합니다.")
        String username,

        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        @Pattern(regexp = ValidationRegex.PHONE_NUMBER_REGEX, message = "전화번호 형식은 01012345678 형태여야 합니다.")
        String phoneNumber,

        @NotBlank(message = "주소는 필수 입력값입니다.")
        String address,

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2~10자 사이여야 합니다.")
        @Pattern(regexp = ValidationRegex.NICKNAME_REGEX, message = "닉네임 전후 공백은 불가합니다.")
        String nickname
) {
}
