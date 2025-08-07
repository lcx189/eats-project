package com.eats.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "eats.jwt")
@Data
public class JwtProperties {

    /**
     * 管理者側従業員のJWTトークン生成に関する設定
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * ユーザー側WeChatユーザーのJWTトークン生成に関する設定
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
