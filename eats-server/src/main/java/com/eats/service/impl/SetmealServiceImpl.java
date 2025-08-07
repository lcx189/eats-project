package com.eats.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.eats.constant.MessageConstant;
import com.eats.constant.StatusConstant;
import com.eats.dto.SetmealDTO;
import com.eats.dto.SetmealPageQueryDTO;
import com.eats.entity.Dish;
import com.eats.entity.Setmeal;
import com.eats.entity.SetmealDish;
import com.eats.exception.DeletionNotAllowedException;
import com.eats.exception.SetmealEnableFailedException;
import com.eats.mapper.DishMapper;
import com.eats.mapper.SetmealDishMapper;
import com.eats.mapper.SetmealMapper;
import com.eats.result.PageResult;
import com.eats.service.SetmealService;
import com.eats.vo.DishItemVO;
import com.eats.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * セットメニュービジネス実�?
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * セットメニューを追加し、同時にセットメニューと料理の関連関係を保存する必要がありま�?
     *
     * @param setmealDTO
     */
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //セットメニューテーブルにデータを挿入
        setmealMapper.insert(setmeal);

        //生成されたセットメニューIDを取�?
        Long setmealId = setmeal.getId();

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });

        //セットメニューと料理の関連関係を保存
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * ページング検�?
     *
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * セットメニューの一括削�?
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE == setmeal.getStatus()) {
                //販売中のセットメニューは削除できませ�?
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            //セットメニューテーブルからデータを削�?
            setmealMapper.deleteById(setmealId);
            //セットメニューと料理の関係テーブルからデータを削�?
            setmealDishMapper.deleteBySetmealId(setmealId);
        });
    }

    /**
     * IDに基づいてセットメニューとセットメニューの料理関係を検索
     *
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        SetmealVO setmealVO = setmealMapper.getByIdWithDish(id);
        return setmealVO;
    }

    /**
     * セットメニューを編集
     *
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //1、セットメニューテーブルを編集、updateを実�?
        setmealMapper.update(setmeal);

        //セットメニューID
        Long setmealId = setmealDTO.getId();

        //2、セットメニューと料理の関連関係を削除、setmeal_dishテーブルを操作、deleteを実�?
        setmealDishMapper.deleteBySetmealId(setmealId);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        //3、セットメニューと料理の関連関係を再挿入、setmeal_dishテーブルを操作、insertを実�?
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * セットメニューの販売開始・停�?
     *
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        //セットメニューを販売開始する際、セットメニュー内に販売停止の料理があるかどうかを判断し、ある場合は「セットメニュー内に未販売の料理が含まれているため、販売開始できません」と表示
        if (status == StatusConstant.ENABLE) {
            //select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = ?
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && dishList.size() > 0) {
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE == dish.getStatus()) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 条件検索
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * IDに基づいて料理オプションを検�?
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
