package com.study.springsecurity.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RespJwtDto {
    private String accessToken;
    private String refreshToken;
}
