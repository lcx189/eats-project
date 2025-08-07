package com.eats.result;

import lombok.Data;

import java.io.Serializable;

/**
 * バックエンドの統一された戻り結
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //コード：1は成功およびその他の数字は失敗
    private String msg; //エラーメッセージ
    private T data; //デー

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
