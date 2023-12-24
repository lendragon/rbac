package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import com.apexinfo.livecloud.server.common.annotation.Column;
import com.apexinfo.livecloud.server.common.annotation.Table;

/**
 * @ClassName: UserRole
 * @Description: 用户角色关联类, 对应用户角色关联表
 * @Author linlongyue
 * @Date 2023/12/23
 * @Version 1.0
 */
@Table("CT_Rbac_User_Role")
public class UserRole {
    @Column("ID")
    private Long id;
    @Column("FUserId")
    private Long userId;
    @Column("FRoleId")
    private Long roleId;

    public UserRole() {
    }
    public UserRole(Long id, Long userId, Long roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
