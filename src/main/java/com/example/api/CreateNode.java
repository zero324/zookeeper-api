package com.example.api;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateNode implements Watcher {
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
         zooKeeper = new ZooKeeper("192.168.100.129:2181", 3000, new CreateNode());
        System.out.println(zooKeeper.getState());
        //countDownLatch.await();
        //表示会话真正建⽴
        Thread.sleep(Integer.MAX_VALUE);

    }

    private static void createNodeSync() throws KeeperException, InterruptedException {
        /**
         * path ：节点创建的路径
         * data[] ：节点创建要保存的数据，是个byte类型的
         * acl ：节点创建的权限信息(4种类型)
         * ANYONE_ID_UNSAFE : 表示任何⼈
         * AUTH_IDS ：此ID仅可⽤于设置ACL。它将被客户机验证的ID替
         换。
         * OPEN_ACL_UNSAFE ：这是⼀个完全开放的ACL(常⽤)-->
         world:anyone
         * CREATOR_ALL_ACL ：此ACL授予创建者身份验证ID的所有权限
         * createMode ：创建节点的类型(4种类型)
         * PERSISTENT：持久节点
         * PERSISTENT_SEQUENTIAL：持久顺序节点
         * EPHEMERAL：临时节点
         * EPHEMERAL_SEQUENTIAL：临时顺序节点
         String node = zookeeper.create(path,data,acl,createMode);
         */
        //持久节点
        String node_persistent = zooKeeper.create("/lg-persistent", "这是持久节点的内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //临时节点
        String node_ephemeral = zooKeeper.create("/lg-ephemeral", "这是临时节点的内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        //持久顺序节点
        String node_persistent_sequential = zooKeeper.create("/lg-persistent-sequential", "这是持久顺序节点的内容".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        System.out.println("创建持久节点  "+node_persistent);
        System.out.println("创建临时节点  "+node_ephemeral);
        System.out.println("创建持久顺序节点  "+node_persistent_sequential);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {


        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            //当连接创建了，服务端发送给客户端SyncConnected事件
            System.out.println("会话创建成功触发事件");
           // countDownLatch.countDown();
            //创建节点
            try {
                createNodeSync();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
