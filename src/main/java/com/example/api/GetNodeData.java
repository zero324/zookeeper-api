package com.example.api;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GetNodeData implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws IOException, InterruptedException {
        /*
          客户端可以通过创建⼀个zk实例来连接zk服务器
          new Zookeeper(connectString,sesssionTimeOut,Wather)
          connectString: 连接地址：IP：端⼝
          sesssionTimeOut：会话超时时间：单位毫秒
          Wather：监听器(当特定事件触发监听时，zk会通过watcher通知到客户端)
       */
        zooKeeper = new ZooKeeper("192.168.100.129:2181", 3000, new GetNodeData());
        System.out.println(zooKeeper.getState());
        //countDownLatch.await();
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        /*
            子节点列表发生改变时会,服务器端会发生NodeChildrenChanged事件通知
            要重新获得子节点列表 同时注意:通知是一次性的需要反复注册监听
         */
        if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
            byte[] data = new byte[0];
            try {
                data = zooKeeper.getData("/lg-persistent", false, null);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(new String(data));
        }


        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            //当连接创建了，服务端发送给客户端SyncConnected事件
            System.out.println("会话创建成功触发事件");
            //countDownLatch.countDown();

            try {
                //获取节点的数据
                getNodeData();
                //获取子节点的列表
                getChildren();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /*
        获取某个节点的内容
     */
    private void getNodeData() throws KeeperException, InterruptedException {
        /**
         * path : 获取数据的路径
         * watch : 是否开启监听
         * stat : 节点状态信息
         * null: 表示获取最新版本的数据
         * zk.getData(path, watch, stat);
         */
        byte[] data = zooKeeper.getData("/lg-persistent", false, null);
        System.out.println(new String(data));
    }

    /*
        获取某个节点的子节点列表方法
     */
    public static void getChildren() throws KeeperException, InterruptedException {
        /*
        path:路径
        watch:是否要启动监听，当⼦节点列表发⽣变化，会触发监听
        zooKeeper.getChildren(path, watch);
        */
        List<String> children = zooKeeper.getChildren("/lg-persistent", true);
        System.out.println(children);

    }
}
