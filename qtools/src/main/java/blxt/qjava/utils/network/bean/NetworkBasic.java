package blxt.qjava.utils.network.bean;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 网络连接基础信息
 * @author OpenJialei
 * @date 2021年10月14日 14:06
 */
public class NetworkBasic {

    private String hostName;
    private String mainDnsSuf;
    private String nodeType;
    private String ipRouteInvocation;
    private String winsAgentInvocation;
    /**
     * DNS 后缀搜索列表
     */
    private String dnsSufSerchList;
    private Map<String, NetworkBean> netLinks = new LinkedHashMap<String, NetworkBean>();

    private static String NEW_LINE = "\r\n";

    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    public String getMainDnsSuf() {
        return mainDnsSuf;
    }
    public void setMainDnsSuf(String mainDnsSuf) {
        this.mainDnsSuf = mainDnsSuf;
    }
    public String getNodeType() {
        return nodeType;
    }
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
    public String getIpRouteInvocation() {
        return ipRouteInvocation;
    }
    public void setIpRouteInvocation(String ipRouteInvocation) {
        this.ipRouteInvocation = ipRouteInvocation;
    }
    public String getWinsAgentInvocation() {
        return winsAgentInvocation;
    }
    public void setWinsAgentInvocation(String winsAgentInvocation) {
        this.winsAgentInvocation = winsAgentInvocation;
    }

    public void addNetworkBeah(String name, NetworkBean bean) {
        this.netLinks.put(name, bean);
    }

    public String getDnsSufSerchList() {
        return dnsSufSerchList;
    }
    public void setDnsSufSerchList(String dnsSufSerchList) {
        this.dnsSufSerchList = dnsSufSerchList;
    }
    public Map<String, NetworkBean> getNetLinks() {
        return netLinks;
    }
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(NEW_LINE).append("------Windows IP 配置------").append(NEW_LINE);
        sb.append("主机名:\t\t").append(this.hostName).append(NEW_LINE)
            .append("主 DNS 后缀:\t\t").append(this.mainDnsSuf).append(NEW_LINE)
            .append("节点类型:\t\t").append(this.nodeType).append(NEW_LINE)
            .append("IP 路由已启用:\t\t").append(this.ipRouteInvocation).append(NEW_LINE)
            .append("WINS 代理已启用:\t\t").append(this.winsAgentInvocation).append(NEW_LINE);
        if(this.dnsSufSerchList != null) {
            sb.append("DNS 后缀搜索列表:\t\t").append(this.dnsSufSerchList).append(NEW_LINE);
        }
        if(this.netLinks != null && this.netLinks.size() > 0) {
            String key = null;
            for(Iterator<String> it = this.netLinks.keySet().iterator(); it.hasNext();) {
                sb.append(NEW_LINE);
                key = it.next();
                sb.append("------").append(key).append("------").append(NEW_LINE);
                sb.append(this.netLinks.get(key).toString());
            }
        }
        return sb.toString();
    }
}
