package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.SshMark;
import blxt.qjava.autovalue.inter.TelnetMark;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.ObjectValue;
import blxt.qjava.qssh.QSSH;
import blxt.qjava.qtelnet.QTelnetClient;


@AutoLoadFactory(name="AutoFtpServer", annotation = SshMark.class, priority = 20)
public class AutoSshCli extends AutoLoadBase {

    @Override
    public <T>T inject(Class<?> object) throws Exception {

        SshMark annotation = object.getAnnotation(SshMark.class);
        if(annotation == null){
            return null;
        }

        QSSH bean = ObjectPool.getObject(object);
        String host = annotation.hostIP();
        int port = annotation.port();
        String uname = annotation.uname();
        String pwd = annotation.upwd();
        String CODEC = annotation.CODEC();

        if(host.startsWith("$")){
            host = ObjectValue.getObjectValue(bean, host, String.class);
        }
        if(uname.startsWith("$")){
            uname = ObjectValue.getObjectValue(bean, uname, String.class);
        }
        if(pwd.startsWith("$")){
            pwd = ObjectValue.getObjectValue(bean, pwd, String.class);
        }

        if(CODEC.startsWith("$")){
            CODEC = ObjectValue.getObjectValue(bean, CODEC, String.class);
        }


        bean.setHost(host);
        bean.setPort(port);
        bean.setUser(uname);
        bean.setPassword(pwd);
        bean.setEncoding(CODEC);

        if(PackageUtil.isInterfaces(object, QSSH.OnTelnetClientListener.class)){
            bean.setOnTelnetClientListener((QSSH.OnTelnetClientListener)bean);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(bean.connect()){
                    bean.connect();
                }
            }
        }).start();

        return (T) bean;
    }

}
