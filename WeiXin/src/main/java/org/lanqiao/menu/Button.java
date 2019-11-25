package org.lanqiao.menu;

public class Button {

    private String type;// 菜单的响应

    private String name;// 菜单的标题

    private Button[] sub_button;//	二级菜单数组

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Button[] getSub_button() {
        return sub_button;
    }

    public void setSub_button(Button[] sub_button) {
        this.sub_button = sub_button;
    }
}
