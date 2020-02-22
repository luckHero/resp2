package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @Author: Lucky
 * @Date:2020-02-09 11:04
 */
/*
    定义清理图片
 */
public class ClearImgJob {
//这里要不要归还连接
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //获取redis数据库中两个set集合进行差值,来确定辣鸡图片集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set!=null){
            for (String imgName : set) {
                //删除七牛云上的辣鸡图片
                QiniuUtils.deleteFileFromQiniu(imgName);//七牛工具类来删除图片
                //这个是将缓存中的大集合中删去已删除的图片名称
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
                System.out.println("清理辣鸡图片"+imgName);
            }
        }
    }
}
