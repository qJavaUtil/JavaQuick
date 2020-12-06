package blxt.qjava.utils.network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/11/2 11:14
 */
public class IPTools {

    final static int IP_TYPE_V4 = 1;
    final static int IP_TYPE_V6 = 2;
    final static int IP_TYPE_ALL = 3;

    /**
     * 获取本地IP
     * @return 主机ip
     */
    public static String getLocalHostIp(){
        InetAddress ia=null;
        try {
            ia= InetAddress.getLocalHost();

            return ia.getHostAddress();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取本地主机名
     * @return 主机名
     */
    public static String getLocalHostName(){
        InetAddress ia=null;
        try {
            ia= InetAddress.getLocalHost();
            return ia.getHostName();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return "null";
    }

    /**
     * 获取ipv4ip集合
     * @return
     */
    public static List<String> getLocalHostIpsIPv4(){
        return getLocalHostIps(IP_TYPE_V4);
    }

    /**
     * 获取ipv6ip集合
     * @return
     */
    public static List<String> getLocalHostIpsIPv6(){
        return getLocalHostIps(IP_TYPE_V6);
    }

    /**
     * 获取本地所有Ip,ipv4和ipv6
     * @return ip列表
     */
    public static List<String> getLocalHostIps(){
        return getLocalHostIps(IP_TYPE_ALL);
    }

    /**
     * 获取本地所有Ip,ipv4和ipv6
     * @return ip列表
     */
    private static List<String> getLocalHostIps(int type) {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip = null;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if(inetAddress == null){
                        break;
                    }

                    ip = null;
                    if(IP_TYPE_V4 == type){
                        if (inetAddress instanceof Inet4Address) {
                            ip = inetAddress.getHostAddress();
                        }
                    }
                    else if(IP_TYPE_V6 == type){
                        if (inetAddress instanceof Inet6Address) {
                            ip = inetAddress.getHostAddress();
                        }
                    }
                    else{        /* IPV4 和 ipv6*/
                        if (inetAddress instanceof Inet4Address || inetAddress instanceof Inet6Address) {
                            ip = inetAddress.getHostAddress();
                        }
                    }

                    if(ip == null || "127.0.0.1".equals(ip)){
                        break;
                    }

                    ipList.add(ip);

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }


    /**
     * 判断远端主机ip是否存在
     * @param ip       ip
     * @param outTime  超时
     * @return
     */
    public static boolean checkHostIP(String ip, int outTime){
        try {
            return InetAddress.getByName(ip).isReachable(3000);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 判断ip端口是否开放,Socket原理
     * @param hostIp   主机
     * @param port	   端口
     * @param outTime  超时时间 ms
     * @return
     */
    public static boolean checkHostIP(String hostIp, int port, int outTime) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(hostIp, port), outTime);
        } catch (IOException e) {
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
        return true;
    }


}
