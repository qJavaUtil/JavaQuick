package blxt.qjava.qudp;

import java.io.IOException;
import java.net.*;

/**
 * @Author: Zhang.Jialei
 * @Date: 2020/10/20 1:04
 */
public class QUdpClient {
    protected String ip;
    protected int port;

    protected DatagramSocket socket = null;
    InetAddress address = null;

    public QUdpClient(){

    }

    /**
     * 设置连接参数
     * @param ip
     * @param port
     */
    public QUdpClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 连接,获取Socket
     */
    public boolean connect(){
        try {
            socket = new DatagramSocket();
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 连接,获取Socket
     */
    public boolean connect(String ip, int port){

        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        this.port = port;
        try {
            socket = new DatagramSocket();
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭连接
     */
    public void close(){
        if(socket == null){
            return;
        }
        socket.close();
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
     * 发送字符串
     * @param data
     * @return
     */
    public boolean send(String data){
        return send(data.getBytes(), data.getBytes().length);
    }

    /**
     * 发送byte
     * @param data
     * @param length
     * @return
     */
    public boolean send(byte[] data, int length){
        return send( address, port, data, length);
    }

    public boolean send(String ip, int port, byte[] data, int length){
        InetAddress address = null;
        try {
            address = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
        return send(address, port, data, length);
    }

    public boolean send(InetAddress address, int port, byte[] data, int length){
        DatagramPacket datagramPacket = new DatagramPacket(data, length, address, port);
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
