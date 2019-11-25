package org.lanqiao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.ReferenceType;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.lanqiao.bean.AccessToken;
import org.lanqiao.code.ActionInfo;
import org.lanqiao.code.Code;
import org.lanqiao.code.Scene;
import org.lanqiao.menu.Button;
import org.lanqiao.menu.ButtonType;
import org.lanqiao.menu.Menu;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NetUtils {

    public static final String APPID = "wx701b8673d5533573";

    public static final String APPSECRET = "be4a375249cde656107756ac631ff32f";

    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    public static final String UPLOADFILE_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    public static final String GET_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    public static final String CHAT_URL = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=关键词";

    public static final String CREATE_QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";

    public static final String TRANSLATE = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    public static final String BAIDUAPPID = "20191115000357247";

    public static final String BAIDUAPPSECRET = "3mDjCaDLlgSOhJm0J5ch";

    // 网络连接工具类
    public static String doGetStr(String urlPath) throws IOException {

        // 创建url对象，帮助获取URLConnection的连接
        URL url = new URL(urlPath);

        // URLConnection连接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        // 设置请求为get
        urlConnection.setRequestMethod("GET");

        // 打开输入流
        urlConnection.setDoInput(true);

        // 关闭输出流
        urlConnection.setDoOutput(false);

        // 建立连接
        urlConnection.connect();

        // 获取输入流后包装
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));

        String len;

        StringBuilder sb = new StringBuilder();

        // 读微信后台返回的内容
        while((len = br.readLine()) != null) {

            sb.append(len);

        }

        // JSON格式的数据
        return sb.toString();

    }

    public static String doPostStr(String urlPath, String params) throws IOException {

        // 创建url对象，帮助获取URLConnection的连接
        URL url = new URL(urlPath);

        // URLConnection连接
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        // 设置请求为get
        urlConnection.setRequestMethod("GET");

        // 打开输入流
        urlConnection.setDoInput(true);

        // 关闭输出流
        urlConnection.setDoOutput(true);

        // 关闭通道的缓存，不禁会有一定几率使用缓存
        urlConnection.setUseCaches(false);

        // 建立连接
//        urlConnection.connect();

        // 使用高效输出流
        PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());

        // 返回给微信服务器，不需要开连接
        printWriter.print(params);

        printWriter.flush();

        // 获取输入流后包装
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));

        String len;

        StringBuilder sb = new StringBuilder();

        // 读微信后台返回的内容
        while((len = br.readLine()) != null) {

            sb.append(len);

        }

        // JSON格式的数据
        return sb.toString();

    }

    public static AccessToken getAccessToken() {

        String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);

        AccessToken token = new AccessToken();

        try {
            String json = doGetStr(url);

            ObjectMapper mapper = new ObjectMapper();

            token = mapper.readValue(json, AccessToken.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return token;

    }

    public static void createMenu(AccessToken accessToken) throws IOException {

        String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", accessToken.getAccess_token());

        ButtonType button1 = new ButtonType();

        button1.setType(MessageUtils.MESSAGE_EVENT_CLICK);

        button1.setName("菜单一");

        button1.setKey("11");

        ButtonType button2 = new ButtonType();

        button2.setType(MessageUtils.MESSAGE_EVENT_VIEW);

        button2.setName("菜单二");

        button2.setUrl("https://www.baidu.com");

        ButtonType button31 = new ButtonType();

        button31.setType(MessageUtils.MESSAGE_EVENT_SCANCODE_PUSH);

        button31.setName("子菜单一");

        button31.setKey("31");

        ButtonType button32 = new ButtonType();

        button32.setType(MessageUtils.MESSAGE_EVENT_LOCATION_SELECT);

        button32.setName("子菜单二");

        button32.setKey("32");

        Button button3 = new Button();

        button3.setName("菜单三");

        button3.setSub_button(new Button[]{button31, button32});

        Menu menu = new Menu();

        menu.setButton(new Button[]{button1, button2, button3});

        ObjectMapper mapper = new ObjectMapper();

        String params = mapper.writeValueAsString(menu);

        System.out.println(params);

        // {"errcode:"0, "errmsg" : "ok"}
        String response = doPostStr(url, params);

        System.out.println(response);

        Map<String, Object> map = (Map<String, Object>) mapper.readValue(response, new TypeReference<Map<String, Object>>() {});

        int errcode = -1;

        errcode = (int)map.get("errcode");

        if(errcode == 0) {

            System.out.println("菜单创建成功");

        }else {

            System.out.println("菜单创建失败");

        }

    }



    public static String uploadFile(String filePath , AccessToken token , String type) throws IOException {
        File file = new File(filePath);

        if(!file.isFile() || !file.exists()){
            throw new IOException("文件不存在");
        }

        String urlPath = UPLOADFILE_URL.replace("ACCESS_TOKEN",token.getAccess_token()).replace("TYPE",type);

        URL url = new URL(urlPath);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        //设置请求头
        connection.setRequestProperty("Connection","Keep-Alive");
        connection.setRequestProperty("Charset","UTF-8");

        String boundary = "-----------"+System.currentTimeMillis();

        connection.setRequestProperty("Content-Type","multipart/form-data; boundary="+boundary);

        //写参数
        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(boundary);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"media\"; filename=\""+file.getName()+"\"\r\n"
                + "Content-Type:application/octet-stream\r\n\r\n");
        System.out.println(sb.toString());

        //写头
        OutputStream os = new DataOutputStream(connection.getOutputStream());
        os.write(sb.toString().getBytes("utf-8"));

        //写文件
        DataInputStream is = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bys = new byte[1024];
        while((bytes = is.read(bys))!=-1){
            os.write(bys,0,bytes);
        }
        is.close();

        //写边界
        String foot = "\n\r--"+boundary+"--"+"\n\r";
        System.out.println(foot);
        os.write(foot.getBytes("utf-8"));

        os.flush();
        os.close();

        //处理响应
        //connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));

        String len;
        StringBuilder sb1 = new StringBuilder();
        while ((len = br.readLine())!= null){
            sb1.append(len);
        }

        return sb1.toString();
    }

    public static void getMedia(AccessToken token , String media_id) throws IOException {
        String url = GET_MEDIA_URL.replace("ACCESS_TOKEN",token.getAccess_token()).replace("MEDIA_ID",media_id);

        System.out.println(url);
        doGetStr(url);

    }

    public static String chat(String msg) throws IOException {

        String url = CHAT_URL.replace("APPID", "0").replace("关键词", msg);

        AccessToken token = new AccessToken();

        String json = doGetStr(url);

        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});

        String result = map.get("result");

        String answer = null;

        if(result.equals("0")) {

                answer = map.get("content");

        }else {

            throw new IOException("出错了");

        }

        return answer;

    }

    /*
    获取二维码
    */
    public static String createQrcode(AccessToken token) throws IOException {
        String url = CREATE_QRCODE.replace("TOKEN",token.getAccess_token());

        Scene scene = new Scene();
        scene.setScene_str("我太难了！");

        ActionInfo info = new ActionInfo();
        info.setScene(scene);

        Code code = new Code();
        code.setExpire_seconds(600);
        code.setAction_name("QR_STR_SCENE");
        code.setAction_info(info);

        ObjectMapper mapper = new ObjectMapper();
        String params = mapper.writeValueAsString(code);
        System.out.println(params);

        String result = doPostStr(url, params);


        return result;
    }

    public static String translate(String word, String language) {

        String json = null;

        try {

            //appid=2015063000000001+q=apple+salt=1435660288+密钥=12345678
            String str = BAIDUAPPID + word + "114514" + BAIDUAPPSECRET;

            System.out.println("str"+str);

            String sign = MD5.md5(str);

            System.out.println("sign"+sign);

            String param = "?q=" + word +
                    "&from=auto" +
                    "&to=" + language + "" +
                    "&appid=" + BAIDUAPPID +"" +
                    "&salt=114514&";

            String url = TRANSLATE + param + "sign=" + sign;

            System.out.println("url"+url);

            String result = NetUtils.doGetStr(url);

            System.out.println(result);

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> map = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});

            List list = (List) map.get("trans_result");

            json = list.toString();

            json = json.substring(json.lastIndexOf("=")+1, json.lastIndexOf("}"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;

    }

}
