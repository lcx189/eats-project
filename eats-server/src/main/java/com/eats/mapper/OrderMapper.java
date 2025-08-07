package com.eats.mapper;

import com.github.pagehelper.Page;
import com.eats.dto.GoodsSalesDTO;
import com.eats.dto.OrdersPageQueryDTO;
import com.eats.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 注文データを挿入
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 注文番号とユーザーIDに基づいて注文を検索
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 注文情報を編
     * @param orders
     */
    void update(Orders orders);

    /**
     * ページング条件検索、注文時間でソー
     * @param ordersPageQueryDTO
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * IDに基づいて注文を検索
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * ステータスに基づいて注文件数を統
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 注文ステータスと注文時間に基づいて注文を検索
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 動的条件に基づいて売上データを統
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 動的条件に基づいて注文件数を統計
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * 指定された時間範囲内の売上ランキングトッ0を統
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin,LocalDateTime end);
}
