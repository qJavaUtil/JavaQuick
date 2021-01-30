package blxt.qjava.qliveness;

import blxt.qjava.quartz.QuartzManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 活跃注册 目前注册到 qjava.register.zhangjialei:60001
 */
public class Register implements Job {

    /** 每月15日 0:59:59 触发 */
    String cron = "59 59 0 15 * ?";
    /** 注册地址 */
    String arl = "http://qjava.register.zhangjialei:60001/qliveness/register";
    /** 首次执行任务延时 */
    int sleep = 1000;
    String appTage[] = null;

    Map<String, String> map;

    /** 开始运行 */
    public void register(){
        String JOB_NAME = "活跃注册" + appTage[0];
        String JOB_GROUP_NAME = "blxt.qjava.qliveness";
        String TRIGGER_GROUP_NAME = "blxt.qjava.qliveness";

        map = UserClientInfoFactory.creadUserClientInfo();
        int i = 0;
        for(String s : appTage){
            if(i == 0){
                map.put("appTage", s);
            }
            else{
                map.put("appTage" + i, s);
            }
            i++;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {

                }
                sendPost(arl, createLinkString(map));
            }
        }).start();

        if(!QuartzManager.isExist(JOB_NAME)){
            System.out.println("启动活跃监听:" + JOB_NAME);
            QuartzManager.addJob(JOB_NAME, JOB_GROUP_NAME, JOB_NAME, TRIGGER_GROUP_NAME, this, cron);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        sendPost(arl, createLinkString(map));
    }

    public static String sendPost(String arl, String s) {
// 创建url资源
        OutputStreamWriter out = null;
        URL url;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            url = new URL(arl);

            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            conn.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            conn.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");//设置消息头，解决508错误
            // 开始连接请求
            conn.connect();
            out = new OutputStreamWriter(
                    conn.getOutputStream(), "UTF-8"); // utf-8编码
            // 写入请求的字符串
            out.append(s);
            out.flush();
            out.close();
            // System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                // System.out.println("success");
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            }

        } catch (Exception e) {
            // System.out.println("发送 POST 请求出现异常！" + e);
            result = new StringBuilder("{\"resCode\":\"1\",\"errCode\":\"1001\",\"resData\":\"\"}");
          //  e.printStackTrace();
            // log.error("远程服务未开启", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
               // ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr.append(key).append("=").append(value);
            } else {
                prestr.append(key).append("=").append(value).append("&");
            }
        }
        return prestr.toString();
    }


    public void setCron(String cron) {
        this.cron = cron;
    }

    public void setArl(String arl) {
        this.arl = arl;
    }

    public void setAppTage(String[] appTage) {
        this.appTage = appTage;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }
}
