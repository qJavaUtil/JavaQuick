package blxt.qjava.qftp;

import blxt.qjava.utils.BitUtils;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;

/**
 * FtpServerFactory
 */
public class MFtpServerFactory {

    static MFtpServerFactory instance = null;

    FtpServer server;
    FtpServerFactory serverFactory ;

    public static MFtpServerFactory getInstance(int port){
        if(instance == null){
            instance = new MFtpServerFactory(port);
        }
        return instance;
    }

    public static MFtpServerFactory getInstance() {
        return instance;
    }


    protected MFtpServerFactory(int port) {
        serverFactory = new FtpServerFactory();
       // this.port = port;
        ListenerFactory factory = new ListenerFactory();
        //设置服务端口
        factory.setPort(port);
        // 添加一个默认监听
        serverFactory.addListener("default", factory.createListener());
        /**
         * 也可以使用配置文件来管理用户
         */
//	    PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
//	    userManagerFactory.setFile(new File("users.properties"));
//	    serverFactory.setUserManager(userManagerFactory.createUserManager());
        try {
            start();
        } catch (FtpException e) {
            e.printStackTrace();
        }
    }

    public void start() throws FtpException {
        if(server == null){
            server = serverFactory.createServer();
            server.start();
        }
    }

    public void stop(){
        if(server != null){
            server.stop();
        }
    }

    public boolean isStart(){
        if(server != null){
            server.isStopped();
        }
        return false;
    }

    /**
     * 创建用户
     * @param name
     * @param pwd
     * @param userPath
     * @param isWrite
     * @return
     */
    public BaseUser creatUser(String name, String pwd, String userPath, boolean isWrite){
        //用户名
        BaseUser user = new BaseUser();
        user.setName(name);
        //密码 如果不设置密码就是匿名用户
        user.setPassword(pwd);
        //用户主目录
        user.setHomeDirectory(userPath);

        List<Authority> authorities = new ArrayList<Authority>();
        if (isWrite) {/** 增加写权限  */
            authorities.add(new WritePermission());
        }

        user.setAuthorities(authorities);
        return user;
    }

    /**
     * 增加用户
     * @param user
     * @return
     */
    public boolean addUser(BaseUser user){
        try {
            serverFactory.getUserManager().save(user);
            return true;
        } catch (FtpException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 添加监听
     */
    public MFtpServerFactory addListener(String name, Listener listener){
        serverFactory.addListener(name, listener);
        return this;
    }

}
