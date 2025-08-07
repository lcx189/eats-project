package com.eats.exception;

/**
 * ログイン失敗例外
 */
public class LoginFailedException extends BaseException{
    public LoginFailedException(String msg){
        super(msg);
    }
}
