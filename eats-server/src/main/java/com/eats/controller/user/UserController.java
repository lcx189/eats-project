package com.eats.controller.user;

import com.eats.constant.JwtClaimsConstant;
import com.eats.dto.UserLoginDTO;
import com.eats.entity.User;
import com.eats.properties.JwtProperties;
import com.eats.result.Result;
import com.eats.service.UserService;
import com.eats.utils.JwtUtil;
import com.eats.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Api(tags = "C側ユーザー関連API")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * WeChatログイン
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("WeChatログイン")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("WeChatユーザーログイン：{}",userLoginDTO.getCode());

        //WeChatログイン
        User user = userService.wxLogin(userLoginDTO);

        //WeChatユーザーのためにJWTトークンを生�?
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }
}
