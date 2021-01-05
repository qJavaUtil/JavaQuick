package com.qjava.qsql.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 工具类
 * @Author: Zhang.Jialei
 * @Date: 2020/9/12 14:40
 */
public class Toos {

    /**
     * @param file 配置文件名
     * @return Properties对象
     */
    public static Properties loadPropertiesFile(File file) {
        if (null == file || !file.exists()){
            throw new IllegalArgumentException("Properties file path can not be null" + file.getPath());
        }
        InputStream inputStream = null;
        Properties p =null;
        try {
            inputStream = new FileInputStream(file);
            p = new Properties();
            p.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream){
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return p;
    }

}
