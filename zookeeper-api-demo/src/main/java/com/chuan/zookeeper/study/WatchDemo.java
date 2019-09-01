package com.chuan.zookeeper.study;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.checkerframework.checker.units.qual.C;

public class WatchDemo {

    public static String STR_CONNECT = "192.168.160.128:2181";

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(STR_CONNECT).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
//        addListenerWithNode(curatorFramework);
//        addListenerWithChild(curatorFramework);
        addListenerWithTreeNode(curatorFramework);

        System.in.read();
    }

    private static void addListenerWithTreeNode(CuratorFramework curatorFramework) throws Exception {
        TreeCache treeCache = new TreeCache(curatorFramework,"/watch");

        TreeCacheListener treeCacheListener = (curatorFramework1,treeCacheEvent)->{
            System.out.println("综合节点监听");
            System.out.println(treeCacheEvent.getType() +"-----"+ new String(treeCacheEvent.getData().getData()) +"-----"+ treeCacheEvent.getData().getPath());
        };

        treeCache.getListenable().addListener(treeCacheListener);
        treeCache.start();


    }


    private static void addListenerWithChild(CuratorFramework curatorFrameword) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFrameword,"/watch",true);

        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework1,pathChildrenCacheEvent)->{
            System.out.println(pathChildrenCacheEvent.getType() +"-------"+new String(pathChildrenCacheEvent.getData().getData()));
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);

        pathChildrenCache.start();
    }


    private static void addListenerWithNode(CuratorFramework curatorFramework) throws Exception {
        NodeCache nodeCache = new NodeCache(curatorFramework,"/watch",false);
        NodeCacheListener nodeCacheListener = ()->{
            System.out.println("receive Node Changed");
            System.out.println(nodeCache.getCurrentData().getPath() +"----"+ new String(nodeCache.getCurrentData().getData()));
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }

}
