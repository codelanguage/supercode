package org.lanqiao.servlet;

import org.dom4j.DocumentException;
import org.lanqiao.bean.AccessToken;
import org.lanqiao.bean.TextMessage;
import org.lanqiao.utils.MessageUtils;
import org.lanqiao.utils.NetUtils;
import org.lanqiao.utils.WeiXinUtils;
import sun.nio.ch.Net;
import sun.plugin2.message.Message;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

@WebServlet("/WeiXinServlet")
public class WeiXinServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");

        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();

        try {

            // 将微信传过来的xml转成map
            Map<String, String> map = MessageUtils.xml2Map(request);

            System.out.println(map);

            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");

            String msgType = map.get("MsgType");


            String message = null;


            if(MessageUtils.MESSAGE_TEXT.equals(msgType)) {

                // 回复1
                String content = map.get("Content");

                if("1".equals(content)){

                    message = MessageUtils.getMessage(fromUserName,toUserName,"维鲁斯,小黄毛,卡莎,小炮,女警");

                }else if("2".equals(content)){

                    message = MessageUtils.getMessage(fromUserName,toUserName,"妖姬,托儿索,儿童劫,球女,雷电法王");

                }else if("3".equals(content)) {

                    // 回复图片消息
                    message = MessageUtils.initImageMessage(fromUserName, toUserName);

                }else if("4".equals(content)) {

                    // 回复图片消息
                    message = MessageUtils.initImageText(fromUserName, toUserName);

                }else if("5".equals(content)) {

                    // 回复音乐消息
                    message = MessageUtils.initMusicMessage(fromUserName, toUserName);

                }else {

                    // 自动回复、聊天
//                    String chat = NetUtils.chat(URLEncoder.encode(content, "utf-8"));
//
//                    message = MessageUtils.getMessage(fromUserName, toUserName, chat);
//
//                    System.out.println(chat);

                    // 获取文字调用百度翻译接口
                    String s = NetUtils.translate(content, "en");

                    message = MessageUtils.getMessage(fromUserName, toUserName, s);

                }

            }else if(MessageUtils.MESSAGE_EVENT.equals(msgType)) {

                String event = map.get("Event");

                if(MessageUtils.MESSAGE_EVENT_SUBSCRIBE.equals(event)) {

                    // 给微信回复消息
                    message = MessageUtils.getMessage(fromUserName, toUserName, MessageUtils.subscribeText());

                }else if(MessageUtils.MESSAGE_EVENT_CLICK.equalsIgnoreCase(event)) {

                    String eventKey = map.get("EventKey");

                    System.out.println(eventKey);

                    if(eventKey.equals("11")) {

                        // 点击菜单一
                        message = MessageUtils.getMessage(fromUserName, toUserName, "菜单一被点击");

                    }

                }else if(MessageUtils.MESSAGE_EVENT_SCANCODE_PUSH.equals(event)) {

                    String  eventKey = map.get("EventKey");

                    System.out.println(eventKey);

                    if(eventKey.equals("31")){

                        //点击子菜单一
                        message = MessageUtils.getMessage(fromUserName,toUserName,"我太难了");

                    }

                }else if(MessageUtils.MESSAGE_EVENT_SCAN.equals(event)){

                    String eventKey = map.get("EventKey");

                    System.out.println(event);

                    System.out.println(eventKey);

                    message = MessageUtils.getMessage(fromUserName,toUserName,eventKey);

                }

            }

            out.print(message);

            //要回复的文本消息
//            TextMessage message = new TextMessage();
//
//            message.setToUserName(fromUserName);
//            message.setFromUserName(toUserName);
//            message.setCreateTime(new Date().getTime()+"");
//            message.setMsgType("text");
//            message.setContent("爱萝莉真的是太好了");
//
//            String content = map.get("Content");
//
//            if(content.equals("你好")) {
//
//                message.setContent("你好");
//
//            }else if(content.equals("1")) {
//
//                message.setContent("啊~啊~~啊~~~~啊啊啊啊啊啊啊啊啊啊");
//
//            }else if(content.equals("2")) {
//
//                message.setContent("你是一个，你是一个");
//
//            }else if(content.equals("3")) {
//
//                message.setContent("sodayo⬇");
//
//            }
//
//            String xml = MessageUtils.text2XML(message);
//
//            System.out.println(xml);
//
//            response.getWriter().print(xml);

        } catch (DocumentException e) {

            e.printStackTrace();

        }finally {

            out.close();

        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("这里");

        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        //将token、timestamp、nonce三个参数进行字典序排序
        String token = "ZDFH";

        System.out.println(signature + timestamp + nonce + echostr);

        String signature1 = WeiXinUtils.checkSignature(token, timestamp, nonce);

        if(signature1.equals(signature)){
            response.getWriter().print(echostr);
        }

    }
}
