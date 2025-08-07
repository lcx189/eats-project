package com.eats.annotation;

import com.eats.enumeration.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * カスタムアノテーション、あるメソッドが機能フィールドの自動補完処理を必要とすることを示すために使用しま
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //データベース操作タイプ：UPDATE INSERT
    OperationType value();
}
