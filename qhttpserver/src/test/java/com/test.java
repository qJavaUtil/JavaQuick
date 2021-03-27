package com;

import blxt.qjava.autovalue.QJavaApplication;
import blxt.qjava.autovalue.autoload.AutoLoadBase;
import blxt.qjava.autovalue.inter.EnHttpServer;

@EnHttpServer()
public class test {


    public static void main(String[] args) throws Exception {

        AutoLoadBase.falSetAccessible = true;
        QJavaApplication.run(test.class);
        System.out.println("加载完成");
    }
    String temp = "<form name='form1' method='post' action='null'><input type='hidden' name='Retdesc' value='parameters error'></form><script>document.form1.submit();</script>";
//        String reg = "name\\s*\\=\\s*[\\w\'\"&&[^\\>]]+|value\\s*\\=\\s*[\\w\'\"\\s&&[^\\>]]+";
//        Pattern pattern = Pattern.compile (reg);
//        Matcher matcher = pattern.matcher (temp);
//        while (matcher.find ())
//        {
//            System.out.println (matcher.group ());
//        }
}
