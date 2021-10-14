package blxt.qjava.utils.network.bean;

import java.util.List;

/**
 * 网络连接bean
 * @author OpenJialei
 * @date 2021年10月14日 14:04
 */
public class NetworkBean {

    private static String NEW_LINE = "\r\n";   /**
     * 网络名称
     */
    private String name;
    /**
     * IPv4 地址
     */
    private String ipv4Addr;
    /**
     * IPv6 地址
     */
    private String ipv6Addr;
    /**
     * 临时 IPv6 地址
     */
    private String ipv6AddrTmp;
    /**
     * 本地链接 IPv6 地址
     */
    private String ipv6AddrLocal;
    /**
     * 获得租约的时间
     */
    private String leaseGetTime;
    /**
     * 租约过期的时间
     */
    private String leaseTimeout;
    /**
     * 描述
     */
    private String description;
    /**
     * 物理地址
     */
    private String mac;
    /**
     * DHCP 已启用
     */
    private String dhcpInvoc;
    /**
     * 连接特定的 DNS 后缀
     */
    private String specialDnsSuffix;
    /**
     * 自动配置已启用
     */
    private String autoConfigInvoc;
    /**
     * 子网掩码
     */
    private String subnetMask;
    /**
     * 默认网关
     */
    private List<String> defaultGateway;
    /**
     * DHCPv6 IAID
     */
    private String dhcpV6IAID;
    /**
     * DHCPv6 客户端 DUID
     */
    private String dhcpV6ClientDUID;
    /**
     * DNS 服务器
     */
    private List<String> dnsServer;
    /**
     * DHCP 服务器
     */
    private List<String> dhcpServer;
    /**
     * TCPIP 上的 NetBIOS
     */
    private String tcpipNetBIOS;
    /**
     * 媒体状态
     */
    private String mediaStatus;

