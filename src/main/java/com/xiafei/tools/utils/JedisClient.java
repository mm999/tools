package com.xiafei.tools.utils;

import com.xiafei.tools.enums.JedisEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <P>Description: 使用时只需调用有参构造方法初始化jdedisCluster. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/6/7</P>
 * <P>UPDATE DATE: 2017/6/28</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.7.0
 */
@Slf4j
public class JedisClient {

    private static JedisCluster jedisCluster;
    /**
     * 最大连接数
     */
    private static Integer maxTotal = 100;
    /**
     * 空闲连接数
     */
    private static Integer maxIdle = 50;
    /**
     * 等待超时时间
     */
    private static Integer maxWaitTime = 3000;

    static {
        BundleUtil bundle = BundleUtil.newInstance("redis");
        if (bundle != null && jedisCluster == null && !StringUtils.isBlank(bundle.getString("redisLock"))) {
            String redisStr = bundle.getString("redisLock");
            if (!StringUtils.isEmpty(redisStr)) {
                String[] redisArr = redisStr.split(",");
                Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
                //Jedis Cluster will attempt to discover cluster nodes automatically
                for (String re : redisArr) {
                    String[] url = re.split(":"); // host:port
                    int port = Integer.parseInt(url[1]);
                    jedisClusterNodes.add(new HostAndPort(url[0], port));
                }
                JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                jedisPoolConfig.setMaxTotal(Integer.valueOf(bundle.getString("maxTotal", "100")));//the max number of connection
                jedisPoolConfig.setMaxIdle(Integer.valueOf(bundle.getString("maxIdle", "50")));//the max number of free
                jedisPoolConfig.setMaxWaitMillis(Long.valueOf(bundle.getString("maxWaitTime", "1000")));//the longest time of waiting
                String encryptRedise = bundle.getString("encryptRedise");
                if (encryptRedise != null && encryptRedise.equals("false")) {
                    jedisCluster = new JedisCluster(jedisClusterNodes, 5000, 5000, 1, jedisPoolConfig);
                } else {
                    jedisCluster = new JedisCluster(jedisClusterNodes, 5000, 5000, 1, bundle.getString("redisPassword"), jedisPoolConfig);
                }
            } else {
                log.info("********jedis fail to initialize********");
            }
        }
    }

    /**
     * redis 工具类初始化构造函数
     * <p> 允许Spring 注入静态使用,要求 redis地址必输,如果当前存在jedisCluster 则使用存在的不再初始化 </p>
     * <p> redisAdress 表达格式要求 例 192.168.0.1:2080,192.168.0.1:2081</p>
     * <p> 注意：如果redisPassword为空，则构造函数尝试使用无密码创建客户端</p>
     *
     * @param redisAdress
     * @param redisPassword
     * @param maxTotal
     * @param maxIdle
     * @param maxWaitTime
     * @throws Exception <p> 如果必要参数为空 throw Exception ,如果 初始化jedisCluster失败 throw Exception</p>
     * @author hujian create 2017-09-15
     */
    public JedisClient(final String redisAdress, final String redisPassword, final Integer maxTotal, final Integer maxIdle, final Integer maxWaitTime) throws Exception {
        if (jedisCluster != null) {
            return;
        }
        if (!StringUtils.isEmpty(redisAdress)) {
            this.maxTotal = maxTotal != null ? maxTotal : this.maxTotal;
            this.maxIdle = maxIdle != null ? maxIdle : this.maxIdle;
            this.maxWaitTime = maxWaitTime != null ? maxWaitTime : this.maxWaitTime;
            String[] redisArr = redisAdress.split(",");
            Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
            for (String re : redisArr) {
                String[] url = re.split(":"); // host:port
                int port = Integer.parseInt(url[1]);
                jedisClusterNodes.add(new HostAndPort(url[0], port));
            }
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(this.maxTotal);//the max number of connection
            jedisPoolConfig.setMaxIdle(this.maxIdle);//the max number of free
            jedisPoolConfig.setMaxWaitMillis(this.maxWaitTime);//the longest time of waiting
            try {
                if (StringUtils.isBlank(redisPassword)) {
                    jedisCluster = new JedisCluster(jedisClusterNodes, 5000, 5000, 1, jedisPoolConfig);
                } else {
                    jedisCluster = new JedisCluster(jedisClusterNodes, 5000, 5000, 1, redisPassword, jedisPoolConfig);
                }
            } catch (Exception e) {
                log.error("********jedis fail to initialize********", e);
                throw e;
            }

        } else {
            log.error("*******redisAdress is  null*********");
            throw new Exception("redisAdress is  null");
        }
    }

    public static void put(String key, String value) {
        assertPoolNotNull();
        jedisCluster.set(key, value);
    }

    public static void putWithExpire(String key, String value, int seconds) {
        assertPoolNotNull();
        jedisCluster.set(key, value);
        jedisCluster.expire(key, seconds);
    }

    public static void set(final String key, final String value, final String nxxx, final String expx,
                           final long time) {
        assertPoolNotNull();
        jedisCluster.set(key, value, nxxx, expx, time);
    }

    /**
     * 设置key并附上过期时间.
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间-秒
     * @return OK-设置成功
     */
    public static String setEx(final String key, final String value, final int time) {
        assertPoolNotNull();
        return jedisCluster.setex(key, time, value);
    }

