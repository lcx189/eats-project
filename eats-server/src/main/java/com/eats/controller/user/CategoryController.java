package com.eats.controller.user;

import com.eats.entity.Category;
import com.eats.result.Result;
import com.eats.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C�?カテゴリAPI")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * カテゴリを照�?
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("カテゴリを照�?")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
