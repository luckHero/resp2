package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Lucky
 * @Date:2020-02-08 22:19
 */
/*
    套餐服务管理
 */
@Service(interfaceClass = SetmealService.class) //这里如果不加这个注解会抛什么异常?
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;//生成静态页面的核心配置类的对象
    @Value("${out_put_path}")
    private String outPutPath;//从属性文件中读取要生成静态页面的位置

    //新增套餐
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setmealMapper.add(setmeal);//往套餐表中添加数据
        this.setSetmealAndCheckGroup(setmeal,checkGroupIds);//操作中间表

        String imgFileName = setmeal.getImg();//获取照片的文件名
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,imgFileName);//将保存到数据库的文件名称页存到redis数据库中
        //当进行添加套餐时会进行生成静态文件(套餐列表,套餐详情的静态页面)

        generateMobilrStaticHtml();//生成静态页面的方法
    }

    //生成当前方法所需的静态页面
    public void generateMobilrStaticHtml(){
        List<Setmeal> list = setmealMapper.findAll(); //生成静态页面之前需要查询套餐数据
        //生成套餐列表的页面
        generateMobileSetmealListHtml(list);
       // System.out.println(11);
        //生成套餐详情的页面
        generateMobileSetmealDetailHtml(list);
    }

    //生成套餐列表静态页面的方法
    public void generateMobileSetmealListHtml(List<Setmeal>list){
        Map map=new HashMap();
        map.put("setmealList",list); //封装模板文件提供数据,这里的键要和模板的键一致
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    //生成套餐详情静态页面的方法
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map=new HashMap();
            setmealMapper.findCheckGroupBySetmealId(setmeal.getId());

            map.put("setmeal",setmealMapper.findSetmealById(setmeal.getId())); //要根据套餐id查询检查组检查项

            //这里生成套餐的静态页面要和模板文件保持一致,不然会访问不到
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }


    //通用用于生成静态页面的方法
    public  void generateHtml(String templateName,String htmlPageName,Map map){//模板文件,静态页面名称,数据
        Configuration configuration = freeMarkerConfig.getConfiguration();//获取配置文件对象
        Writer out=null;
        try {
            Template template = configuration.getTemplate(templateName);//生成模板对象
             out = new FileWriter(new File(outPutPath+"/"+htmlPageName));//创建输出流,输出静态文件,指定生成静态文件的名称
            template.process(map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //分页展示套餐的方法
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();//当前页码
        Integer pageSize = queryPageBean.getPageSize();//每页显示的记录数
        String queryString = queryPageBean.getQueryString();//查询的条件
        PageHelper.startPage(currentPage,pageSize);//使用mybatis的插件,他会自动在sql上去添加limit关键字
        Page<Setmeal> page=setmealMapper.findPage(queryString);//返回的是mybais封装好的list集合,该集合的属性就有分页展示的数据
        return new PageResult(page.getTotal(),page.getResult());
    }
    //根据id查询套餐的方法,只用于信息的回显,没有查对应的检查组,以及检查项
    @Override
    public Setmeal findById(Integer id) {
        return setmealMapper.findById(id);
    }
    //查询套餐中,包含哪些检查组,将检查组的id返回去
    @Override
    public List<Integer> findCheckGroupBySetmealId(Integer id) {
        return setmealMapper.findCheckGroupBySetmealId(id);
    }
    //编辑套餐的方法
    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        //先添加t_checkgroup表
        setmealMapper.edit(setmeal);
        //管理当前检查组的检查项
        setmealMapper.deleteAssociation(setmeal.getId());
        //重新关联检查组的检查项
        this.setSetmealAndCheckGroup(setmeal,checkGroupIds);

        generateMobilrStaticHtml();//生成静态页面的方法
    }
    //删除套餐的的方法
    @Override
    public void delete(Integer id) {
        setmealMapper.deleteAssociation(id);//先删除中间表的外键关联
        setmealMapper.delete(id);

        generateMobilrStaticHtml();//生成静态页面的方法
    }

    //查询所有的套餐表
    @Override
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }

    //查询套餐详情的方法
    @Override
    public Setmeal findSetmealById(int id) {
        return setmealMapper.findSetmealById(id);
    }
    /*
        统计套餐数量的方法
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealMapper.findSetmealCount();
    }

    //操作套餐表和检查组表的方法
    public void setSetmealAndCheckGroup(Setmeal setmeal ,Integer[] checkGroupIds){
        Integer setmealId = setmeal.getId();//获取setmealId
        //添加中间表中关联,用map集合来封装参数条件
        for (Integer checkGroupId : checkGroupIds) {
            Map<String,Integer> map=new HashMap<String,Integer>();
            map.put("setmealId",setmealId);
            map.put("checkGroupId",checkGroupId);
            setmealMapper.setSetmealAndCheckGroup(map);
        }
    }
}
