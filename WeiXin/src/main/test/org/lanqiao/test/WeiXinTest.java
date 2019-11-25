package org.lanqiao.test;

import org.junit.Test;
import org.lanqiao.bean.AccessToken;
import org.lanqiao.utils.NetUtils;

import java.io.IOException;
import java.net.URLEncoder;

public class WeiXinTest {

    @Test
    public void test() {

        AccessToken token = NetUtils.getAccessToken();

        System.out.println(token.getAccess_token());

        System.out.println(token.getExpires_in());

    }

    @Test
    public void test1() throws IOException {

        NetUtils.createMenu(NetUtils.getAccessToken());



    }

    @Test
    public void test5() throws IOException {
        String filePath = "E:\\各种背景图片尺寸\\少女前线\\AK-12（静寂苍蓝）.png";

        String image = NetUtils.uploadFile(filePath, NetUtils.getAccessToken(), "image");

        System.out.println(image);

    }

    @Test
    public void test6() throws IOException {
        String media = "DgzI-s9uW1wHamXemEYdI1SGVUlucBXXO3JnYw67sbv3p8VQZljDjZiBoF6XJU8h";
        NetUtils.getMedia(NetUtils.getAccessToken(),media);

    }

    @Test
    public void test7() throws IOException {

        String result = NetUtils.chat(URLEncoder.encode("你好", "utf-8"));

        System.out.println(result);

    }

    @Test
    public void test8() throws IOException {
        String qrcode = NetUtils.createQrcode(NetUtils.getAccessToken());
        System.out.println(qrcode);

    }
    @Test
    public void test9() {

        String qrcode = NetUtils.translate("你好", "en");
        System.out.println(qrcode);

    }

}
