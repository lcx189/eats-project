package com.eats.exception;

/**
 * パスワード変更失敗例
 */
public class PasswordEditFailedException extends BaseException{

    public PasswordEditFailedException(String msg){
        super(msg);
    }

}
