package com.eats.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.eats.constant.MessageConstant;
import com.eats.constant.StatusConstant;
import com.eats.dto.DishDTO;
import com.eats.dto.DishPageQueryDTO;
import com.eats.entity.Dish;
import com.eats.entity.DishFlavor;
import com.eats.entity.Setmeal;
import com.eats.exception.DeletionNotAllowedException;
import com.eats.mapper.DishFlavorMapper;
import com.eats.mapper.DishMapper;
import com.eats.mapper.SetmealDishMapper;
import com.eats.mapper.SetmealMapper;
import com.eats.result.PageResult;
import com.eats.service.DishService;
import com.eats.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 料理と対応するフレーバーを追
     *
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        //料理テーブル件のデータを挿入
        dishMapper.insert(dish);

        //insert文で生成された主キー値を取得
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //フレーバーテーブルにn件のデータを挿入
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 料理のページング検索
     *
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 料理の一括削
     *
     * @param ids
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //現在の料理が削除可能かどうかを判--販売中の料理が存在するかどうか？
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                //現在の料理は販売中のため、削除できません
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //現在の料理が削除可能かどうかを判--セットメニューに関連付けられているかどうか？？
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            //現在の料理はセットメニューに関連付けられているため、削除できません
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //料理テーブルから料理データを削除
        for (Long id : ids) {
            dishMapper.deleteById(id);
            //料理に関連するフレーバーデータを削除
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    /**
     * IDに基づいて料理と対応するフレーバーデータを検
     *
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        //IDに基づいて料理データを検
        Dish dish = dishMapper.getById(id);

        //料理IDに基づいてフレーバーデータを検索
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);

        //検索したデータをVOにカプセル化
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * IDに基づいて料理の基本情報と対応するフレーバー情報を編
     *
     * @param dishDTO
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //料理テーブルの基本情報を編集
        dishMapper.update(dish);

        //元のフレーバーデータを削
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        //フレーバーデータを再挿入
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //フレーバーテーブルにn件のデータを挿入
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 料理の販売開始・停止
     *
     * @param status
     * @param id
     */
    @Transactional
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);

        if (status == StatusConstant.DISABLE) {
            //販売停止操作の場合、現在の料理を含むセットメニューも販売停止にする必要があります
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // select setmeal_id from setmeal_dish where dish_id in (?,?,?)
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * カテゴリIDに基づいて料理を検索
     *
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * 条件で料理とフレーバーを検索
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //料理IDに基づいて対応するフレーバーを検
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
