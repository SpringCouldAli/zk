package com.chuan.zookeeper.study;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * author:曲终、人散
 * Date:2019/8/25 18:04
 */
public class LockDemo {
    private static String str_connect = "192.168.160.128:2181";

    public static CuratorFramework curatorFramework;

    static {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(str_connect)
                .sessionTimeoutMs(50000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
    }

    public static void main(String[] args) {

        InterProcessMutex lock = new InterProcessMutex(curatorFramework,"/lock");

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" ---->尝试获取锁！！！");
                try {
                    lock.acquire();
                    System.out.println(Thread.currentThread().getName()+" ---->获得了锁！！！");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    lock.release();
                    System.out.println(Thread.currentThread().getName()+" ---->释放了锁！！！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}
