package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

/**
 * @ClassName: UserDTO
 * @Description: 用户DTO类, 对应用户信息和AES解密的iv
 * @Author linlongyue
 * @Date 2023/12/17
 * @Version 1.0
 */
public class UserDTO {
    private User user;
    private String iv;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
