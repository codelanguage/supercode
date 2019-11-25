package org.lanqiao.menu;

public class Menu {

    private Button[] button;//button	是	一级菜单数组，个数应为1~3个

    public Button[] getButton() {
        return button;
    }

    public void setButton(Button[] button) {
        this.button = button;
    }
}
