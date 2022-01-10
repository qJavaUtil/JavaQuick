package org.blxt.tts.baidu;

import blxt.qjava.autovalue.exception.DataException;
import blxt.qjava.utils.http.ResponseUtil;
import com.alibaba.fastjson.JSON;

import java.io.IOException;

/**
 * @author OpenJialei
 * @date 2021年12月18日 14:58
 */
public class BaiduTTSHelper {

    String httpUrl = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s";
    String appid;
    String appkey;

    /**
     * 初始化工具
     * app信息从 https://ai.baidu.com/docs#/TTS-API/41ac79a6 申请
     * @param appid
     * @param appkey
     */
    public BaiduTTSHelper(String appid, String appkey) {
        this.appid = appid;
        this.appkey = appkey;

        httpUrl = String.format(httpUrl, appid, appkey);

    }

    /**
     * 获取token
     * @return
     * @throws IOException
     */
    public BaiduTTSToken getToken() throws IOException {
        String res = null;
        try {
            res = ResponseUtil.sendGet(httpUrl, null, null);
        } catch (IOException e) {
            throw e;
        }
        if(res != null){
            BaiduTTSToken bean = JSON.parseObject(res, BaiduTTSToken.class);
            if(bean.getAccess_token() == null){
                throw new DataException(-1, "token获取为空","");
            }
            return bean;
        }
        throw new DataException(-1, "token获取失败","");
    }


    public static void main(String[] args) {
        String appid = "94MjGKzImlVNMxgRb1XyoGTi";
        String appKey = "6Sz0UpLC4HV9D1eSPhbqQy5BHDxrUk1H";

        BaiduTTSHelper baiduTTSHelper = new BaiduTTSHelper(appid, appKey);
        try {
            BaiduTTSToken bean = baiduTTSHelper.getToken();
            System.out.println(bean.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
