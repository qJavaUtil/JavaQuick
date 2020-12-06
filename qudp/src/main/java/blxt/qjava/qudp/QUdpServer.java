package blxt.qjava.qudp;

import java.io.IOException;
import java.net.*;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/10/20 0:47
 */
public class QUdpServer implements Runnable{
    /** 数据包最大长度 */
    private int MAX_LENGTH = 1024;
    /** 连接scoket */
    DatagramSocket socket = null;
    /** 监听回调 */
    QUdpListener listener = null;
    /** 监听端口 */
    int port;
    /** 暂停标志 */
    boolean interrupt = false;


    /**
     *
     * @param port         监听端口
     * @param listener     监听回调
     */
    public QUdpServer(int port, QUdpListener listener) {
        this.port = port;
        this.listener = listener;
    }

    /**
     * 创建
     * @return
     */
    public boolean creat(){
        if(socket != null){
            return false;
        }
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
        
    }

    /**
     * 关闭
     */
    public void close(){
        if(socket == null){
            return;
        }
        socket.close();
        interrupt = true;
    }

    /**
     * run停止
     *
     */
    public void interrupt(){
        interrupt = true;
    }

    /**
     * 判断是否连接
     * @return
     */
    public boolean isConnected(){
        if(socket == null){
            return false;
        }

        if(!socket.isConnected()){
            return false;
        }
        if(socket.isClosed()){
            return false;
        }

        return true;
    }

    /**
     * 运行
     */
    @Override
    public void run() {
        interrupt = false;
        System.out.println("UdpServer启动, 端口:" + port);
        while(!interrupt){
            try {
                byte[] buffer = new byte[MAX_LENGTH];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                if(interrupt){
                    return;
                }
                if(listener != null){
                    listener.OnUdpReceive(packet.getAddress().getHostAddress(), buffer, packet);
                }else{
                    String receStr = new String(packet.getData(), 0 , packet.getLength());
                    System.out.println("接收数据包" + receStr);
                }

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("UdpServer停止, 端口:" + port);
    }

    /**
     * 发送数据
     * @param ip      目标ip
     * @param port    目标端口
     * @param data    数据
     * @return
     */
    public boolean send(String ip,int port, byte data[]){

        InetAddress address = null;
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int getMAX_LENGTH() {
        return MAX_LENGTH;
    }

    public void setMAX_LENGTH(int MAX_LENGTH) {
        this.MAX_LENGTH = MAX_LENGTH;
    }

    /**
     * 设置监听回调
     * @param listener
     */
    public void setListener(QUdpListener listener) {
        this.listener = listener;
    }

    /**
     * 内部监听器
     */
    public interface QUdpListener {
        /**
         * 数据接收监听回调
         * @param ip       客户端ip
         * @param buffer   客户端数据
         */
        void OnUdpReceive(String ip, byte buffer[], DatagramPacket packet);
    }
}
