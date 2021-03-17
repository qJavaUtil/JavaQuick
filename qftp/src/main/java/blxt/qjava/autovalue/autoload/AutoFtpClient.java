package blxt.qjava.autovalue.autoload;


import blxt.qjava.autovalue.inter.FtpClientMark;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.ObjectValue;
import blxt.qjava.qftp.QFTPClient;

/**
 * ftpClient注解实现
 */
@AutoLoadFactory(name="AutoFtpServer", annotation = FtpClientMark.class, priority = 20)
public class AutoFtpClient extends AutoLoadBase{

    @Override
    public Object inject(Class<?> object) throws Exception {

        FtpClientMark annotation = object.getAnnotation(FtpClientMark.class);
        if(annotation == null){
            return null;
        }

        QFTPClient bean = (QFTPClient)ObjectPool.getObject(object);
        String host = annotation.hostIp();
        int port = annotation.port();
        String uname = annotation.uname();
        String pwd = annotation.upwd();
        /** 当前录路径 */
        String pathUse = annotation.path();
        String localCharset = annotation.localCharset();
        String serverCharset = annotation.serverCharset();
        boolean isUtf8 = annotation.OTP_UTF8();

        if(host.startsWith("$")){
            host = ObjectValue.getObjectValue(bean, host, String.class);
        }
        if(uname.startsWith("$")){
            uname = ObjectValue.getObjectValue(bean, uname, String.class);
        }
        if(pwd.startsWith("$")){
            pwd = ObjectValue.getObjectValue(bean, pwd, String.class);
        }
        if(pathUse.startsWith("$")){
            pathUse = ObjectValue.getObjectValue(bean, pathUse, String.class);
        }
        if(localCharset.startsWith("$")){
            serverCharset = ObjectValue.getObjectValue(bean, localCharset, String.class);
        }
        if(serverCharset.startsWith("$")){
            serverCharset = ObjectValue.getObjectValue(bean, serverCharset, String.class);
        }

        bean.setHost(host);
        bean.setPort(port);
        bean.setUname(uname);
        bean.setPwd(pwd);
        bean.setLocalCharset(serverCharset);
        bean.setServerCharset(serverCharset);

        if(annotation.OTP_UTF8()){
            bean.setUtf8();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(bean.connect()){
                    bean.login();
                }
            }
        }).start();

        return bean;
    }

}
