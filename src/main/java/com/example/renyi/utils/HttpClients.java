package com.example.renyi.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

public class HttpClients {

    /** POST请求 */
    public static String HttpPost(String Url,String json,String Token) throws Exception{
        String result = "";
        URL realUrl = new URL(Url);
        URLConnection conn = realUrl.openConnection();
        conn.setRequestProperty("user-agent","Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("appKey", "58jNuL4M");
        conn.setRequestProperty("AppSecret", "423E90F07754E6B803E316A1DF7A848D");
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
}
