package blxt.qjava.utils.system;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @author OpenJialei
 * @since 2022年08月29日 17:19
 */
@Slf4j
public class CpuHelp {

    /**
     * 获取CPU序列号.
     *
     * @return  CPU序列号
     */
    public static String getCPUSerial() {
        if(SystemosEnum.getOs() == SystemosEnum.Windows){
            return getCPUSerial4Win();
        }
        else if(SystemosEnum.getOs() == SystemosEnum.Linux){
            return getCPUSerial4Linux();
        }
        return null;
    }

    /**
     * 获取linux的cpu序列号.
     * @return
     */
    private static String getCPUSerial4Linux() {
        String result = "";
        String CPU_ID_CMD = "dmidecode";
        BufferedReader bufferedReader = null;
        Process p = null;
        try {
            // 管道
            p = Runtime.getRuntime().exec(new String[] { "sh", "-c", CPU_ID_CMD });
            bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[hwaddr]
                index = line.toLowerCase().indexOf("uuid");
                if (index >= 0) {// 找到了
                    // 取出mac地址并去除2边空格
                    result = line.substring(index + "uuid".length() + 1).trim();
                    break;
                }
            }
        } catch (IOException e) {
            log.error("获取cpu信息错误", e.getMessage());
            e.printStackTrace();
        }
        return result.trim();
    }

    /**
     * 获取windos的cpu序列号.
     * @return cpu序列号.
     */
    private static String getCPUSerial4Win() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                + "Set colItems = objWMIService.ExecQuery _ \n"
                + "   (\"Select * from Win32_Processor\") \n"
                + "For Each objItem in colItems \n"
                + "    Wscript.Echo objItem.ProcessorId \n"
                + "    exit for  ' do the first cpu only! \n" + "Next \n";

            // + "    exit for  \r\n" + "Next";
            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec(
                "cscript //NoLogo " + path);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (result.trim().length() < 1 || result == null) {
            result = "无CPU_ID被读取";
        }
        return result.trim();
    }


}
