package blxt.qjava.utils.network;

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

    private static String IPCONFIG_ALL = "cmd /c ipconfig /all";
    private static String CLN = ":";

    private NetworkBasic netBasic = null;

    public NetworkBasic build() {
        boolean mainConfig = false;
        String value = null, label = null;
        NetworkBean bean = null;
        List<String> list = this.execReturnRet(IPCONFIG_ALL);
        //List<String> list = Arrays.asList(ss);
        for(String el : list) {
            el = el.trim();
            if("".equals(el)) {
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
                bean = new NetworkBean();
                bean.setName(el.replace(CLN, ""));
                netBasic.addNetworkBeah(bean.getName(), bean);
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
                    bean.setSpecialDnsSuffix(value);
                } else if(el.startsWith("描述") || el.startsWith("Description")) {
                    bean.setDescription(value);
                } else if(el.startsWith("物理地址") || el.startsWith("Physical Address")) {
                    bean.setMac(value);
                } else if(el.startsWith("DHCP 已启用") || el.startsWith("Dhcp Enabled")) {
                    bean.setDhcpInvoc(value);
                } else if(el.startsWith("自动配置已启用") || el.startsWith("Autoconfiguration Enabled")) {
                    bean.setAutoConfigInvoc(value);
                } else if(el.startsWith("IPv6 地址")) {
                    bean.setIpv6Addr(value);
                } else if(el.startsWith("临时 IPv6 地址")) {
                    bean.setIpv6AddrTmp(value);
                } else if(el.startsWith("本地链接 IPv6 地址")) {
                    bean.setIpv6AddrLocal(value);
                } else if(el.startsWith("IPv4 地址") || el.startsWith("IP Address")) {
                    bean.setIpv4Addr(value);
                } else if(el.startsWith("子网掩码") || el.startsWith("Subnet Mask")) {
                    bean.setSubnetMask(value);
                } else if(el.startsWith("获得租约的时间") || el.startsWith("Lease Obtained")) {
                    bean.setLeaseGetTime(value);
                } else if(el.startsWith("租约过期的时间") || el.startsWith("Lease Expires")) {
                    bean.setLeaseTimeout(value);
                } else if(el.startsWith("默认网关") || el.startsWith("Default Gateway")) {
                    List<String> defaultGateway = bean.getDefaultGateway();
                    if(defaultGateway == null) {
                        defaultGateway = new ArrayList<String>();
                        bean.setDefaultGateway(defaultGateway);
                    }
                    defaultGateway.add(value);
                } else if(el.startsWith("DHCP 服务器") || el.startsWith("DHCP Server")) {
                    List<String> dhcpServer = bean.getDhcpServer();
                    if(dhcpServer == null) {
                        dhcpServer = new ArrayList<String>();
                        bean.setDhcpServer(dhcpServer);
                    }
                    dhcpServer.add(value);
                } else if(el.startsWith("DNS 服务器") || el.startsWith("DNS Servers")) {
                    List<String> dnsServer = bean.getDnsServer();
                    if(dnsServer == null) {
                        dnsServer = new ArrayList<String>();
                        bean.setDnsServer(dnsServer);
                    }
                    dnsServer.add(value);
                } else if(el.startsWith("TCPIP 上的 NetBIOS")) {
                    bean.setTcpipNetBIOS(value);
                } else if(el.startsWith("媒体状态") || el.startsWith("Media State")) {
                    bean.setMediaStatus(value);
                } else if(el.startsWith("DHCPv6 IAID")) {
                    bean.setDhcpV6IAID(value);
                } else if(el.startsWith("DHCPv6 客户端 DUID")) {
                    bean.setDhcpV6ClientDUID(value);
                } else if(el.startsWith("主 WINS 服务器") || el.startsWith("Primary WINS Server")) {
                    bean.setPrimaryWINSServer(value);
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
        System.out.println("执行的DOS命令：" + cmd);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader normal = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            List<String> normalList = getOutputReturnList(normal);
            List<String> errorList = getOutputReturnList(error);
            System.out.println("正常结果：" + normalList);
            System.out.println("错误结果：" + errorList);
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

