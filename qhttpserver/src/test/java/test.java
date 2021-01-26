import blxt.qjava.httpserver.util.HttpTools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {


    public static void main(String[] args) throws Exception {

        String temp = "<form name='form1' method='post' action='null'><input type='hidden' name='Retdesc' value='parameters error'></form><script>document.form1.submit();</script>";
        String reg = "name\\s*\\=\\s*[\\w\'\"&&[^\\>]]+|value\\s*\\=\\s*[\\w\'\"\\s&&[^\\>]]+";
        Pattern pattern = Pattern.compile (reg);
        Matcher matcher = pattern.matcher (temp);
        while (matcher.find ())
        {
            System.out.println (matcher.group ());
        }

//       String res = HttpTools.sendPost("http://192.168.1.113:8080/test/hello3?name=123", "133");
//
//       System.out.println(res);
    }

}
