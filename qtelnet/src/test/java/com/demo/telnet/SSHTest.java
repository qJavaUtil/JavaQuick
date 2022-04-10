package com.demo.telnet;

import blxt.qjava.qssh.QSSH;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.*;

/**
 * @author OpenJialei
 * @date 2022年04月09日 19:37
 */
public class SSHTest {

    private static String DEFAULTCHART = "UTF-8";

    public static Connection login(String ip, String username, String password) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = new Connection(ip);
            connection.connect();// 连接
            flag = connection.authenticateWithPassword(username, password);// 认证
            if (flag) {
                session = connection.openSession();// 打开一个会话
                System.out.println("================登录成功==================");
                return connection;
            }
        } catch (IOException e) {
            System.out.println("=========登录失败=========" + e);
            connection.close();
        }
        return connection;
    }

    /**
     * 远程执行shll脚本或者命令
     *
     * @param cmd
     *            即将执行的命令
     * @return 命令执行完后返回的结果值
     */
    static Session session;
    public static String execmd(Connection connection, String cmd) {
        String result = "";
        try{
            if (connection != null) {
                if(session == null){
                    session = connection.openSession();// 打开一个会话
                }
                session.execCommand(cmd);// 执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                System.out.println(result);
                // 如果为得到标准输出为空，说明脚本执行出错了
                if (result == null || result.isEmpty()) {
                    System.out.println("得到标准输出为空,链接conn:" + connection + ",执行的命令：" + cmd);
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                } else {
                    System.out.println("执行命令成功,链接conn:" + connection + ",执行的命令：" + cmd);
                }
               // connection.close();
               // session.close();
            }
        } catch (IOException e) {
            System.out.println("执行命令失败,链接conn:" + connection + ",执行的命令：" + cmd + "  " + e);
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in
     *            输入流对象
     * @param charset
     *            编码
     * @return 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
                System.out.println(line);
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            System.out.println("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void main(String[] args) throws IOException {
        QSSH qssh = new QSSH();
        qssh.setHost("192.168.10.198");
        qssh.setUserName("root");
        qssh.setPassword("nm,.qwer");

//        qssh.connect();
//        System.out.println("ls: " + qssh.exeCommand("ls"));
//        System.out.println("cd: " +qssh.exeCommand("cd /"));
//        System.out.println("ls: " +qssh.exeCommand("ls"));

        String ip = "192.168.10.198";//change your ip
        String username = "root";//user
        String password = "nm,.qwer";//your server password

        Connection connection = login(ip, username, password);

        session.execCommand("cd /");
        System.out.println("执行结果 "+ execmd(connection, "ls"));

    }
}