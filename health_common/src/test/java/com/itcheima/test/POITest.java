package com.itcheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

/**
 * @Author: Lucky
 * @Date:2020-02-09 19:25
 */
public class POITest {
    //使用POI读取Excel文件中的数据
    @Test
    public void test1() throws IOException {
        //加载指定文件,创建一个Excel对象,工作簿
        XSSFWorkbook excel =new XSSFWorkbook(new FileInputStream(new File("e://heima.xlsx"))); //这里获取的习Excel对象

        //读取Excel文件中的第一个Sheet标签页
        XSSFSheet rows = excel.getSheetAt(0);   //这里获取的是Excel里面的表对象 sheet
        for (Row row : rows) {//遍历第一页的数据      //获取每一行 的对象
        //遍历行,获得每个单元格的对象
            for (Cell cell : row) {//遍历每一行的到每个单元格,然后转换输出   //这里获取的是每一个单元格的对象
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }
    //excel读取数据
    @Test
    public void test2() throws IOException {
        //加载指定文件,创建一个Excel对象,工作簿
        XSSFWorkbook excel =new XSSFWorkbook(new FileInputStream(new File("e://heima.xlsx"))); //这里获取的习Excel对象
        //读取Excel文件中的第一个Sheet标签页
        XSSFSheet sheet = excel.getSheetAt(0);   //这里获取的是Excel里面的表对象 sheet            //1.获取到Excel中的第一个表
        int lastRowNum = sheet.getLastRowNum();//获取表中最后一行的索引                                 //2.获取表中最后一行的索引
        for (int i = 0; i <=lastRowNum ; i++) {

            XSSFRow row = sheet.getRow(i);    //根据行号来获取每一行的对象                             //3.根据索引来获取每一行的对象
            short lastCellNum = row.getLastCellNum(); //获取每一行的最后一列的索引                     //4.获取每一行最后一页的索引
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);   //根据单元格索引来获取每个单元格的对象               //5.根据每一列的索引的来获取每个单元格的对象
                System.out.println(cell.getStringCellValue());                                       //输出每一个单元格的内容
            }
        }
        excel.close();
    }
        @Test
    //使用POI写入数据,并使用数输出流保存到磁盘上
    public  void test3() throws IOException {
        XSSFWorkbook excel = new XSSFWorkbook();//在内出中创建一个工作簿
        XSSFSheet sheet = excel.createSheet("黑马之旅");//创建一个工作表
        XSSFRow titleRow = sheet.createRow(0); //创建第一行  作为标题行
        
         titleRow.createCell(0).setCellValue("姓名");//创建第1个单元格
         titleRow.createCell(1).setCellValue("地址");//创建第2个单元格
         titleRow.createCell(2).setCellValue("年龄");//创建第3个单元格

        XSSFRow dataRow = sheet.createRow(1);//创建第二行 作为数据行
        dataRow.createCell(0).setCellValue("小红");//创建第1个单元格
        dataRow.createCell(1).setCellValue("上海");//创建第2个单元格
        dataRow.createCell(2).setCellValue("24");//创建第3个单元格

        FileOutputStream out = new FileOutputStream(new File("e://蚂蚁牙黑黑2.xlsx"));
        excel.write(out);
        excel.close();

    }


}
