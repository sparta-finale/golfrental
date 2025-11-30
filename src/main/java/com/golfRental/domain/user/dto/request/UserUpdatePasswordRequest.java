package com.golfRental.domain.user.dto.request;

import com.golfRental.common.validation.ValidationRegex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdatePasswordRequest {

    @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
    private String oldPassword;

    @NotBlank(message = "새로운 비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 30, message = "비밀번호는 8~30자 사이여야 합니다.")
    @Pattern(regexp = ValidationRegex.PASSWORD_REGEX,
            message = "비밀번호는 대소문자 불문 영문, 숫자, 특수문자를 모두 포함해야 하고 공백은 불가합니다.")
    private String newPassword;
}
