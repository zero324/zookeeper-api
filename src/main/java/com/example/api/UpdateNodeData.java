package com.example.api;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class UpdateNodeData implements Watcher {
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
         zooKeeper = new ZooKeeper("192.168.100.129:2181", 3000, new UpdateNodeData());
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
            //更新节点数据方法
            try {
                updateNodeDataSync();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateNodeDataSync() throws KeeperException, InterruptedException {
        /*
        path:路径
        data:要修改的内容 byte[]
        version:为-1，表示对最新版本的数据进⾏修改
        zooKeeper.setData(path, data,version);
         */
        byte[] data = zooKeeper.getData("/lg-persistent", false, null);
        System.out.println("修改前的值  :"+new String(data));
        Stat stat = zooKeeper.setData("/lg-persistent", "客户端修改了持久节点的数据".getBytes(), -1);
        byte[] data2 = zooKeeper.getData("/lg-persistent", false, null);
        System.out.println("修改后的值  :"+new String(data2));
    }
}
