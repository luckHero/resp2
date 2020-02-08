package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author: Lucky
 * @Date:2020-02-08 20:07
 */
/*
检查套餐的管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;

    //使用redisPool操作redis服务
    @Autowired
    private JedisPool jedisPool;

    //文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile) {
        // System.out.println(multipartFile);//获取到文件名 是传到七牛云上的外联的文件名
        String originalFilename = multipartFile.getOriginalFilename();//获取上传到七牛云上的文件名
        int index = originalFilename.lastIndexOf(".");//获取后缀 "."的索引
        String extention = originalFilename.substring(index - 1);// .jpg  获取上传文件的后缀
        String newFileName = UUID.randomUUID().toString() + extention;//处理过后的文件名
        try {
            //将处理过的文件通过工具类上传到七牛云
            QiniuUtils.upload2Qiniu(multipartFile.getBytes(), newFileName);
            //只要用户往七牛云存了图片,就将文件名保存到redis数据库中去
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);//这里用的set集合
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);//上传文件失败
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, newFileName);//上传文件成功
    }

    //新增套餐的方法
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        try {
            setmealService.add(setmeal, checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);//新增套餐失败
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);//新增套餐成功
    }


    //分页展示套餐的方法
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(queryPageBean);
        return pageResult;
    }
}
