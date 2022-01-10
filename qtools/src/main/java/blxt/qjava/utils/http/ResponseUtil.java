package blxt.qjava.utils.http;

import blxt.qjava.utils.Converter;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口请求工具
 * @author ZhangJieLei
 */
public class ResponseUtil {

    /**
     * 发送get请求
     * 如 https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}?mchid=4646
     * @param url             初始地址  如：https://api.mch.weixin.qq.com/v3/pay/transactions/id/
     * @param pathParames     路径参数  如：{transaction_id}
     * @param queryParames    URL传参  如：mchid=1321263
     * @return
     */
    public static String sendGet(String url, String[] pathParames, Map<String, Object> queryParames) throws IOException {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = makeUrl(url, pathParames, queryParames) ;
            // 打开和URL之间的连接
            URLConnection connection = buildUrlConnection(urlNameString, null);
            if(connection == null){
                //log.error("构建链接失败");
                return null;
            }
            // 建立实际的连接
            connection.connect();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw e;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
               throw e2;
            }
        }
        return result;
    }

    /**
     * HttpPost 发送
     * 如 https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}?mchid=4646
     * @param url             初始地址  如：https://api.mch.weixin.qq.com/v3/pay/transactions/id/
     * @param pathParames     路径参数  如：{transaction_id}
     * @param queryParames    URL传参  如：mchid=1321263
     * @param headParames     headParames，map，可以传null
     * @param body            body参数，map，自动转换成json
     * @return
     */
    public static String sendPost(String url, String[] pathParames, Map<String, Object> queryParames,
                                  Map<String, String> headParames,  Map<String, String> body) throws IOException{
        String bodyjson = Converter.toJsonStr(body);
        return sendPostStr(url, pathParames, queryParames, headParames, bodyjson);
    }

    /**
     * HttpPost 发送
     * 如 https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}?mchid=4646
     * @param url             初始地址  如：https://api.mch.weixin.qq.com/v3/pay/transactions/id/
     * @param pathParames     路径参数  如：{transaction_id}
     * @param queryParames    URL传参  如：mchid=1321263
     * @param headParames     headParames，map，可以传null
     * @param body            body参数，实体，自动转换成json, 或者jsonString
     * @return
     */
    public static String sendPost(String url, String[] pathParames, Map<String, Object> queryParames,
                                  Map<String, String> headParames, Object body) throws IOException{
        String bodyjson = null;
        if(body.getClass().equals(String.class)){
            bodyjson = (String) body;
        }
        else{
            bodyjson = Converter.toJsonStr(body);
        }
        return sendPostStr(url, pathParames, queryParames, headParames, bodyjson);
    }

    /**
     * HttpPost 发送
     * 如 https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}?mchid=4646
     * @param url             初始地址  如：https://api.mch.weixin.qq.com/v3/pay/transactions/id/
     * @param pathParames     路径参数  如：{transaction_id}
     * @param queryParames    URL传参  如：mchid=1321263
     * @param bodyJson        body参数，json
     * @return
     */
    public static String sendPostStr(String url, String[] pathParames, Map<String, Object> queryParames,
                                     Map<String, String> headParames, String bodyJson) throws IOException {
        String urlNameString = makeUrl(url, pathParames, queryParames) ;
        // 构建HttpPost
        HttpPost post = buildHttpPost(urlNameString, headParames);

        // body入参
        StringEntity stringEntity = new StringEntity(bodyJson, "utf-8");
        stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));
        post.setEntity(stringEntity);

        // 链接请求
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpResponse httpResponse = client.execute(post);
            // 回参处理
            String res = getStrByInputStream(httpResponse.getEntity().getContent());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            //  请求服务器成功
                return res;
            } else {
             //   log.warn("请求失败：{}/{}", url, res);
            //  请求服务端失败
                return null;
            }
        } catch (IOException e) {
            throw  e;
        }
    }

    /**
     * 构建 post 请求
     * @param url           http地址
     * @param headParames   头参数
     * @return
     */
    protected static HttpPost buildHttpPost(String url, Map<String, String> headParames){
        HttpPost post = new HttpPost(url);
        // 默认头
        post.setHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Basic YWRtaW46");
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");

        // 附加头
        if(headParames != null){
            for(String key:headParames.keySet()){
                String value = headParames.get(key);
                post.addHeader(key, value);
            }
        }

        return post;
    }


    /**
     * 构建链接, [post]请求链接
     * @param url
     * @return
     */
    protected static HttpURLConnection buildHttpURLConnection(String url, Map<String, String> headParames){
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();
            // 打开和URL之间的连接

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 附加头
            if(headParames != null){
                for(String key:headParames.keySet()){
                    String value = headParames.get(key);
                    conn.setRequestProperty(key, value);
                }
            }

            return conn;
        }catch (Exception e){
           // log.warn("请求失败：{}/{}", url, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 构建链接, get请求链接
     * @param url
     * @return
     */
    protected static URLConnection buildUrlConnection(String url, Map<String, String> headParames){
        URL realUrl = null;
        try {
            realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Charset", "UTF-8");

            // 附加头
            if(headParames != null){
                for(String key:headParames.keySet()){
                    String value = headParames.get(key);
                    connection.setRequestProperty(key, value);
                }
            }

            return connection;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 拼装简单url
     * 如 https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}?mchid=4646
     * @param url             初始地址  如：https://api.mch.weixin.qq.com/v3/pay/transactions/id/
     * @param pathParames     路径参数  如：{transaction_id}
     * @param queryParames    URL传参  如：mchid=1321263
     * @return
     */
    protected static String makeUrl(String url, String[] pathParames, Map<String, Object> queryParames){
        String urlRes = url;
        String queryStr = null;
        if(url.endsWith("?")){
            urlRes = url.substring(0, url.length() - 1);
        }
        if(url.endsWith("/")){
            urlRes = url.substring(0, url.length() - 1);
        }
        if(pathParames != null && pathParames.length > 0){
            for (String s : pathParames){
                urlRes = String.format("%s/%s", urlRes, s);
            }
        }
        if(queryParames != null && queryParames.size() > 0){
            for(String key:queryParames.keySet()){
                Object value = queryParames.get(key);
                String valueStr = null;
                try {
                    valueStr = URLEncoder.encode(String.valueOf(value),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                queryStr = String.format("%s%s%s=%s", queryStr == null? "": queryStr,
                                                      queryStr ==null ? "?": "&",
                                                      key, valueStr);
            }
        }
        else{
            queryStr = "";
        }
        urlRes = String.format("%s%s", urlRes, queryStr);
        return urlRes;
    }

    public static String getBodyParam(String strUrlParam, String key) {
        Map<String, String> mapRequest = getBodyParams(strUrlParam);
        return mapRequest.get(key);
    }
    /**
     * 解析出url参数中的键值对
     * <p>
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param strUrlParam 参数
     * @return url请求参数部分
     */
    public static Map<String, String> getBodyParams(String strUrlParam) {
        Map<String, String> mapRequest = new HashMap();
        String[] arrSplit = null;
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    /**
     *
     * @param inStream
     * @return
     */
    public static String getStrByInputStream(InputStream inStream){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                inStream, StandardCharsets.UTF_8));
            StringBuilder strber = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                strber.append(line).append("\n");
            }
            return strber.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /***
     * 测试
     * @param args
     */
    public static void main(String[] args) {

        String url = "http://127.0.0.1:8507/hnsp/uaccess/loginToken";

        Map<String, String> body = new HashMap<>();
        body.put("kaptcha", "12");
        body.put("kaptchaToken", "123");
        body.put("keepUser", "1234");
        body.put("uaccess", "12345");
        body.put("upwd", "123456");

        String bodyjson = Converter.toJsonStr(body);
        try {
            String res = sendPostStr(url, null, null, null, bodyjson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  log.debug("post:{}", res);
    }


}