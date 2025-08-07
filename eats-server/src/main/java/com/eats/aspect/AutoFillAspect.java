package com.eats.aspect;

import com.eats.annotation.AutoFill;
import com.eats.constant.AutoFillConstant;
import com.eats.context.BaseContext;
import com.eats.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * カスタムアスペクト、共通フィールドの自動補完処理ロジックを実装
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * ポイントカッ�?
     */
    @Pointcut("execution(* com.eats.mapper.*.*(..)) && @annotation(com.eats.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知、通知内で共通フィールドの値を設定
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("共通フィールドの自動補完を開始しま�?..");

        //現在インターセプトされているメソッドのデータベース操作タイプを取�?
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//メソッドシグネチャオブジェク�?
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//メソッドのアノテーションオブジェクトを取�?
        OperationType operationType = autoFill.value();//データベース操作タイプを取得

        //現在インターセプトされているメソッドのパラメータ（エンティティオブジェクト）を取�?
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }

        Object entity = args[0];

        //割り当てるデータを準�?
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //現在の操作タイプに応じて、対応するプロパティにリフレクションを介して値を割り当てます
        if(operationType == OperationType.INSERT){
            //4つの共通フィールドに値を割り当てます
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //リフレクションを介してオブジェクトのプロパティに値を割り当てます
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            //2つの共通フィールドに値を割り当てます
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //リフレクションを介してオブジェクトのプロパティに値を割り当てます
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
