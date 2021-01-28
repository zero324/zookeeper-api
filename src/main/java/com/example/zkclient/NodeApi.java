package com.example.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class NodeApi {
    //借助zkClient创建会话
    public static void main(String[] args) throws InterruptedException {
    //创建一个zkclient的实例就可以完成连接,完成会话的创建
        //serverString :服务器链接地址  127.0.0.1:2181
        //注意:zkClient 是通过了对zookeeper的api内部封装 将异步创建会话同步化了
        ZkClient zkClient = new ZkClient("192.168.230.130:2181");
        System.out.println("会话被创建了.....");
        String path = "/lg-zkClient-Ep";

        //判断节点是否存在
        boolean exists = zkClient.exists(path);
        if (!exists){
            zkClient.createEphemeral(path, "123");
        }

        //注册监听
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String path, Object data) throws
                    Exception {
                /*
                    path  节点名称
                    data 变更后的数据
                 */
                System.out.println(path+"该节点内容被更新，更新后的内容"+data);
            }
            public void handleDataDeleted(String s) throws Exception {
                /*
                    节点删除后回调函数
                    s : 节点名称
                 */
                System.out.println(s+" 该节点被删除");
            }
        });
        //获取节点内容
        Object o = zkClient.readData(path);
        System.out.println(o);
        //更新
        zkClient.writeData(path,"4567");
        Thread.sleep(1000);
        //删除
        zkClient.delete(path);
        Thread.sleep(1000);

    }
}
