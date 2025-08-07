package com.eats.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeePageQueryDTO implements Serializable {

    //従業員名
    private String name;

    //ページ番�?
    private int page;

    //1ページあたりの表示レコード数
    private int pageSize;

}
