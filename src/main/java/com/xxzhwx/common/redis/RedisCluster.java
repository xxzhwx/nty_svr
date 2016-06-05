package com.xxzhwx.common.redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class RedisCluster {
    private static final Map<String, RedisCluster> clusters = new HashMap<String, RedisCluster>();

    static {
        // Initialize clusters from configuration, like:
        // init(name1, urls1);
        // init(name2, urls2);
    }

    private static void init(String name, String urls) {
        HashSet<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        for (String url : urls.split(",")) {
            String[] hostPort = url.split(":");
            HostAndPort hostAndPort = new HostAndPort(hostPort[0], Integer.parseInt(hostPort[1]));
            hostAndPorts.add(hostAndPort);
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);
        poolConfig.setMinIdle(1);
        poolConfig.setMaxIdle(10);
        JedisCluster cluster = new JedisCluster(hostAndPorts, 2000, poolConfig);
        clusters.put(name, new RedisCluster(cluster));
    }

    public static RedisCluster getRedisCluster(String name) {
        return clusters.get(name);
    }

    public static void destroy() {
        clusters.forEach((k, v) -> v.close());
    }

    private JedisCluster cluster;

    private RedisCluster(JedisCluster cluster) {
        this.cluster = cluster;
    }

    private void close() {
        cluster.close();
    }

    // *** redis client methods, add what you need. ***
    public String setex(String key, int seconds, String value) {
        try {
            return cluster.setex(key, seconds, value);
        } catch (JedisException e) {
            System.out.println("<<RedisCluster>>: " + e.getMessage());
            return null;
        }
    }

    public String get(String key) {
        try {
            return cluster.get(key);
        } catch (JedisException e) {
            System.out.println("<<RedisCluster>>: " + e.getMessage());
            return null;
        }
    }
}
