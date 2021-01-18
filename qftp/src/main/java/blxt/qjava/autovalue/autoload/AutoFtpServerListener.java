package blxt.qjava.autovalue.autoload;


import blxt.qjava.autovalue.inter.EnFtpServer;
import blxt.qjava.autovalue.inter.OnFtpServerListener;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.qftp.MFtpServerFactory;
import org.apache.ftpserver.listener.Listener;

/**
 * OnFtpServerListener
 */
@AutoLoadFactory(name="AutoFtpServerListener", annotation = OnFtpServerListener.class, priority = 30)
public class AutoFtpServerListener extends AutoLoadBase{


    @Override
    public Object inject(Class<?> object) throws Exception {

        if(!PackageUtil.isInterfaces(object, Listener.class)){
            throw new Exception("OnFtpServerListener需要实现org.apache.ftpserver.listener.Listener接口。");
        }

        Listener listener = (Listener)ObjectPool.getObject(object);

        MFtpServerFactory ftpServerFactory = MFtpServerFactory.getInstance();
        ftpServerFactory.addListener(object.getName(), listener);

        return null;
    }

}
