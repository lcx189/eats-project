package com.eats.controller.admin;

import com.eats.constant.JwtClaimsConstant;
import com.eats.dto.EmployeeDTO;
import com.eats.dto.EmployeeLoginDTO;
import com.eats.dto.EmployeePageQueryDTO;
import com.eats.entity.Employee;
import com.eats.properties.JwtProperties;
import com.eats.result.PageResult;
import com.eats.result.Result;
import com.eats.service.EmployeeService;
import com.eats.utils.JwtUtil;
import com.eats.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 従業員管
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "従業員関連API")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * ログイン
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "従業員ログイ")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("従業員ログイン：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //ログイン成功後、JWTトークンを生
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * ログアウ
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("従業員ログアウト")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 従業員を追加
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("従業員を追加")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("従業員を追加：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 従業員のページング検
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("従業員のページング検索")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("従業員のページング検索、パラメータ：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 従業員アカウントの有効化・無効化
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("従業員アカウントの有効化・無効化")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("従業員アカウントの有効化・無効化：{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * IDに基づいて従業員情報を検
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("IDに基づいて従業員情報を検索")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 従業員情報を編集
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("従業員情報を編集")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("従業員情報を編集：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
