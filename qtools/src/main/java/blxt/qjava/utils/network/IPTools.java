package blxt.qjava.utils.network;

import blxt.qjava.utils.check.CheckUtils;
import blxt.qjava.utils.network.bean.NetworkBasic;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/11/2 11:14
 */
public class IPTools {
    static Integer No = 0;
    final static int IP_TYPE_V4 = 1;
    final static int IP_TYPE_V6 = 2;
    final static int IP_TYPE_ALL = 3;

    /**
     * 获取本地IP
     *
     * @return 主机ip
     */
    public static String getLocalHostIp() {
        List<String> ips = getLocalHostIpsIPv4();
        if (CheckUtils.isNotEmpty(ips)) {
            return ips.get(0);
        }
        return null;
    }

    /**
     * 获取适配的ip.
     *
     * @param host 网段ip/mask
     * @return 主机ip
     */
    public static String getLocalHostIp(String host) {
        List<String> ips = getLocalHostIpsIPv4();
        for (String ip : ips) {
            if (isInRange(ip, host)) {
                return ip;
            }
        }
        return ips.get(0);
    }

    /**
     * 获取本地主机名
     *
     * @return 主机名
     */
    public static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "null";
    }

    /**
     * 获取ipv4ip集合
     *
     * @return
     */
    public static List<String> getLocalHostIpsIPv4() {
        return getLocalHostIps(IP_TYPE_V4);
    }

    /**
     * 获取ipv6ip集合
     *
     * @return
     */
    public static List<String> getLocalHostIpsIPv6() {
        return getLocalHostIps(IP_TYPE_V6);
    }

    /**
     * 获取本地所有Ip,ipv4和ipv6
     *
     * @return ip列表
     */
    public static List<String> getLocalHostIps() {
        return getLocalHostIps(IP_TYPE_ALL);
    }

    /**
     * 获取本地所有Ip,ipv4和ipv6
     *
     * @return ip列表
     */
    private static List<String> getLocalHostIps(int type) {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                // 去除回环接口，子接口，未运行和接口
                if (networkInterface.isLoopback()
                        || networkInterface.isVirtual()
                        || !networkInterface.isUp()) {
                    continue;
                }
                // 排除虚拟机
                String name = networkInterface.getDisplayName();
                if (name.contains("VMware") || name.contains("VirtualBox")) {
                    continue;
                }

                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress == null) {
                        break;
                    }

                    String ip = null;
                    if (IP_TYPE_V4 == type) {
                        if (inetAddress instanceof Inet4Address) {
                            ip = inetAddress.getHostAddress();
                        }
                    } else if (IP_TYPE_V6 == type) {
                        if (inetAddress instanceof Inet6Address) {
                            ip = inetAddress.getHostAddress();
                        }
                    } else {        /* IPV4 和 ipv6*/
                        if (inetAddress instanceof Inet4Address || inetAddress instanceof Inet6Address) {
                            ip = inetAddress.getHostAddress();
                        }
                    }
                    if (ip == null) {
                        continue;
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
     *
     * @param ip      ip
     * @param outTime 超时
     * @return
     */
    public static boolean checkHostIP(String ip, int outTime) {
        try {
            return InetAddress.getByName(ip).isReachable(3000);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 判断ip端口是否开放,Socket原理
     *
     * @param hostIp  主机
     * @param port    端口
     * @param outTime 超时时间 ms
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


    /**
     * 获取网络适配器信息, 仅用于 windows
     *
     * @return
     */
    public static NetworkBasic getNetworkBasic() {
        WindowsNetworkAnalysis t = new WindowsNetworkAnalysis();
        return t.build();
    }


    /**
     * 获取空闲端口
     *
     * @param MinPort
     * @param MAXPort
     * @return
     */
    public static Integer getPort(Integer MinPort, Integer MAXPort) {

        Random random = new Random();
        int tempPort = 0;
        int port;
        try {
            while (true) {
                tempPort = random.nextInt(MAXPort) % (MAXPort - MinPort + 1) + MinPort;
                ServerSocket serverSocket = new ServerSocket(tempPort);
                port = serverSocket.getLocalPort();
                serverSocket.close();
                break;
            }
        } catch (Exception e) {
            if (No > 200) {
                throw new RuntimeException("此服务器可开放端口暂时已满！");
            }
            No++;
            port = getPort(MinPort, MAXPort);
        }
        No = 0;
        return port;
    }

    /**
     * 判断某个ip是否在一个网段内.
     *
     * @param ip
     * @param cidr
     * @return
     */
    public static boolean isInRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");
        int ipAddr = (Integer.parseInt(ips[0]) << 24)
                | (Integer.parseInt(ips[1]) << 16)
                | (Integer.parseInt(ips[2]) << 8) | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = 0xFFFFFFFF << (32 - type);
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");
        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);
        return (ipAddr & mask) == (cidrIpAddr & mask);
    }


    public static void main(String[] args) {
        List<String> ips = getLocalHostIpsIPv4();
        for (String s : ips) {
            System.out.println(s);
        }
    }


}
