package com.eats.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * セットメニュー概
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealOverViewVO implements Serializable {
    // 販売中数
    private Integer sold;

    // 販売停止数量
    private Integer discontinued;
}
