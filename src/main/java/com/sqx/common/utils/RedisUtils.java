package com.sqx.common.utils;

import com.google.gson.Gson;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 */
@Slf4j
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;
    private final static Gson Gson = new Gson();

    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    /**
     * 根据表达式批量删除key。内部使用SCAN + DEL的方式进行删除
     * 建议使用该方法替代 flushdb 方法
     * @param pattern redis支持的表达式
     */
    public void deleteByPattern(String pattern){
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            // TODO(bootun): 该删除并非是原子的，如果想要原子的删除，可以使用LUA脚本继续优化。当前版本存在只删除部分KEY的风险
            // 使用SCAN规避KEYS的性能问题
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(1000).build())) {
                while (cursor.hasNext()) {
                    connection.del(cursor.next());
                }
            }
            return null;
        });
    }


    /**
     * flushdb 方法内部使用SCAN + DEL命令删除Redis中的所有key
     * 但在知道业务key的前提下，仍然建议使用 deleteByPattern 方法来代替flushdb 方法
     */
    @Deprecated
    public void flushdb(){
//        // 创建Redis客户端
//        RedisClient redisClient = RedisClient.create("redis://"+redisHost+":"+port);
//        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
//            // 获取同步的Redis命令执行器
//            RedisCommands<String, String> syncCommands = connection.sync();
//            syncCommands.auth(password);
//            // 清空当前数据库的所有key
//            syncCommands.flushdb();
//
//            // 或者清空所有数据库的所有key
//            // syncCommands.flushall();
//
//            log.info("Redis cache cleared successfully");
//        } finally {
//            // 关闭Redis客户端
//            if (redisClient != null) {
//                redisClient.shutdown();
//            }
//        }

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(1000).build())) {
                while (cursor.hasNext()) {
                    connection.del(cursor.next());
                }
            }
            // NOTE: 严禁使用FLUSHDB命令
            //connection.flushDb();
            return null;
        });
    }


    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return Gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return Gson.fromJson(json, clazz);
    }
}
