package blxt.qjava.autovalue.autoload;


import blxt.qjava.autovalue.inter.EnFtpServer;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.qftp.MFtpServerFactory;
import blxt.qjava.qftp.QFTPClient;
import blxt.qjava.utils.PropertiesTools;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.usermanager.impl.BaseUser;

@AutoLoadFactory(name="AutoFtpServer", annotation = EnFtpServer.class, priority = 20)
public class AutoFtpServer extends AutoLoadBase{
    final String FTP_PORT_KEY = "ftp.port";
    final String FTP_USER_KEY = "ftp.user";
    final String FTP_PWD_KEY = "ftp.userpwd";
    final String FTP_PATH_KEY = "ftp.userpath";
    final String FTP_WITRE_KEY = "ftp.isWrite";
    final String FTP_LISTENER_KEY = "ftp.listener";

    @Override
    public Object  inject(Class<?> object) throws Exception {

        EnFtpServer anno = object.getAnnotation(EnFtpServer.class);

        int port = anno.port();
        String username = "";
        String userpwd = "";
        String userhomepath = "";
        boolean isWrite = false;

        if(!AutoValue.isNull(FTP_PORT_KEY)){
            port = (int) AutoValue.getPropertiesValue(FTP_PORT_KEY, int.class);
        }
        if(!AutoValue.isNull(FTP_USER_KEY)){
            username = (String) AutoValue.getPropertiesValue(FTP_USER_KEY, String.class);
        }
        if(!AutoValue.isNull(FTP_PWD_KEY)){
            userpwd = (String) AutoValue.getPropertiesValue(FTP_PWD_KEY, String.class);
        }
        if(!AutoValue.isNull(FTP_PATH_KEY)){
            userhomepath = (String) AutoValue.getPropertiesValue(FTP_PATH_KEY, String.class);
        }
        if(!AutoValue.isNull(FTP_WITRE_KEY)){
            isWrite = (boolean) AutoValue.getPropertiesValue(FTP_WITRE_KEY, boolean.class);
        }


        MFtpServerFactory ftpServerFactory = MFtpServerFactory.getInstance(port);

        // 添加默认监听
        if(!AutoValue.isNull(FTP_LISTENER_KEY)){
            String listenerClass = (String) AutoValue.getPropertiesValue(FTP_LISTENER_KEY, String.class);
            Class objClass = Class.forName(listenerClass);
            if(!PackageUtil.isInterfaces(objClass, Listener.class)){
                throw new Exception( listenerClass + "需要实现org.apache.ftpserver.listener.Listener接口。");
            }
            Listener  listener = (Listener)ObjectPool.getObject(objClass);
            ftpServerFactory.addListener(listenerClass, listener);
        }

        // 添加默认用户
        if(username != null ){
            BaseUser user = ftpServerFactory.creatUser(username, userpwd, userhomepath, isWrite);
            ftpServerFactory.addUser(user);
        }

        return null;
    }

}
