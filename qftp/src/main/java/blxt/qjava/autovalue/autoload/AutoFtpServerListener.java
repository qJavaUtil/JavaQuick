package blxt.qjava.autovalue.autoload;


import blxt.qjava.autovalue.inter.OnFtpServerListener;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.qftp.MFtpServerFactory;
import blxt.qjava.utils.check.CheckUtils;
import org.apache.ftpserver.listener.Listener;

/**
 * OnFtpServerListener
 */
@AutoLoadFactory(name="AutoFtpServerListener", annotation = OnFtpServerListener.class, priority = 30)
public class AutoFtpServerListener extends AutoLoadBase{

    @Override
    public <T>T inject(Class<?> object) throws Exception {
        if(!PackageUtil.isInterfaces(object, Listener.class)){
            throw new Exception("OnFtpServerListener需要实现org.apache.ftpserver.listener.Listener接口。");
        }

        MFtpServerFactory ftpServerFactory = MFtpServerFactory.getInstance();
        CheckUtils.objectCheckNull(ftpServerFactory, "需要启动ftp服务，添加@EnFtpServer注解","0001", null);

        Listener listener = (Listener)ObjectPool.getObject(object);
        ftpServerFactory.addListener(object.getName(), listener);
        return null;
    }

}
