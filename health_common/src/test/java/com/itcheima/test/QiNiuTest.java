package com.itcheima.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;


/**
 * @Author: Lucky
 * @Date:2020-02-08 1:53
 */
public class QiNiuTest {
    //上传图片到七牛云
    @Test
    public  void test(){

        Configuration cfg = new Configuration(Zone.zone0()); //设置一个域名
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "krbF9POOE4J0Cnm6X2Eahye779xmXB8vcTjTo9hw";//
        String secretKey = "1zEYwcy000QwmNZMElvN1mPCNjKUi8AQLmoCtM19";//sk密钥
        String bucket = "hyq-repertory"; //仓库名
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "E:\\黑马\\传智健康-加密\\视频\\day04\\资源\\图片资源\\3bd90d2c-4e82-42a1-a401-882c88b06a1a2.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "hahaha.jpg";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
    //删除七牛云空键的文件
    @Test
    public  void test2(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        String accessKey = "krbF9POOE4J0Cnm6X2Eahye779xmXB8vcTjTo9hw";
        String secretKey = "1zEYwcy000QwmNZMElvN1mPCNjKUi8AQLmoCtM19";
        String bucket = "hyq-repertory";
        String key = "hahaha.jpg";
        Auth auth = Auth.create(accessKey, secretKey); //进行验证鉴权
        BucketManager bucketManager = new BucketManager(auth, cfg);  //创建删除这个方法
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}

