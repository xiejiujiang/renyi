package com.example.renyi.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpClients {

    public final static String openurl = "https://openapi.chanjet.com";

    /** POST请求 */
    public static String HttpPost(String Url,String json,String Token) throws Exception{
        String result = "";
        URL realUrl = new URL(openurl+Url);
        URLConnection conn = realUrl.openConnection();
        conn.setRequestProperty("user-agent","Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("appKey", "A9A9WH1i");
        conn.setRequestProperty("AppSecret", "C69A8CD150DA721BCB2DF5176D31E97E");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("openToken", Token);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        //PrintWriter out = new PrintWriter(conn.getOutputStream());
        PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
        out.print(json);
        out.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        out.close();
        in.close();
        return result;
    }



    /** GET请求 */
    public static String HttpGet(String Url,String param,String Token) throws Exception{
        String result = "";
        BufferedReader in = null;
        try{
            String charset = java.nio.charset.StandardCharsets.UTF_8.name();
            //String query = String.format("param1=%s",URLEncoder.encode(param1, charset));
            //若是两个请求参数，使用param1=%s&param2=%s
            //String query = String.format("wd=%s&param2=%s",URLEncoder.encode(param1, charset),URLEncoder.encode(param2, charset));
            String request = Url + "?" + param;

            //打开和URL之间的连接
            URLConnection connection = new URL(request).openConnection();
            /* begin获取响应码 */
            HttpURLConnection httpUrlConnection = (HttpURLConnection)connection;
            httpUrlConnection.setConnectTimeout(300000);
            httpUrlConnection.setReadTimeout(300000);
            httpUrlConnection.setRequestProperty("user-agent","Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
            httpUrlConnection.setRequestProperty("Charset", "UTF-8");
            httpUrlConnection.setRequestProperty("appKey", "58jNuL4M");
            httpUrlConnection.setRequestProperty("AppSecret", "423E90F07754E6B803E316A1DF7A848D");
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");
            httpUrlConnection.setRequestProperty("openToken", Token);
            httpUrlConnection.connect();
            //获取响应码 =200
            int resCode = httpUrlConnection.getResponseCode();
            //获取响应消息 =OK
            String message = httpUrlConnection.getResponseMessage();

            System.out.println(" getResponseCode resCode = " + resCode);
            System.out.println(" getResponseMessage message = " + message);
            /* end获取响应码 */

            /* begin获取响应headers*/
            System.out.println("响应头：" + result);
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                System.out.println(header.getKey() + "=" + header.getValue());
            }
            /* end获取响应headers*/

            /* begin获取响应内容 /
            if (resCode == httpUrlConnection.getResponseCode()) {
                int contentLength = httpUrlConnection.getContentLength();
                System.out.println("contentLength--->" + contentLength);
                if(contentLength > 0){
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        result += "\n" + inputLine;
                    System.out.println("响应内容：" + result);
                }
            }
            /* end获取响应内容 */

            /*
            //设置通用的请求属性
            connection.setRequestProperty("Accept-Charset", charset);
            //建立实际的连接
            connection.connect();
            //获取响应头部
            Map<String,List<String>> map = connection.getHeaderFields();
            System.out.println("\n显示响应Header信息...\n");
            //遍历所有的响应头字段并输出
            //方式一、
            for (String key : map.keySet()) {
                System.out.println(key + " : " + map.get(key));
            }
            //方式二、
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                System.out.println(header.getKey() + "=" + header.getValue());
            }
            */
            //打印response body
            //方式一、定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                result += "\n" + inputLine;
            System.out.println("result===" + result);
            /*
            //方式二、使用Scanner
            System.out.println("响应内容：");
            InputStream response = connection.getInputStream();

            try(Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println(responseBody);
            }*/

            //解析响应json
            JSONObject json = JSONObject.parseObject(result/*"待解析的json字符串"*/);
            System.out.println(JSONObject.toJSONString(json, true));
        }catch (Exception e){
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }// 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
