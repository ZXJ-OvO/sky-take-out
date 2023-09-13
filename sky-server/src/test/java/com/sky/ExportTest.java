package com.sky;


import com.sky.entity.CategoryEntity;
import com.sky.entity.EmployeeEntity;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.EmployeeMapper;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author zxj
 * @mail zxjOvO@gmail.com
 * @date 2023/09/13 15:50
 */
@SpringBootTest   // 该注解会启动整个springboot工程，会启动WebSocket导致问题，启动前需要去配置类中注释@Configuration
public class ExportTest {

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Test
    @SneakyThrows
    public void testExportEmployee() {

        List<EmployeeEntity> employeeEntities = employeeMapper.selectAll();

        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("员工信息表");
        for (int i = 0; i < employeeEntities.size(); i++) {
            EmployeeEntity employeeEntity = employeeEntities.get(i);
            sheet.createRow(i).createCell(0).setCellValue(employeeEntity.getId());
            sheet.getRow(i).createCell(1).setCellValue(employeeEntity.getUsername());
            sheet.getRow(i).createCell(2).setCellValue(employeeEntity.getName());
            sheet.getRow(i).createCell(3).setCellValue(employeeEntity.getPassword());
            sheet.getRow(i).createCell(4).setCellValue(employeeEntity.getPhone());
            sheet.getRow(i).createCell(5).setCellValue(employeeEntity.getSex());
            sheet.getRow(i).createCell(6).setCellValue(employeeEntity.getIdNumber());
            sheet.getRow(i).createCell(7).setCellValue(employeeEntity.getStatus());
            sheet.getRow(i).createCell(8).setCellValue(employeeEntity.getCreateTime().toString());
            sheet.getRow(i).createCell(9).setCellValue(employeeEntity.getUpdateTime().toString());
            sheet.getRow(i).createCell(10).setCellValue(employeeEntity.getCreateUser());
            sheet.getRow(i).createCell(11).setCellValue(employeeEntity.getUpdateUser());
        }

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\employee.xlsx");
        excel.write(fileOutputStream);
        fileOutputStream.close();
        excel.close();
    }


    @Test
    @SneakyThrows
    public void testExportCategory() {

        List<CategoryEntity> categoryEntityList = categoryMapper.selectList(null);

        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("员工信息表");
        for (int i = 0; i < categoryEntityList.size(); i++) {
            CategoryEntity categoryEntity = categoryEntityList.get(i);
            sheet.createRow(i).createCell(0).setCellValue(categoryEntity.getId());
            sheet.getRow(i).createCell(1).setCellValue(categoryEntity.getType());
            sheet.getRow(i).createCell(2).setCellValue(categoryEntity.getName());
            sheet.getRow(i).createCell(3).setCellValue(categoryEntity.getSort());
            sheet.getRow(i).createCell(4).setCellValue(categoryEntity.getStatus());
            sheet.getRow(i).createCell(5).setCellValue(categoryEntity.getCreateTime().toString());
            sheet.getRow(i).createCell(5).setCellValue(categoryEntity.getUpdateTime().toString());
            sheet.getRow(i).createCell(5).setCellValue(categoryEntity.getCreateUser());
            sheet.getRow(i).createCell(5).setCellValue(categoryEntity.getUpdateUser());
        }

        FileOutputStream fileOutputStream = new FileOutputStream("D:\\category.xlsx");
        excel.write(fileOutputStream);
        fileOutputStream.close();
        excel.close();
    }
}
