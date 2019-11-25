package org.lanqiao.utils;

import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.lanqiao.bean.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class MessageUtils {

    public static final String MESSAGE_TEXT = "text";

    public static final String MESSAGE_EVENT = "event";

    public static final String MESSAGE_EVENT_SUBSCRIBE = "subscribe";

    public static final String MESSAGE_IMAGE = "image";

    public static final String MESSAGE_NEWS = "news";

    public static final String MESSAGE_MUSIC = "music";

    public static final String MESSAGE_EVENT_CLICK = "click";

    public static final String MESSAGE_EVENT_VIEW = "view";

    public static final String MESSAGE_EVENT_SCANCODE_PUSH = "scancode_push";

    public static final String MESSAGE_EVENT_LOCATION_SELECT = "location_select";

    public static final String MESSAGE_EVENT_SCAN= "SCAN";

    public static Map<String, String> xml2Map(HttpServletRequest request) throws IOException, DocumentException {

        Map<String, String> map = new HashMap<>();

        ServletInputStream inputStream = request.getInputStream();

        // 创建saxreader对象解析xml
        SAXReader reader = new SAXReader();

        // 读取xml文件
        Document document = reader.read(inputStream);

        // 获取根节点---xml
        Element root = document.getRootElement();

        // 获取根节点里的子节点
        List<Element> elements = root.elements();

        for (Element element : elements) {

            map.put(element.getName(), element.getText());

        }

        return map;

    }

    public static String text2XML(TextMessage message){
        XStream xStream = new XStream();
        xStream.alias("xml",TextMessage.class);
        return xStream.toXML(message);
    }

    // 订阅时回复的内容
    public static String subscribeText() {

        StringBuilder sb = new StringBuilder();

        sb.append("欢迎来到召唤师峡谷!\n");

        sb.append("[1]查看射手英雄\n");

        sb.append("[2]查看法师英雄\n");

        sb.append("[3]查看打野英雄\n");

        sb.append("[4]查看上单英雄\n");

        sb.append("[5]查看辅助英雄\n");

        return sb.toString();

    }

    public static String getMessage(String fromUserName, String toUserName, String content) {

        TextMessage message = new TextMessage();

        message.setToUserName(fromUserName);

        message.setFromUserName(toUserName);

        message.setCreateTime(new Date().getTime()+"");

        message.setMsgType(MessageUtils.MESSAGE_TEXT);

        message.setContent(content);

        return MessageUtils.text2XML(message);

    }

    // 将图片消息转成xml
    public static String image2XML(ImageMessage message) {

        XStream xStream = new XStream();

        xStream.alias("xml", ImageMessage.class);

        xStream.alias("Image", Image.class);

        return xStream.toXML(message);

    }

    public static String imageText2XML(ImageTextMessage message) {

        XStream xStream = new XStream();

        xStream.alias("xml", ImageTextMessage.class);

        xStream.alias("item", Item.class);

        return xStream.toXML(message);

    }

    public static String initImageMessage(String fromUserName, String toUserName) {

        Image image = new Image();

        image.setMediaId("sWGG_fCBjlRj_DS6axT2UB__n126Cg2ZN4RX7aur_fg_ouywSONifoPuxwoevOgS");

        ImageMessage message = new ImageMessage();

        message.setFromUserName(toUserName);

        message.setToUserName(fromUserName);

        message.setImage(image);

        message.setCreateTime(new Date().getTime()+"");

        message.setMsgType(MessageUtils.MESSAGE_IMAGE);

        return image2XML(message);

    }

    public static String initImageText(String fromUserName, String toUserName) {

        List<Item> list = new ArrayList<>();

        Item item = new Item();

        item.setTitle("这是正文标题");

        item.setDescription("这是正文描述");

        item.setPicUrl("http://freedoor.free.idcfengye.com/WeiXin/image/aftersport.jpg");

        item.setUrl("https://zh.moegirl.org/%E5%B0%91%E5%A5%B3%E5%89%8D%E7%BA%BF#");

        list.add(item);

        ImageTextMessage message = new ImageTextMessage();

        message.setFromUserName(toUserName);

        message.setToUserName(fromUserName);

        message.setCreateTime(new Date().getTime()+"");

        message.setMsgType(MESSAGE_NEWS);

        message.setArticleCount(list.size());

        message.setArticles(list);

        System.out.println(message);

        return imageText2XML(message);

    }

    /*
    将音乐消息转成xml
     */
    public static String music2XML(MusicMessage message){
        XStream xStream = new XStream();
        xStream.alias("xml",MusicMessage.class);
        xStream.alias("Music",Music.class);
        return xStream.toXML(message);
    }

    public static String initMusicMessage(String fromUserName , String toUserName){
        Music music = new Music();
        music.setTitle("愛にできることはまだあるかい");
        music.setDescription("爱还可以做到什么");
        music.setMusicUrl("http://freedoor.free.idcfengye.com/WeiXin/music/music.mp3");
        music.setHQMusicUrl("http://freedoor.free.idcfengye.com/WeiXin/music/music.mp3");
        music.setThumbMediaId("6Gz4DSNqnQvxj_glp9kNmn6k8nLRh6bZm5B5qT9Y_MchZjOLSxU1sFSAMRsYEfnt");

        MusicMessage message = new MusicMessage();
        message.setFromUserName(toUserName);
        message.setToUserName(fromUserName);
        message.setCreateTime(new Date().getTime()+"");
        message.setMsgType(MESSAGE_MUSIC);
        message.setMusic(music);

        return music2XML(message);
    }

}
