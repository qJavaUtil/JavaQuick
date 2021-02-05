package blxt.qjava.qliveness;

import blxt.qjava.quartz.QuartzManager;
import com.google.common.net.MediaType;
import okhttp3.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
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

        if(!QuartzManager.isExist(JOB_NAME)){
            //System.out.println("启动活跃监听:" + JOB_NAME);
            QuartzManager.addJob(JOB_NAME, JOB_GROUP_NAME, JOB_NAME, TRIGGER_GROUP_NAME, this, cron);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {

                }
                sendPost(arl, map);
            }
        }).start();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        String res = sendPost(arl, map);
        //System.out.println("注册:"  + res);
    }

    public static String sendPost(String arl, Map<String, String> params) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(Map.Entry<String, String> entry : params.entrySet()){
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            builder.addFormDataPart(mapKey,mapValue);
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(arl)
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.toString();
        } catch (IOException e) {
            //e.printStackTrace();
        }

        return "error";
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
