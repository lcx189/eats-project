package com.eats.mapper;

import com.eats.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * openidに基づいてユーザーを検索
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * データを挿入
     * @param user
     */
    void insert(User user);

    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /**
     * 動的条件に基づいてユーザー数を統�?
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
