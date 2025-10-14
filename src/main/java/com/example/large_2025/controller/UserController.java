package com.example.large_2025.controller;

import com.example.large_2025.pojo.User;
import com.example.large_2025.service.IUserService;
import com.example.large_2025.util.BCryptUtil;
import com.example.large_2025.util.JWTUtil;
import com.example.large_2025.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    /**
     * 用户注册
     * POST /user/register
     * Request Body: {"nickname": "xxx", "password": "xxx", "email": "xxx"}
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String nickname = request.get("nickname");
            String password = request.get("password");
            String email = request.get("email");

            // 参数校验
            if (nickname == null || nickname.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "用户名不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (password == null || password.length() < 6) {
                response.put("success", false);
                response.put("message", "密码长度不能少于6位");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                response.put("success", false);
                response.put("message", "邮箱格式不正确");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 检查用户名是否已存在
            User existingUserByNickname = userService.findByNickname(nickname);
            if (existingUserByNickname != null) {
                response.put("success", false);
                response.put("message", "用户名已存在");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // 检查邮箱是否已存在
            User existingUserByEmail = userService.findByEmail(email);
            if (existingUserByEmail != null) {
                response.put("success", false);
                response.put("message", "邮箱已被注册");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // 注册用户
            userService.register(nickname, password, email);
            logger.info("用户注册成功: {}", nickname);

            response.put("success", true);
            response.put("message", "注册成功");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("用户注册失败", e);
            response.put("success", false);
            response.put("message", "注册失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 用户登录
     * POST /user/login
     * Request Body: {"nickname": "xxx", "password": "xxx"}
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String nickname = request.get("nickname");
            String password = request.get("password");

            // 参数校验
            if (nickname == null || nickname.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "用户名不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (password == null || password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "密码不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 查询用户
            User user = userService.findByNickname(nickname);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户名或密码错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 验证密码
            if (!BCryptUtil.verifyPassword(password, user.getPassword())) {
                response.put("success", false);
                response.put("message", "用户名或密码错误");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 生成 JWT Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("nickname", user.getNickname());
            claims.put("email", user.getEmail());

            String token = JWTUtil.GenToken(claims);

            logger.info("用户登录成功: {}", nickname);

            response.put("success", true);
            response.put("message", "登录成功");
            response.put("token", token);

            // 返回用户基本信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("email", user.getEmail());
            response.put("user", userInfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("用户登录失败", e);
            response.put("success", false);
            response.put("message", "登录失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取当前登录用户信息
     * GET /user/info
     * Header: Authorization: token
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo() {
        Map<String, Object> response = new HashMap<>();

        try {
            // 从 ThreadLocal 获取当前用户信息
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("userId");

            User user = userService.findById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // 构建返回数据（不包含密码）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("email", user.getEmail());

            response.put("success", true);
            response.put("data", userInfo);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("获取用户信息失败", e);
            response.put("success", false);
            response.put("message", "获取用户信息失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 根据ID查询用户
     * GET /user/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable("userId") Integer userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.findById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // 构建返回数据（不包含密码）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("email", user.getEmail());

            response.put("success", true);
            response.put("data", userInfo);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("查询用户失败", e);
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 更新用户信息
     * PUT /user/update
     * Request Body: {"nickname": "xxx", "email": "xxx", "password": "xxx"}
     * Header: Authorization: token
     */
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 从 ThreadLocal 获取当前用户信息
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("userId");

            User user = userService.findById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // 更新字段
            String nickname = request.get("nickname");
            String email = request.get("email");
            String password = request.get("password");

            if (nickname != null && !nickname.trim().isEmpty()) {
                // 检查新用户名是否已被使用
                if (!nickname.equals(user.getNickname())) {
                    User existingUser = userService.findByNickname(nickname);
                    if (existingUser != null) {
                        response.put("success", false);
                        response.put("message", "用户名已存在");
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                    }
                }
                user.setNickname(nickname);
            }

            if (email != null && !email.trim().isEmpty()) {
                // 检查新邮箱是否已被使用
                if (!email.equals(user.getEmail())) {
                    User existingUser = userService.findByEmail(email);
                    if (existingUser != null) {
                        response.put("success", false);
                        response.put("message", "邮箱已被注册");
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                    }
                }
                user.setEmail(email);
            }

            if (password != null && !password.trim().isEmpty()) {
                if (password.length() < 6) {
                    response.put("success", false);
                    response.put("message", "密码长度不能少于6位");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                user.setPassword(password);  // UserService.update() 会自动加密
            }

            // 更新用户信息
            userService.update(user);
            logger.info("用户信息更新成功: {}", userId);

            response.put("success", true);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("更新用户信息失败", e);
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除用户
     * DELETE /user/delete
     * Header: Authorization: token
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUser() {
        Map<String, Object> response = new HashMap<>();

        try {
            // 从 ThreadLocal 获取当前用户信息
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("userId");

            User user = userService.findById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            userService.delete(userId);
            logger.info("用户删除成功: {}", userId);

            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("删除用户失败", e);
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 健康检查
     * GET /user/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "UserService");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}