package com.example.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class DeleteNode implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper =null;

    public static void main(String[] args) throws IOException, InterruptedException {
        /*
          客户端可以通过创建⼀个zk实例来连接zk服务器
          new Zookeeper(connectString,sesssionTimeOut,Wather)
          connectString: 连接地址：IP：端⼝
          sesssionTimeOut：会话超时时间：单位毫秒
          Wather：监听器(当特定事件触发监听时，zk会通过watcher通知到客户端)
       */
         zooKeeper = new ZooKeeper("192.168.100.129:2181", 3000, new DeleteNode());
        System.out.println(zooKeeper.getState());
        //countDownLatch.await();
        //表示会话真正建⽴
        Thread.sleep(Integer.MAX_VALUE);

    }


    @Override
    public void process(WatchedEvent watchedEvent) {


        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            //当连接创建了，服务端发送给客户端SyncConnected事件
            System.out.println("会话创建成功触发事件");
           // countDownLatch.countDown();
            //删除节点
            try {
                deleteNode();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void deleteNode() throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists("/lg-persistent/c1", false);
        System.out.println(stat==null?"该节点不存在":"该节点存在");
        zooKeeper.delete("/lg-persistent/c1",-1);
        Stat stat2 = zooKeeper.exists("/lg-persistent/c1", false);
        System.out.println(stat2==null?"该节点不存在":"该节点存在");
    }
}
