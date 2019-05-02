package com.sms.admin.utils;

import com.google.gson.Gson;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.dao.AdminAccountDao;
import com.sms.admin.data.dto.UserToken;
import com.sms.admin.data.entity.AdminAccount;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    AdminAccountDao adminAccountDao;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }
    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }
    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        Object result = null;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        if(result==null){
            return null;
        }
        return result.toString();
    }
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  boolean hmset(String key, Map<String, String> value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public  boolean hset(String key, String field, String value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().put(key, field, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  boolean hdel(String key, String... fields) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().delete(key, fields);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  String hget(String key, String field) {
        Object result = null;
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        HashOperations<Serializable, Object, Object> operations = redisTemplate.opsForHash();
        result = operations.get(key,field);
        if(result==null){
            return null;
        }
        return result.toString();
    }

    public Map<String,String> hmget(String key) {
        Map<String,String> result =null;
        try {
            result=  redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public AdminAccount findRedisAdminAccount(HttpServletRequest request) {
        /*boolean isExist =  this.exists("username");
        if (!isExist) {
            throw new BaseException(ExceptionCode.NO_PERMIT);
        }
        String username = this.get("username");*/
        Cookie[] arrCks = request.getCookies();
        String token = "";
        if (null != arrCks) {
            for (Cookie cookie : arrCks) {
                if (cookie.getName().equals("sms_user")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token.equals("")) {
            throw new BaseException(ExceptionCode.NO_PERMIT);
        }

        AdminAccount adminAccount = null;
        String tokens = "";
        Map<String, String> map = this.hmget("tokens");
        for (Map.Entry<String, String> entry:map.entrySet()) {
            if (entry.getKey().equals(token)) {
                tokens = entry.getValue();
            }
        }
        UserToken userToken = new Gson().fromJson(token, UserToken.class);
        adminAccount = new Gson().fromJson(tokens,AdminAccount.class);
        if (adminAccount.getUsername().equals(((AdminAccount)userToken.getUser()).getUsername())) {
            return adminAccount;
        }
        throw new BaseException(ExceptionCode.NO_PERMIT);
    }
}
