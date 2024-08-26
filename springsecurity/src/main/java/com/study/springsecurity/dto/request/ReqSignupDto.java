package com.study.springsecurity.dto.request;
import com.study.springsecurity.domain.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Pattern;

@Data
public class ReqSignupDto {
    @Pattern(regexp = "^[a-z0-9]{6,}$", message = "사용자 이름은 6자이상의 영소문자, 숫자 조합이어야합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*?])[a-zA-Z\\d~!@#$%^&*?]{8,16}$", message = "비밀번호는 8~16자의 영대소문자, 숫자, 특수문자(~!@#$%^&*?)가 포함된 조합이어야합니다.")
    private String password;
//    @Pattern(regexp = "", message = "")
    private String checkPassword;
    @Pattern(regexp = "[가-힣]+$", message = "이름은 한글이어야합니다.")
    private String name;

    public User toEntity(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
    }
}
