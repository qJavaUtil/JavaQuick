package blxt.qjava.qudp;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/10/20 0:47
 */
public class QUdpServer implements Runnable{
    /** 数据包最大长度 */
    protected int MAX_LENGTH = 1024;
    /** 连接scoket */
    protected DatagramSocket socket = null;
    /** 监听回调 */
    protected List<QUdpListener> listener = new ArrayList<>();
    /** 监听端口 */
    protected int port;
    /** 暂停标志 */
    protected boolean interrupt = false;


    public QUdpServer(){

    }

    /**
     *
     * @param port         监听端口
     * @param listener     监听回调
     */
    public QUdpServer(int port, QUdpListener listener) {
        this.port = port;
        if(listener != null) {
            this.listener.add(listener);
        }
    }

    /**
     * 创建
     * @return
     */
    public boolean creat(){
        return creat(port);
    }

    /**
     * 创建
     * @return
     */
    public boolean creat(int port){
        this.port = port;
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
                    noti(packet);
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

    public void setPort(int port){
        this.port = port;
    }

    /**
     * 设置监听回调
     * @param listener
     */
    public void setListener(QUdpListener listener) {
        this.listener.clear();
        this.listener.add(listener);
    }

    public void addListener(QUdpListener listener) {
        this.listener.add(listener);
    }

    public void setListener(List<QUdpListener> listener) {
        this.listener = listener;
    }

    /**
     * 广播
     * @param packet
     */
    private void noti(DatagramPacket packet){
        for (QUdpListener listener : this.listener) {
            listener.OnUdpReceive(packet);
        }
    }

    /**
     * 内部监听器
     */
    public interface QUdpListener {
        /**
         * 数据接收监听回调
         * @param packet      DatagramPacket包
         */
        void OnUdpReceive(DatagramPacket packet);
    }
}
