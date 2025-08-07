package com.eats.interceptor;

import com.eats.constant.JwtClaimsConstant;
import com.eats.context.BaseContext;
import com.eats.properties.JwtProperties;
import com.eats.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWTトークン検証インターセプター
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * JWTを検�?
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //現在インターセプトされたのがControllerのメソッドか、それとも他のリソースかを判�?
        if (!(handler instanceof HandlerMethod)) {
            //現在インターセプトされたのは動的メソッドではないため、直接通過させます
            return true;
        }

        //1、リクエストヘッダーからトークンを取得
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、トークンを検証
        try {
            log.info("JWT検証:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("現在のユーザーID：{}", userId);
            BaseContext.setCurrentId(userId);
            //3、検証成功、通過
            return true;
        } catch (Exception ex) {
            //4、検証失敗�?01ステータスコードを応�?
            response.setStatus(401);
            return false;
        }
    }
}
