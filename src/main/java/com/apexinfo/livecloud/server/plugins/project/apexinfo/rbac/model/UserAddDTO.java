package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

public class UserAddDTO {
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
