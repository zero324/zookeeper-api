package com.example.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class GetNodeChildren {
    //借助zkClient创建会话
    public static void main(String[] args) throws InterruptedException {
    //创建一个zkclient的实例就可以完成连接,完成会话的创建
        //serverString :服务器链接地址  127.0.0.1:2181
        //注意:zkClient 是通过了对zookeeper的api内部封装 将异步创建会话同步化了
        ZkClient zkClient = new ZkClient("192.168.230.130:2181");
        System.out.println("会话被创建了.....");

        //获取子节点列表
        List<String> children = zkClient.getChildren("/lg-zkClient");
        System.out.println(children);
        //注册监听事件
        /*
            客户端可以对一个不存在的节点的子节点列表进行监听
            只要该节点的子节点列表发生变化或者该节点本身创建或者删除都会触发监听事件
         */
        zkClient.subscribeChildChanges("/lg-zkClient-get", new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String>
                    currentChilds) throws Exception {
                System.out.println(parentPath + " 's child changed, currentChilds:" + currentChilds);
            }
        });
        //测试
        zkClient.createPersistent("/lg-zkClient-get");
        Thread.sleep(1000);
        zkClient.createPersistent("/lg-zkClient-get/c1");
        Thread.sleep(1000);

    }
}
