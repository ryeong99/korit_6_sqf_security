package com.study.springsecurity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ReqSigninDto {
    //@Pattern(regexp = "^[a-z0-9]{6,}$", message = "사용자 이름은 6자이상의 영소문자, 숫자 조합이어야합니다.")
    @NotBlank
    private String username;
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*?])[a-zA-Z\\d~!@#$%^&*?]{8,16}$", message = "비밀번호는 8~16자의 영대소문자, 숫자, 특수문자(~!@#$%^&*?)가 포함된 조합이어야합니다.")
    @NotBlank(message = "빈 값")
    private String password;
}
