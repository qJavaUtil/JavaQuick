package blxt.qjava.utils.network;

import blxt.qjava.utils.check.CheckUtils;
import blxt.qjava.utils.network.bean.NetworkBasic;
import blxt.qjava.utils.network.bean.NetworkBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 网络解析器, 仅限于 Windows
 * @author OpenJialei
 * @date 2021年10月14日 14:07
 */
public class WindowsNetworkAnalysis {

    private static String IPCONFIG_ALL = "cmd /c ipconfig /allcompartments /all";
    private static String CLN = ":";

    private NetworkBasic netBasic = null;

    public NetworkBasic build() {
        boolean mainConfig = false;
        String value = null, label = null;
        List<String> list = this.execReturnRet(IPCONFIG_ALL);
        String beanName = null;
        for(String el : list) {
            el = el.trim();
            if(CheckUtils.isEmpty(el)) {
                continue;
            }
            if("Windows IP 配置".equals(el) || "Windows IP Configuration".equals(el)) {
                mainConfig = true;
                netBasic = new NetworkBasic();
                continue;
            }
            if((el.startsWith("以太网适配器") || el.startsWith("Ethernet adapter")
                || el.startsWith("无线局域网适配器") || el.startsWith("WLAN adapter")
                || el.startsWith("隧道适配器") || el.startsWith("Tunnel adapter")) && el.endsWith(CLN)) {
                mainConfig = false;
                beanName = el.replace(CLN, "");
                NetworkBean bean = new NetworkBean();
                bean.setName(beanName);
                netBasic.addNetworkBeah(beanName, bean);
                continue;
            }
            if(el.indexOf(CLN) > 0 && el.indexOf(".") > 0) {
                label = el.substring(0, el.indexOf(CLN));
                value = el.substring(el.indexOf(CLN) + 1).trim();
            } else {
                value = el;
                el = label + CLN + el;
            }
            if(mainConfig) {
                if(el.startsWith("主机名") || el.startsWith("Host Name")) {
                    netBasic.setHostName(value);
                } else if(el.startsWith("主 DNS 后缀") || el.startsWith("Primary Dns Suffix")) {
                    netBasic.setMainDnsSuf(value);
                } else if(el.startsWith("节点类型") || el.startsWith("Node Type")) {
                    netBasic.setNodeType(value);
                } else if(el.startsWith("IP 路由已启用") || el.startsWith("IP Routing Enabled")) {
                    netBasic.setIpRouteInvocation(value);
                } else if(el.startsWith("WINS 代理已启用")  || el.startsWith("WINS Proxy Enabled")) {
                    netBasic.setWinsAgentInvocation(value);
                } else if(el.startsWith("DNS 后缀搜索列表") || el.startsWith("DNS Suffix Search List")) {
                    netBasic.setDnsSufSerchList(value);
                }
                continue;
            }

            if(!mainConfig) {
                if(el.startsWith("连接特定的 DNS 后缀") || el.startsWith("Connection-specific DNS Suffix")) {
                    netBasic.getNetworkBeah(beanName).setSpecialDnsSuffix(value);
                } else if(el.startsWith("描述") || el.startsWith("Description")) {
                    netBasic.getNetworkBeah(beanName).setDescription(value);
                } else if(el.startsWith("物理地址") || el.startsWith("Physical Address")) {
                    netBasic.getNetworkBeah(beanName).setMac(value);
                } else if(el.startsWith("DHCP 已启用") || el.startsWith("DHCP Enabled")) {
                    netBasic.getNetworkBeah(beanName).setDhcpInvoc(value);
                } else if(el.startsWith("自动配置已启用") || el.startsWith("Autoconfiguration Enabled")) {
                    netBasic.getNetworkBeah(beanName).setAutoConfigInvoc(value);
                } else if(el.startsWith("IPv6 地址")) {
                    netBasic.getNetworkBeah(beanName).setIpv6Addr(value);
                } else if(el.startsWith("临时 IPv6 地址")) {
                    netBasic.getNetworkBeah(beanName).setIpv6AddrTmp(value);
                } else if(el.startsWith("本地链接 IPv6 地址") || el.startsWith("Link-local IPv6 Address")) {
                    netBasic.getNetworkBeah(beanName).setIpv6AddrLocal(value);
                } else if(el.startsWith("IPv4 地址") || el.startsWith("IPv4 Address")) {
                    netBasic.getNetworkBeah(beanName).setIpv4Addr(value);
                } else if(el.startsWith("子网掩码") || el.startsWith("Subnet Mask")) {
                    netBasic.getNetworkBeah(beanName).setSubnetMask(value);
                } else if(el.startsWith("获得租约的时间") || el.startsWith("Lease Obtained")) {
                    netBasic.getNetworkBeah(beanName).setLeaseGetTime(value);
                } else if(el.startsWith("租约过期的时间") || el.startsWith("Lease Expires")) {
                    netBasic.getNetworkBeah(beanName).setLeaseTimeout(value);
                } else if(el.startsWith("默认网关") || el.startsWith("Default Gateway")) {
                    netBasic.getNetworkBeah(beanName).addDefaultGateway(value);
                } else if(el.startsWith("DHCP 服务器") || el.startsWith("DHCP Server")) {
                    netBasic.getNetworkBeah(beanName).addDhcpServer(value);
                } else if(el.startsWith("DNS 服务器") || el.startsWith("DNS Servers")) {
                    netBasic.getNetworkBeah(beanName).addDnsServer(value);
                } else if(el.startsWith("TCPIP 上的 NetBIOS")) {
                    netBasic.getNetworkBeah(beanName).setTcpipNetBIOS(value);
                } else if(el.startsWith("媒体状态") || el.startsWith("Media State")) {
                    netBasic.getNetworkBeah(beanName).setMediaStatus(value);
                } else if(el.startsWith("DHCPv6 IAID")) {
                    netBasic.getNetworkBeah(beanName).setDhcpV6IAID(value);
                } else if(el.startsWith("DHCPv6 客户端 DUID")) {
                    netBasic.getNetworkBeah(beanName).setDhcpV6ClientDUID(value);
                } else if(el.startsWith("主 WINS 服务器") || el.startsWith("Primary WINS Server")) {
                    netBasic.getNetworkBeah(beanName).setPrimaryWINSServer(value);
                }
                continue;
            }

        }

        return netBasic;
    }

    public NetworkBasic getNetBasic() {
        return netBasic;
    }

    /**
     * 执行dos命令，需要使用返回结果
     * @return 获取返回结果
     */
    public List<String> execReturnRet(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader normal = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            List<String> normalList = getOutputReturnList(normal);
            List<String> errorList = getOutputReturnList(error);
            int exitVal = process.waitFor();
            if (exitVal != 0) {
                System.out.println("执行的DOS命令，错误信息如下：");
            }
            if(normalList.size() > 0) {
                return normalList;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取流信息并转为List返回
     * @param input
     * @return
     * @throws IOException
     */
    private List<String> getOutputReturnList(BufferedReader input) throws IOException {
        List<String> list = new LinkedList<String>();
        String line = null;
        while ((line = input.readLine()) != null) {
            if (!"null".equals(line)) {
                list.add(line + "\r\n");
            }
        }
        return list;
    }
}