    /**
     * 主WINS服务器
     */
    private String primaryWINSServer;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name.replace("以太网适配器", "");
        name = name.replace("Ethernet adapter", "");
        name = name.replace("无线局域网适配器", "");
        name = name.replace("WLAN adapter", "");
        name = name.replace("隧道适配器", "");
        name = name.replace("Tunnel adapter", "");
        this.name = name;
    }

    public String getPrimaryWINSServer() {
        return primaryWINSServer;
    }
    public void setPrimaryWINSServer(String primaryWINSServer) {
        this.primaryWINSServer = primaryWINSServer;
    }
    public String getIpv4Addr() {
        return ipv4Addr;
    }
    public void setIpv4Addr(String ipv4Addr) {
        this.ipv4Addr = ipv4Addr;
    }
    public String getIpv6Addr() {
        return ipv6Addr;
    }
    public void setIpv6Addr(String ipv6Addr) {
        this.ipv6Addr = ipv6Addr;
    }
    public String getIpv6AddrTmp() {
        return ipv6AddrTmp;
    }
    public void setIpv6AddrTmp(String ipv6AddrTmp) {
        this.ipv6AddrTmp = ipv6AddrTmp;
    }
    public String getIpv6AddrLocal() {
        return ipv6AddrLocal;
    }
    public void setIpv6AddrLocal(String ipv6AddrLocal) {
        this.ipv6AddrLocal = ipv6AddrLocal;
    }
    public String getLeaseGetTime() {
        return leaseGetTime;
    }
    public void setLeaseGetTime(String leaseGetTime) {
        this.leaseGetTime = leaseGetTime;
    }
    public String getLeaseTimeout() {
        return leaseTimeout;
    }
    public void setLeaseTimeout(String leaseTimeout) {
        this.leaseTimeout = leaseTimeout;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getDhcpInvoc() {
        return dhcpInvoc;
    }
    public void setDhcpInvoc(String dhcpInvoc) {
        this.dhcpInvoc = dhcpInvoc;
    }
    public String getSpecialDnsSuffix() {
        return specialDnsSuffix;
    }
    public void setSpecialDnsSuffix(String specialDnsSuffix) {
        this.specialDnsSuffix = specialDnsSuffix;
    }
    public String getAutoConfigInvoc() {
        return autoConfigInvoc;
    }
    public void setAutoConfigInvoc(String autoConfigInvoc) {
        this.autoConfigInvoc = autoConfigInvoc;
    }
    public String getSubnetMask() {
        return subnetMask;
    }
    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }
    public List<String> getDefaultGateway() {
        return defaultGateway;
    }
    public void setDefaultGateway(List<String> defaultGateway) {
        this.defaultGateway = defaultGateway;
    }
    public String getDhcpV6IAID() {
        return dhcpV6IAID;
    }
    public void setDhcpV6IAID(String dhcpV6IAID) {
        this.dhcpV6IAID = dhcpV6IAID;
    }
    public String getDhcpV6ClientDUID() {
        return dhcpV6ClientDUID;
    }
    public void setDhcpV6ClientDUID(String dhcpV6ClientDUID) {
        this.dhcpV6ClientDUID = dhcpV6ClientDUID;
    }
    public List<String> getDnsServer() {
        return dnsServer;
    }
    public void setDnsServer(List<String> dnsServer) {
        this.dnsServer = dnsServer;
    }
    public List<String> getDhcpServer() {
        return dhcpServer;
    }
    public void setDhcpServer(List<String> dhcpServer) {
        this.dhcpServer = dhcpServer;
    }
    public String getTcpipNetBIOS() {
        return tcpipNetBIOS;
    }
    public void setTcpipNetBIOS(String tcpipNetBIOS) {
        this.tcpipNetBIOS = tcpipNetBIOS;
    }
    public String getMediaStatus() {
        return mediaStatus;
    }
    public void setMediaStatus(String mediaStatus) {
        this.mediaStatus = mediaStatus;
    }
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if(this.name != null) {
            sb.append("网络名称:\t\t").append(this.name).append(NEW_LINE);
        }
        if(this.mediaStatus != null) {
            sb.append("媒体状态:\t\t").append(this.mediaStatus).append(NEW_LINE);
        }
        if(this.specialDnsSuffix != null) {
            sb.append("连接特定的 DNS 后缀:\t\t").append(this.specialDnsSuffix).append(NEW_LINE);
        }
        if(this.description != null) {
            sb.append("描述:\t\t\t").append(this.description).append(NEW_LINE);
        }
        if(this.mac != null) {
            sb.append("物理地址:\t\t").append(this.mac).append(NEW_LINE);
        }
        if(this.dhcpInvoc != null) {
            sb.append("DHCP 已启用:\t\t").append(this.dhcpInvoc).append(NEW_LINE);
        }
        if(this.autoConfigInvoc != null) {
            sb.append("自动配置已启用:\t\t").append(this.autoConfigInvoc).append(NEW_LINE);
        }
        if(this.ipv6Addr != null) {
            sb.append("IPv6 地址:\t\t").append(this.ipv6Addr).append(NEW_LINE);
        }
        if(this.ipv6AddrTmp != null) {
            sb.append("临时 IPv6 地址:\t\t").append(this.ipv6AddrTmp).append(NEW_LINE);
        }
        if(this.ipv6AddrLocal != null) {
            sb.append("本地链接 IPv6 地址:\t").append(this.ipv6AddrLocal).append(NEW_LINE);
        }
        if(this.ipv4Addr != null) {
            sb.append("IPv4 地址:\t\t").append(this.ipv4Addr).append(NEW_LINE);
        }
        if(this.subnetMask != null) {
            sb.append("子网掩码:\t\t").append(this.subnetMask).append(NEW_LINE);
        }
        if(this.leaseGetTime != null) {
            sb.append("获得租约的时间:\t\t").append(this.leaseGetTime).append(NEW_LINE);
        }
        if(this.leaseTimeout != null) {
            sb.append("租约过期的时间:\t\t").append(this.leaseTimeout).append(NEW_LINE);
        }
        if(this.defaultGateway != null) {
            sb.append("默认网关:\t\t");
            for(int i = 0, size = this.defaultGateway.size(); i < size; i++) {
                if(i > 0) {
                    sb.append("\t\t\t").append(this.defaultGateway.get(i)).append(NEW_LINE);
                } else {
                    sb.append(this.defaultGateway.get(i)).append(NEW_LINE);
                }
            }
        }
        if(this.dhcpV6IAID != null) {
            sb.append("DHCPv6 IAID:\t\t").append(this.dhcpV6IAID).append(NEW_LINE);
        }
        if(this.dhcpV6ClientDUID != null) {
            sb.append("DHCPv6 客户端 DUID:\t").append(this.dhcpV6ClientDUID).append(NEW_LINE);
        }
        if(this.dhcpServer != null) {
            sb.append("DHCP 服务器:\t\t");
            for(int i = 0, size = this.dhcpServer.size(); i < size; i++) {
                if(i > 0) {
                    sb.append("\t\t\t").append(this.dhcpServer.get(i)).append(NEW_LINE);
                } else {
                    sb.append(this.dhcpServer.get(i)).append(NEW_LINE);
                }
            }
        }
        if(this.dnsServer != null) {
            sb.append("DNS 服务器:\t\t");
            for(int i = 0, size = this.dnsServer.size(); i < size; i++) {
                if(i > 0) {
                    sb.append("\t\t\t").append(this.dnsServer.get(i)).append(NEW_LINE);
                } else {
                    sb.append(this.dnsServer.get(i)).append(NEW_LINE);
                }
            }
        }
        if(this.primaryWINSServer != null) {
            sb.append("主 WINS 服务器:\t\t").append(this.primaryWINSServer).append(NEW_LINE);
        }
        if(this.tcpipNetBIOS != null) {
            sb.append("TCPIP 上的 NetBIOS:\t").append(this.tcpipNetBIOS).append(NEW_LINE);
        }
        return sb.toString();
    }
}
