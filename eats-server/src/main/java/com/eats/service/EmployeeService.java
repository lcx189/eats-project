package com.eats.service;

import com.eats.dto.EmployeeDTO;
import com.eats.dto.EmployeeLoginDTO;
import com.eats.dto.EmployeePageQueryDTO;
import com.eats.entity.Employee;
import com.eats.result.PageResult;

public interface EmployeeService {

    /**
     * 従業員ログイ�?
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 従業員を追加
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * ページング検�?
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 従業員アカウントの有効化・無効化
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * IDに基づいて従業員を検�?
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 従業員情報を編集
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
