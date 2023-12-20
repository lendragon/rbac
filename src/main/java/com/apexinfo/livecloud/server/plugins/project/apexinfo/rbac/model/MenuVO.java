package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.model;

import java.util.List;

public class MenuVO {
    // 菜单
    private Menu menu;
    // 子菜单
    private List<MenuVO> children;

    public MenuVO() {
    }

    public MenuVO(Menu menu, List<MenuVO> children) {
        this.menu = menu;
        this.children = children;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<MenuVO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVO> children) {
        this.children = children;
    }
}
