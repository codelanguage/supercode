package org.lanqiao.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lanqiao.bean.WXUserInfo;
import org.lanqiao.utils.NetUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/Callback")
public class CallBackServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String code = request.getParameter("code");

        System.out.println(code+"这是code");

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + NetUtils.APPID +
                "&secret=" + NetUtils.APPSECRET +
                "&code=" + code +
                "&grant_type=authorization_code";

        String result = NetUtils.doGetStr(url);

        System.out.println(result);

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = (Map<String, Object>)mapper.readValue(result, new TypeReference<Map<String, Object>>() {});

        String access_token = (String) map.get("access_token");

        String openid = (String) map.get("openid");

        String url2 = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + access_token +
                "&openid=" + openid +
                "&lang=zh_CN";

        String result2 = NetUtils.doGetStr(url2);

        WXUserInfo userInfo = mapper.readValue(result2, WXUserInfo.class);

        //登录
        //1.没有自己账号体系
        //req.setAttribute("userInfo",userInfo);
        //req.getRequestDispatcher("/success.jsp").forward(req,resp);
        //2.有自己的账号体系
        //将账号与微信绑定 ,如果有nickname则被绑定，没有没被绑定
        request.setAttribute("userInfo",userInfo);
        request.getRequestDispatcher("/success.jsp").forward(request,response);

    }
}
