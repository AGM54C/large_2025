package com.example.large_2025.mapper;

import com.example.large_2025.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    //添加用户
    @Insert("INSERT INTO tab_user (nickname, password, email) VALUES (#{nickname}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    void register(User user);

    //根据ID查询用户
    @Select("SELECT * FROM tab_user WHERE user_id = #{userId}")
    User findById(Integer userId);

    //根据用户名查询用户
    @Select("SELECT * FROM tab_user WHERE nickname = #{nickname}")
    User findByNickname(String nickname);

    //根据邮箱查询用户
    @Select("SELECT * FROM tab_user WHERE email = #{email}")
    User findByEmail(String email);

    //更新用户信息
    @Update("UPDATE tab_user SET nickname = #{nickname}, password = #{password}, email = #{email} WHERE user_id = #{userId}")
    void update(User user);

    //删除用户
    @Delete("DELETE FROM tab_user WHERE user_id = #{userId}")
    void delete(Integer userId);
}