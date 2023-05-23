package com.shopme.admin.Util;

import com.shopme.common.entity.User;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserExcelExporter extends AbstractExporter{
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
       workbook=new XSSFWorkbook();
    }

    public void writeHeaderLine(){
        sheet=workbook.createSheet("Users");
        XSSFRow row=sheet.createRow(0);

        XSSFCellStyle cellStyle=workbook.createCellStyle();
        XSSFFont font=workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);
        createCell(row,0,"User ID",cellStyle);
        createCell(row,1,"Email",cellStyle);
        createCell(row,2,"First Name",cellStyle);
        createCell(row,3,"Last Name",cellStyle);
        createCell(row,4,"Roles",cellStyle);
        createCell(row,5,"Enable",cellStyle);
    }


    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style){
        XSSFCell cell=row.createCell(columnIndex);

        sheet.autoSizeColumn(columnIndex);
        if (value instanceof Integer){
            cell.setCellValue((Integer)value);
        } else if (value instanceof Boolean ) {
            cell.setCellValue((Boolean)value);
        }else{
            cell.setCellValue((String)value );
        }

        cell.setCellStyle(style);
    }
    public void export(List<User>listUser, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/octet-stream",".xlsx");
        writeHeaderLine();
        writeDataLines(listUser);
        ServletOutputStream outputStream=response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeDataLines(List<User> listUser) {
        XSSFCellStyle cellStyle=workbook.createCellStyle();
        XSSFFont font=workbook.createFont();
        font.setFontHeight(14);
        int rowIndex=1;
        for (User user : listUser) {
            XSSFRow row=sheet.createRow(rowIndex++);
           int columnIndex=0;
           createCell(row,columnIndex++,user.getId(),cellStyle);
           createCell(row,columnIndex++,user.getEmail(),cellStyle);
           createCell(row,columnIndex++,user.getFirstName(),cellStyle);
           createCell(row,columnIndex++,user.getLastName(),cellStyle);
           createCell(row,columnIndex++,user.getRoles().toString(),cellStyle);
           createCell(row,columnIndex++,user.isEnabled(),cellStyle);
        }
    }


}
