package com.example.large_2025.service.impl;

import com.example.large_2025.mapper.UserMapper;
import com.example.large_2025.pojo.User;
import com.example.large_2025.service.IUserService;
import com.example.large_2025.util.BCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户
     */
    @Override
    public User findByNickname(String nickname) {
        return userMapper.findByNickname(nickname);
    }

    /**
     * 注册用户
     */
    @Override
    public void register(String nickname, String password, String email) {
        // 创建用户对象
        User user = new User();
        user.setNickname(nickname);

        // 使用 BCrypt 加密密码
        String hashedPassword = BCryptUtil.hashPassword(password);
        user.setPassword(hashedPassword);

        user.setEmail(email);

        // 保存到数据库
        userMapper.register(user);
    }

    /**
     * 根据邮箱查询用户
     */
    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    /**
     * 删除用户
     */
    @Override
    public void delete(Integer userId) {
        userMapper.delete(userId);
    }

    /**
     * 根据ID查询用户
     */
    @Override
    public User findById(Integer userId) {
        return userMapper.findById(userId);
    }

    /**
     * 更新用户信息
     */
    @Override
    public void update(User user) {
        // 如果密码字段不为空，需要重新加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String hashedPassword = BCryptUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
        }

        userMapper.update(user);
    }
}