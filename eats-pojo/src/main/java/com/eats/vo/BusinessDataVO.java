package com.eats.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * データ概�?
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDataVO implements Serializable {

    private Double turnover;//売上�?

    private Integer validOrderCount;//有効注文件数

    private Double orderCompletionRate;//注文完了�?

    private Double unitPrice;//平均客単�?

    private Integer newUsers;//新規ユーザー�?

}
