package com.example.large_2025.pojo;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "tab_user")
public class User {

    /**
     * 用户ID，自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 邮箱地址
     */
    @Column(name = "email", nullable = false,unique = true, length = 100)
    private String email;


    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 用户昵称
     */
    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    //Getter and Setter
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname +
                '}';
    }
}
