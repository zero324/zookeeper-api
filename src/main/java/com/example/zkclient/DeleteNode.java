package com.example.zkclient;

import org.I0Itec.zkclient.ZkClient;

public class DeleteNode {
    //借助zkClient创建会话
    public static void main(String[] args) {
    //创建一个zkclient的实例就可以完成连接,完成会话的创建
        //serverString :服务器链接地址  127.0.0.1:2181
        //注意:zkClient 是通过了对zookeeper的api内部封装 将异步创建会话同步化了
        ZkClient zkClient = new ZkClient("192.168.100.129:2181");
        System.out.println("会话被创建了.....");
        //递归删除节点 保留了/lg-zkClient节点
        String path="/lg-zkClient/lg-c1";
       // zkClient.createPersistent(path+"/c11");
        zkClient.deleteRecursive(path);
        System.out.println("递归删除子节点");
    }
}
