import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        String property = "-L\"$(WORKSPACE_demo-lib)/Release\"";
        //懒匹配${}
        String regex = "\\$\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(property);
        while(matcher.find()) {
            //替换匹配内容
            System.out.println(matcher.group() + "->" + matcher.group(1));
        }
    }
}
