package blxt.qjava.qliveness;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 用户设备信息
 */
public class UserClientInfoFactory {

    public static Map<String, String> creadUserClientInfo() {
        Map<String, String> map = new HashMap<>();
        try {
            InetAddress addr = InetAddress.getLocalHost();
            map.put("hostname", addr.getHostName());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        map.put("ip", getIpAddress().toString());

        return map;
    }

    public static List<String> getIpAddress() {
        List<String> ipAddress = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                // 用于排除回送接口,非虚拟网卡,未在使用中的网络接口
                if (netInterface.isLoopback()
                        || netInterface.isVirtual()
                        || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            ipAddress.add(ip.getHostAddress());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.toString());
        }
        return ipAddress;
    }

}
