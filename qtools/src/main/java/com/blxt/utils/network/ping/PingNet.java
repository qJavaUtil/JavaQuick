package com.blxt.utils.network.ping;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * ping 操作,网络检查
 */
public class PingNet {

    private static boolean isLog = false;

    /**
     * ping 测试
     * ping失败,不代表网络不通, 建议使用 IPTools.checkHostIP()
     * @param ip          ip
     * @param timece      ping 次数
     * @param outtime     超时时间,ms
     * @return ping结果
     */
    public static PingNetEntity ping(String ip, int timece, int outtime){

        return ping(new PingNetEntity(ip, timece, outtime, new StringBuffer()));
    }

    /**
     * ping 测试
     * @param ip
     * @return
     */
    public static PingNetEntity ping(String ip){

        return ping(new PingNetEntity(ip, 0, 0, new StringBuffer()));
    }

    /**
     * ping 主机, cmd原理
     * @param pingNetEntity 检测网络实体类
     * @return 检测后的数据
     */
    public static PingNetEntity ping(PingNetEntity pingNetEntity) {
        String line = null;
        Process process = null;
        BufferedReader successReader = null;

        String command = pingNetEntity.getPingCommand();

        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                append(pingNetEntity.getResultBuffer(), "ping fail:process is null.");
                pingNetEntity.setPingTime(null);
                pingNetEntity.setResult(false);
                return pingNetEntity;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = successReader.readLine()) != null) {

                append(pingNetEntity.getResultBuffer(), line);
                String time;
                if ((time = getTime(line)) != null) {
                    pingNetEntity.setPingTime(time);
                }
            }
            int status = process.waitFor();
            if (status == 0) {

                append(pingNetEntity.getResultBuffer(), "exec cmd success:" + command);
                pingNetEntity.setResult(true);
            } else {

                append(pingNetEntity.getResultBuffer(), "exec cmd fail.");
                pingNetEntity.setPingTime(null);
                pingNetEntity.setResult(false);
            }

            append(pingNetEntity.getResultBuffer(), "exec finished.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {

            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return pingNetEntity;
    }




    private static void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text + "\n");
        }
    }

    private static String getTime(String line) {
        String[] lines = line.split("\n");
        String time = null;
        for (String l : lines) {
            if (!l.contains("time=")) {
                continue;
            }
            int index = l.indexOf("time=");
            time = l.substring(index + "time=".length());

        }
        return time;
    }
}


//    PingNetEntity pingNetEntity=new PingNetEntity(purecameraip,3,5,new StringBuffer());
//    pingNetEntity=PingNet.ping(pingNetEntity);
//    if (pingNetEntity.isResult()){
//    Log.i("Ping测试",pingNetEntity.getIp());
//    Log.i("Ping测试","time="+pingNetEntity.getPingTime());
//    Log.i("Ping测试",pingNetEntity.isResult()+"");
//    }
//    else{
//    //UserControl.ui.Error("IP不存在");
//    }


