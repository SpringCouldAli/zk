package com.chuan.zookeeper.study;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * author:曲终、人散
 * Date:2019/8/25 16:54
 */
public class WatcherDemo2 {
    
    private static String str_connect = "192.168.160.128:2181";
    
    private static String path = "/watch";

    public static CuratorFramework curatorFramework;

    static {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(str_connect)
                .sessionTimeoutMs(50000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
    }

    public static void main(String[] args) throws Exception {
//        addListenerWithNode(curatorFramework,path);

        addListenerWithChild(curatorFramework,path);

        System.in.read();
    }


    private static void addListenerWithChild(CuratorFramework curatorFramework,String path) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,path,true);

        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework1,pathChildrenCacheEvent)->{
            System.out.println("监听子节点变化");
            System.out.println(pathChildrenCacheEvent.getType()+"--------"+pathChildrenCacheEvent.getData().getData());
        };

        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }


    //监听当前节点的变化
    private static void addListenerWithNode(CuratorFramework curatorFramework, String path) throws Exception {
        NodeCache nodeCache = new NodeCache(curatorFramework,path,false);
        NodeCacheListener nodeCacheListener = ()->{
            System.out.println("监听当前节点变化");
            System.out.println(nodeCache.getCurrentData().getPath() +"-------"+ new String(nodeCache.getCurrentData().getData()));
        };

        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();



    }

}