    /**
     * 如果存在则设置.
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间-秒
     * @return 设置是否成功
     */
    public static boolean setIfExist(final String key, final String value, final int time) {
        assertPoolNotNull();
        return "OK".equalsIgnoreCase(jedisCluster.set(key, value, JedisEnums.NxxxEnum.XX.name(), JedisEnums.EXPX.EX.name(), time));
    }


    public static boolean setNxEX(final String key, final String value, final String nxxx, final String expx,
                                  final long time) {
        assertPoolNotNull();
        return "OK".equalsIgnoreCase(jedisCluster.set(key, value, nxxx, expx, time));
    }

    public static boolean expireAt(final String key, final Date date) {
        assertPoolNotNull();
        try {
            return jedisCluster.pexpireAt(key, date.getTime()) == 1 ? true : false;
        } catch (Exception e) {
            return jedisCluster.expireAt(key, date.getTime() / 1000) == 1 ? true : false;
        }
    }

    public static void expire(String key, int seconds) {
        assertPoolNotNull();
        jedisCluster.expire(key, seconds);
    }

    public static Long incr(String key) {
        assertPoolNotNull();
        return jedisCluster.incr(key);
    }

    public static Long hset(String key, String field, String value) {
        assertPoolNotNull();
        return jedisCluster.hset(key, field, value);
    }

    public static Long hset(byte[] key, byte[] field, byte[] value) {
        assertPoolNotNull();
        return jedisCluster.hset(key, field, value);
    }

    public static Long hdel(String key, String... field) {
        assertPoolNotNull();
        return jedisCluster.hdel(key, field);
    }

    public static Long decr(String key) {
        assertPoolNotNull();
        return jedisCluster.decr(key);
    }

    public static boolean sismember(String key, String member) {
        assertPoolNotNull();
        return jedisCluster.sismember(key, member);
    }

    public static void sadd(String key, String... member) {
        assertPoolNotNull();
        jedisCluster.sadd(key, member);
    }

    public static Set<String> smembers(String key, String... member) {
        assertPoolNotNull();
        return jedisCluster.smembers(key);
    }

    public static Long ttl(String key) {
        assertPoolNotNull();
        return jedisCluster.ttl(key);
    }

    public static void batchPut(Map<String, String> map) {
        assertPoolNotNull();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            jedisCluster.set(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 从redis中尝试获取一个key的值.
     *
     * @param key 这还用解释
     * @return 如果这个key在redis中不存在，返回null而不是"nil"
     */
    public static String get(String key) {
        assertPoolNotNull();
        return jedisCluster.get(key);
    }

    public static boolean exists(String key) {
        assertPoolNotNull();
        return jedisCluster.exists(key);
    }

    public static void remove(String key) {
        assertPoolNotNull();
        jedisCluster.del(key);
    }

    //获取hashMap中所有key
    public static Set<String> hkeys(String hmName) {
        try {
            return jedisCluster.hkeys(hmName);
        } catch (Exception e) {
            log.error("获取所有key失败", e);
        }
        return null;
    }

    public static boolean hmset(String hmKeyname, Map<String, String> map) {
        assertPoolNotNull();
        try {
            jedisCluster.hmset(hmKeyname, map);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String hmget(String hmKeyname, String keyname) {
        assertPoolNotNull();
        return jedisCluster.hget(hmKeyname, keyname);
    }


    public static int setnx(String key, String value) {
        assertPoolNotNull();
        return jedisCluster.setnx(key, value).intValue();
    }

    public static void lpush(String key, String value, int seconds) {
        assertPoolNotNull();
        jedisCluster.lpush(key, value);
        jedisCluster.expire(key, seconds);
    }

    public static void lpush(String key, String value) {
        assertPoolNotNull();
        jedisCluster.lpush(key, value);
    }


    public static List<String> brpop(String key) {
        assertPoolNotNull();
        return jedisCluster.brpop(0, key);
    }

    public static String lpop(String key) {
        assertPoolNotNull();
        return jedisCluster.lpop(key);
    }

    public static Long incrBy(String key, long integer) {
        assertPoolNotNull();
        return jedisCluster.incrBy(key, integer);
    }

    public static String getSet(String key, String value) {
        assertPoolNotNull();
        return jedisCluster.getSet(key, value);
    }

    private static void assertPoolNotNull() {
        if (jedisCluster == null) {
            throw new NullPointerException("shardedJedisPool is null");
        }
    }

    private JedisClient() {

    }

    public static void main(String[] args) throws Exception {
        JedisClient.put("1{javaput}", "test1");
        JedisClient.put("1{javaput}", "test2");
        System.out.println(JedisClient.get("1{javaput}"));

        JedisClient.putWithExpire("2{javaput}", "test1", 1);
        JedisClient.putWithExpire("2{javaput}", "test2", 1);
        Thread.sleep(3000);

        System.out.println(JedisClient.get("2{javaput}"));

        Map<String, String> map = new HashMap<String, String>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "1");
        System.out.println(JedisClient.hmset("useridxxxxx", map));

        System.out.println(JedisClient.hmget("useridxxxxx", "a"));

    }

}
