package com.xiafei.tools.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <P>Description: Excel工具. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2018/4/8</P>
 * <P>UPDATE DATE: 2018/4/8</P>
 *
 * @author qixiafei
 * @version 1.0
 * @since java 1.8.0
 */
@Slf4j
public class ExcelUtil {

    /**
     * Excel导出使用的字体名称.
     */
    private static final String FONT_NAME = "黑体";

    public static void main(String[] args) throws IOException {
//        String sheetName = "用车统计表单";
//        String titleName = "用车申请数据统计表";
//        String fileName = "用车申请统计表单";
//        int columnNumber = 3;
//        int[] columnWidth = {10, 20, 30};
//        String[][] dataList = {{"001", "2015-01-01", "IT"},
//                {"002", "2015-01-02", "市场部"}, {"003", "2015-01-03", "测试"}};
//        String[] columnName = {"单号", "申请时间", "申请部门"};
//
//        new ExcelUtil().export(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList,
//                new FileOutputStream(new File("./temp/test.xlsx")), "123");
        new ExcelUtil().read(new FileInputStream(new File("./temp/test.xlsx")), "test.xlsx", new ArrayList<>());
    }

    public void export(final String sheetName, final String titleName, final String fileName,
                       final int columnNumber, final int[] columnWidth, final String[] columnName,
                       final String[][] dataList, final HttpServletResponse response, final String uuid)
            throws IOException {
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="
                .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        export(sheetName, titleName, fileName, columnNumber, columnWidth, columnName, dataList, response.getOutputStream(), uuid);
    }

    private void export(final String sheetName, final String titleName, final String fileName,
                        final int columnNumber, final int[] columnWidth, final String[] columnName,
                        final String[][] dataList, final OutputStream out, final String uuid) throws IOException {
        if (columnNumber == columnWidth.length && columnWidth.length == columnName.length) {
            // 第一步，创建一个webbook，对应一个Excel文件
            Workbook wb;
            if (fileName.endsWith(".xls")) {
                wb = new HSSFWorkbook();
            } else {
                wb = new XSSFWorkbook();
            }
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            Sheet sheet = wb.createSheet(sheetName);
            // sheet.setDefaultColumnWidth(15); //统一设置列宽
            for (int i = 0; i < columnNumber; i++) {
                sheet.setColumnWidth(i, columnWidth[i] * 256); // 单独设置每列的宽
            }
            // 创建第0行 也就是标题
            Row title = sheet.createRow(0);
            title.setHeightInPoints(50);// 设备标题的高度
            // 第三步创建标题的单元格样式style2以及字体样式headerFont1
            CellStyle titleStyle = wb.createCellStyle();
            titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            titleStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
            titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            Font titleFont = wb.createFont(); // 创建字体样式
            titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
            titleFont.setFontName(FONT_NAME); // 设置字体类型
            titleFont.setFontHeightInPoints((short) 15); // 设置字体大小
            titleStyle.setFont(titleFont); // 为标题样式设置字体样式
            Cell titleCell = title.createCell(0);// 创建标题第一列
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
                    columnNumber - 1)); // 合并列标题
            titleCell.setCellValue(titleName); // 设置值标题
            titleCell.setCellStyle(titleStyle); // 设置标题样式

            // 创建第1行 也就是表头
            Row header = sheet.createRow(1);
            header.setHeightInPoints(37);// 设置表头高度

            // 第四步，创建表头单元格样式 以及表头的字体样式
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setWrapText(true);// 设置自动换行
            headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式

            headerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
            headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            Font headerFont = wb.createFont(); // 创建字体样式
            headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 字体加粗
            headerFont.setFontName(FONT_NAME); // 设置字体类型
            headerFont.setFontHeightInPoints((short) 10); // 设置字体大小
            headerStyle.setFont(headerFont); // 为标题样式设置字体样式

            // 第四.一步，创建表头的列
            for (int i = 0; i < columnNumber; i++) {
                Cell cell = header.createCell(i);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(columnName[i]);
            }

            // 为数据内容设置特点新单元格样式2 自动换行 上下居中左右也居中
            CellStyle dataStyle = wb.createCellStyle();
            dataStyle.setWrapText(true);// 设置自动换行
            dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个上下居中格式
            dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
            // 设置边框
            dataStyle.setBottomBorderColor(HSSFColor.BLACK.index);
            dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            // 第五步，创建单元格，并设置值
            for (int i = 0; i < dataList.length; i++) {
                Row dataRow = sheet.createRow(i + 2);
                for (int j = 0; j < columnNumber; j++) {
                    final Cell datacell = dataRow.createCell(j);
                    datacell.setCellValue(dataList[i][j]);
                    datacell.setCellStyle(dataStyle);
                }
            }

            // 第六步，将文件存到浏览器设置的下载位置
            try {
                wb.write(out);// 将数据写出去
                log.info("[{}]导出Excel成功", uuid);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        } else {
            log.error("[{}]传入参数不匹配", uuid);
        }

    }

    public void read(InputStream is, final String fileName, List<Map<String, Object>> dataList) throws IOException {
        final Workbook wb;
        if (fileName.endsWith(".xls")) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        try {
            final Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() < 2) { // rowNum从0开始是第一行，这里假设标题有两行，跳过标题数据
                    continue;
                }
                System.out.println("rowNum:" + row.getRowNum());
                System.out.println(row.getCell(0).getStringCellValue());
                System.out.println(row.getCell(1).getStringCellValue());
                System.out.println(row.getCell(2).getStringCellValue());
            }
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }

    }
}
