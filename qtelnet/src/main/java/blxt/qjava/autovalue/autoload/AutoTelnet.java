package blxt.qjava.autovalue.autoload;

import blxt.qjava.autovalue.inter.TelnetMark;
import blxt.qjava.autovalue.inter.autoload.AutoLoadFactory;
import blxt.qjava.autovalue.reflect.PackageUtil;
import blxt.qjava.autovalue.util.ObjectPool;
import blxt.qjava.autovalue.util.ObjectValue;
import blxt.qjava.qtelnet.QTelnetClient;


@AutoLoadFactory(name="AutoFtpServer", annotation = TelnetMark.class, priority = 20)
public class AutoTelnet extends AutoLoadBase {

    @Override
    public <T>T inject(Class<?> object) throws Exception {

        TelnetMark annotation = object.getAnnotation(TelnetMark.class);
        if(annotation == null){
            return null;
        }

        QTelnetClient bean = ObjectPool.getObject(object);
        String host = annotation.hostIP();
        int port = annotation.port();
        String uname = annotation.uname();
        String pwd = annotation.upwd();
        String prompt = annotation.prompt();
        String termtype = annotation.termtype();
        String ORIG_CODEC = annotation.ORIG_CODEC();
        String TRANSLATE_CODEC = annotation.TRANSLATE_CODEC();

        if(host.startsWith("$")){
            host = ObjectValue.getObjectValue(bean, host, String.class);
        }
        if(uname.startsWith("$")){
            uname = ObjectValue.getObjectValue(bean, uname, String.class);
        }
        if(pwd.startsWith("$")){
            pwd = ObjectValue.getObjectValue(bean, pwd, String.class);
        }
        if(termtype.startsWith("$")){
            termtype = ObjectValue.getObjectValue(bean, termtype, String.class);
        }
        if(ORIG_CODEC.startsWith("$")){
            ORIG_CODEC = ObjectValue.getObjectValue(bean, ORIG_CODEC, String.class);
        }
        if(TRANSLATE_CODEC.startsWith("$")){
            TRANSLATE_CODEC = ObjectValue.getObjectValue(bean, TRANSLATE_CODEC, String.class);
        }
        if(ORIG_CODEC.equals(TRANSLATE_CODEC)){
            bean.setChangeCode(false);
        }
        else{
            bean.setChangeCode(true);
        }

        bean.setHostIp(host);
        bean.setPort(port);
        bean.setUsername(uname);
        bean.setPassword(pwd);
        bean.setTermtype(termtype);
        bean.setPrompt(prompt);
        bean.setORIG_CODEC(ORIG_CODEC);
        bean.setTRANSLATE_CODEC(TRANSLATE_CODEC);

        if(PackageUtil.isInterfaces(object, QTelnetClient.OnTelnetClientListener.class)){
            bean.setOnTelnetClientListener((QTelnetClient.OnTelnetClientListener)bean);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(bean.connect()){
                    bean.login();
                }
            }
        }).start();

        return (T) bean;
    }

}
