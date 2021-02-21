import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import blxt.qjava.qxml.QElement;
import blxt.qjava.qxml.unit.QXmlTools;

public class XmlMain2 {

	public static void main(String[] args) {
//		File file = new File("E:\\MyComputer\\Desktop\\books - 副本.txt");
		String mainContent = "";
 
	    mainContent = getUrlString("https://www.17k.com/chapter/2722533/33762471.html", "utf-8");
//	    System.out.println(mainContent);
	    
	    QElement html = QXmlTools.init(mainContent);
	  
	    html = html.getSubElement().get(0).getNodeByLable("head");
	    System.out.println(html.toString());
	 //   QElement content = html.getSubElement().get(0);
	 //   QLog.i("结果" , content.res.toString());
	}
	
	public static String getUrlString(String urlStr, String code) {
		String strRes = "";
		try {
            URL url = new URL(urlStr);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //System.err.println("成功");   
                InputStream in = httpConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(in, code);
                BufferedReader bufr = new BufferedReader(isr);
                String str;
                while ((str = bufr.readLine()) != null) {
                	strRes += str + "\r\n";
                }
                bufr.close();
            } else {
                System.err.println("失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return strRes;
	}

	 @SuppressWarnings("resource")
	public static String readFile(File file, String code)
	  {
		 BufferedReader br = null;
		try {
			br = new BufferedReader(
					 new InputStreamReader(new FileInputStream(file), code));
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}

		 String strRes = "";
		 
		 String line=null;
		         try {
					while((line=br.readLine())!=null){
						 strRes += line + "\r\n";
					 }
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
	    return strRes;
	  }

}
