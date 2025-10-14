package com.example.large_2025.service;
import com.example.large_2025.mapper.UserMapper;
import com.example.large_2025.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
public interface IUserService {
    //根据用户名查询用户
    User findByNickname(String nickname);

    //注册用户
    void register(String nickname, String password,String email);

    //根据邮箱查询用户
    User findByEmail(String email);

    //删除用户
    void delete(Integer userId);

    //根据ID查用户
    User findById(Integer userId);

    //更新用户信息
    void update(User user);
}
