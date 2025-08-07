package com.eats.service;

import com.eats.dto.UserLoginDTO;
import com.eats.entity.User;

public interface UserService {

    /**
     * WeChatログイン
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
