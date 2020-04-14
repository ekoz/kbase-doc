/*
 * Powered by http://ibotstat.com
 */
package com.eastrobot.doc.demo;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version v1.0
 * @date 2019/12/30 13:12
 */
public class Tests {

    @Test
    public void test() throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("会计考试");
        addTitle(sheet);

        List<String> list = FileUtils.readLines(new File("E:\\会计成绩名单.txt"), "UTF-8");
        int i=0;
        for (String line : list){
            if (line.contains("麻城")){
                line = line.substring(1, line.length()-1).replace("\"", "");
                System.out.println(line);
                String[] arr = line.split(",");
                i++;
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(arr[0]);
                row.createCell(1).setCellValue(arr[1]);
                row.createCell(2).setCellValue(arr[2]);
                row.createCell(3).setCellValue(arr[3]);
                row.createCell(4).setCellValue(arr[4]);
                row.createCell(5).setCellValue(arr[5]);
                row.createCell(6).setCellValue(arr[6]);
                row.createCell(7).setCellValue(arr[7]);
                row.createCell(8).setCellValue(arr[8]);
                row.createCell(9).setCellValue(arr[9]);
            }
        }
        System.out.println(i);
        //==============================================================


        try (OutputStream fileOut = new FileOutputStream("E://workbook.xlsx")) {
            wb.write(fileOut);
        }
    }

    private void addTitle(Sheet sheet){
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("专业名称");
        row.createCell(1).setCellValue("考号");
        row.createCell(2).setCellValue("学生姓名");
        row.createCell(3).setCellValue("学校名称");
        row.createCell(4).setCellValue("总分");
        row.createCell(5).setCellValue("全省排名");
        row.createCell(6).setCellValue("全省最高分");
        row.createCell(7).setCellValue("全省平均分");
        row.createCell(8).setCellValue("学校平均分");
        row.createCell(9).setCellValue("全省参考人数");
    }

}
