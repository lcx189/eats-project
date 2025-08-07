package com.eats.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * アドレス
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //ユーザーID
    private Long userId;

    //受取
    private String consignee;

    //電話番号
    private String phone;

    //性別 0:女1:男
    private String sex;

    //都道府県コー
    private String provinceCode;

    //都道府県
    private String provinceName;

    //市区町村コー
    private String cityCode;

    //市区町村
    private String cityName;

    //地区コー
    private String districtCode;

    //地区
    private String districtName;

    //詳細住所
    private String detail;

    //ラベ
    private String label;

    //デフォル0:いい1:はい
    private Integer isDefault;
}
