package org.lanqiao.menu;

public class ButtonType extends Button {

    private String key;//click等点击类型必须	菜单KEY值，用于消息接口推送，不超过128字节

    /*
    view、miniprogram类型（网页 链接），用户点击菜单可打开链接，不超过1024字节。
    type为miniprogram时，不支持小程序的老版本客户端将打开本url。
     */
    private String url;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
