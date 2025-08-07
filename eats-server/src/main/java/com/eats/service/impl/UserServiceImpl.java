package com.eats.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eats.constant.MessageConstant;
import com.eats.dto.UserLoginDTO;
import com.eats.entity.User;
import com.eats.exception.LoginFailedException;
import com.eats.mapper.UserMapper;
import com.eats.properties.WeChatProperties;
import com.eats.service.UserService;
import com.eats.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //WeChatサービスAPIアドレス
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * WeChatログイン
     * @param userLoginDTO
     * @return
     */
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenid(userLoginDTO.getCode());

        //openidが空かどうかを判断し、空の場合はログイン失敗としてビジネス例外をスロ
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //現在のユーザーが新規ユーザーかどうかを判
        User user = userMapper.getByOpenid(openid);

        //新規ユーザーの場合、自動的に登録を完了
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        //このユーザーオブジェクトを返
        return user;
    }

    /**
     * WeChat APIサービスを呼び出し、WeChatユーザーのopenidを取
     * @param code
     * @return
     */
    private String getOpenid(String code){
        //WeChat APIサービスを呼び出し、現在のWeChatユーザーのopenidを取
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
