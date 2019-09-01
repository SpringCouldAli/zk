package com.chuan.zookeeper.study;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * author:曲终、人散
 * Date:2019/8/26 22:29
 */
public class LeaderSelectorClientB extends LeaderSelectorListenerAdapter implements Closeable{

    //表示当前进程
    private String name;

    //leader选举的api
    private LeaderSelector leaderSelector;

    public LeaderSelectorClientB(String name) {
        this.name = name;
    }

    public void setLeaderSelector(LeaderSelector leaderSelector) {
        this.leaderSelector = leaderSelector;
        leaderSelector.autoRequeue();
    }

    CountDownLatch countDownLatch = new CountDownLatch(1);

    public void start(){
        //开始竞争leader
        leaderSelector.start();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        System.out.println(this.name +"->现在是leader了");
        countDownLatch.await();//防止当前得到线程挂掉
    }

    @Override
    public void close() throws IOException {
        leaderSelector.close();
    }

    private static String str_connect = "192.168.160.128:2181,192.168.160.129:2181,192.168.160.130:2181";
    public static void main(String[] args) throws IOException {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(str_connect).sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();

        LeaderSelectorClientB leaderSelectorClient = new LeaderSelectorClientB("clintB");
        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework,"/leader",leaderSelectorClient);
        leaderSelectorClient.setLeaderSelector(leaderSelector);
        leaderSelectorClient.start();
        System.in.read();

    }

}
