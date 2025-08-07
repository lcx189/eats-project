package com.eats.handler;

import com.eats.constant.MessageConstant;
import com.eats.exception.BaseException;
import com.eats.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * グローバル例外ハンドラ、プロジェクトでスローされたビジネス例外を処�?
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * ビジネス例外をキャッ�?
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("例外情報：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * SQL例外を処�?
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        //Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + MessageConstant.ALREADY_EXISTS;
            return Result.error(msg);
        }else{
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
