package com.eats.exception;

/**
 * セットメニューの有効化失敗例
 */
public class SetmealEnableFailedException extends BaseException {

    public SetmealEnableFailedException(){}

    public SetmealEnableFailedException(String msg){
        super(msg);
    }
}
