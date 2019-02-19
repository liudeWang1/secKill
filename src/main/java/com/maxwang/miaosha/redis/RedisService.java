package com.maxwang.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;



    public <T> T get(KeyPrefix prefix,String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey=prefix.getPrefix()+key;

            String str = jedis.get(realKey);
            //我们需要将得到的str转化为T
            T t = stringToBean(str,clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }

    }


    public <T> boolean set(KeyPrefix prefix,String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            String str = beanToString(value);

            if (str == null) {
                return false;
            }
            //生成真正的key
            String realKey=prefix.getPrefix()+key;
            int seconds=prefix.expireSeconds();
            if (seconds<=0){
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey,seconds,str);
            }

            return true;
        } finally {
            returnToPool(jedis);
        }

    }

    /**
     * 判断键是否存在值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey=prefix.getPrefix()+key;

            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }

    }

    /**
     * incr方法
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey=prefix.getPrefix()+key;

            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * decr方法
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            //生成真正的key
            String realKey=prefix.getPrefix()+key;

            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }

    }


    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    /**
     * 将bean转化为String
     *
     * @param value
     * @return
     */
    private <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();

        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 将String转化为Bean
     * am str
     *
     * @param <T>
     * @param clazz
     * @return
     */
    private <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null||str.length() <= 0||clazz==null){
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }





}
