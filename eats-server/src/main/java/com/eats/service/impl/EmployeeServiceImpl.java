package com.eats.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.eats.constant.MessageConstant;
import com.eats.constant.PasswordConstant;
import com.eats.constant.StatusConstant;
import com.eats.context.BaseContext;
import com.eats.dto.EmployeeDTO;
import com.eats.dto.EmployeeLoginDTO;
import com.eats.dto.EmployeePageQueryDTO;
import com.eats.entity.Employee;
import com.eats.exception.AccountLockedException;
import com.eats.exception.AccountNotFoundException;
import com.eats.exception.PasswordErrorException;
import com.eats.mapper.EmployeeMapper;
import com.eats.result.PageResult;
import com.eats.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 従業員ログイ
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、ユーザー名に基づいてデータベース内のデータを検
        Employee employee = employeeMapper.getByUsername(username);

        //2、さまざまな例外状況を処理（ユーザー名が存在しない、パスワードが間違っている、アカウントがロックされている）
        if (employee == null) {
            //アカウントが存在しません
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //パスワードの比較
        //フロントエンドから渡された平文パスワードをmd5で暗号化処理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //パスワードが間違っていま
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //アカウントがロックされていま
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、エンティティオブジェクトを返す
        return employee;
    }

    /**
     * 従業員を追加
     *
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //オブジェクトプロパティのコピ
        BeanUtils.copyProperties(employeeDTO, employee);

        //アカウントの状態を設定、デフォルトは通常状態 1は通常 0はロック
        employee.setStatus(StatusConstant.ENABLE);

        //パスワードを設定、デフォルトパスワードは123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //現在のレコードの作成日時と更新日時を設定
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //現在のレコードの作成者IDと更新者IDを設
        //employee.setCreateUser(BaseContext.getCurrentId());
        //employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

    /**
     * ページング検
     *
     * @param employeePageQueryDTO
     * @return
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // select * from employee limit 0,10
        //ページング検索を開始
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 従業員アカウントの有効化・無効化
     *
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        // update employee set status = ? where id = ?

        /*Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);*/

        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.update(employee);
    }

    /**
     * IDに基づいて従業員を検
     *
     * @param id
     * @return
     */
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");
        return employee;
    }

    /**
     * 従業員情報を編集
     *
     * @param employeeDTO
     */
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }
}
