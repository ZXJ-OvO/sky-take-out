package com.sky;


import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

/**
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/09/13 10:20
 */
public class POITest {


    @Test
    @SneakyThrows
    public void write() {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        // 创建sheet页
        XSSFSheet sheet = xssfWorkbook.createSheet("test-poi");

        // 创建行、列、值
        sheet.createRow(0).createCell(0).setCellValue("黑马冲击波冲冲冲");

        // 将内存中的excel写入磁盘
        xssfWorkbook.write(new FileOutputStream("C:\\Users\\root\\Desktop\\test-poi.xlsx"));

        // 关闭资源
        xssfWorkbook.close();
    }

    @Test
    @SneakyThrows
    public void read() {
        String value = new XSSFWorkbook(new FileInputStream("C:\\Users\\root\\Desktop\\test-poi.xlsx")).getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
        System.out.println(value);
    }

    @Test
    @SneakyThrows
    public void test1() {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        xssfWorkbook.createSheet("test1").createRow(0).createCell(0).setCellValue("黑马冲击波");
        xssfWorkbook.write(new FileOutputStream("C:\\Users\\root\\Desktop\\test1.xlsx"));
        xssfWorkbook.close();
    }

    @Test
    @SneakyThrows
    public void test2() {
        String value = new XSSFWorkbook(new FileInputStream("C:\\Users\\root\\Desktop\\test1.xlsx")).getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
        System.out.println(value);
    }

    @Test
    @SneakyThrows
    public void test3() {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet("test3");
        sheet.createRow(0).createCell(0).setCellValue(String.valueOf(LocalDateTime.now()));
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\root\\Desktop\\test1.xlsx");
        xssfWorkbook.write(fileOutputStream);
        fileOutputStream.close();
        xssfWorkbook.close();
    }

    @Test
    @SneakyThrows
    public void test4() {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\root\\Desktop\\test1.xlsx");
        XSSFWorkbook sheets = new XSSFWorkbook(fileInputStream);
        String value = sheets.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
        System.out.println(value);
        sheets.close();
        fileInputStream.close();
    }

    @Test
    @SneakyThrows
    public void test5() {
        XSSFWorkbook sheets = new XSSFWorkbook();
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\root\\Desktop\\test1.xlsx");
        sheets.createSheet("333").createRow(0).createCell(0).setCellValue("333");
        sheets.write(fileOutputStream);
        fileOutputStream.close();
        sheets.close();
    }

    @Test
    @SneakyThrows
    public void test6() {
    }

    @Test
    @SneakyThrows
    public void test7() {
    }

    @Test
    @SneakyThrows
    public void test8() {
    }
}
