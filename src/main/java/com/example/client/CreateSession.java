package com.example.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateSession implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        /*
          客户端可以通过创建⼀个zk实例来连接zk服务器
          new Zookeeper(connectString,sesssionTimeOut,Wather)
          connectString: 连接地址：IP：端⼝
          sesssionTimeOut：会话超时时间：单位毫秒
          Wather：监听器(当特定事件触发监听时，zk会通过watcher通知到客户端)
       */
        ZooKeeper zooKeeper = new ZooKeeper("192.168.230.130:2181", 3000, new CreateSession());
        System.out.println(zooKeeper.getState());
        countDownLatch.await();
        //表示会话真正建⽴
        System.out.println("=========Client Connected to "+zooKeeper.getState()+"zookeeper==========");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {


        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            //当连接创建了，服务端发送给客户端SyncConnected事件
            System.out.println("会话创建成功触发事件");
            countDownLatch.countDown();
        }

    }
}
