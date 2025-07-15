
package com.Panaderia.util;

import com.Panaderia.Modelo.Clientes;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component("adminclientes.xlsx")
public class ListarClientesExcel  extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
          response.setHeader("Content-Disposition", "attachment; filename=clientes.xlsx");
        Sheet sheet = workbook.createSheet("Clientes");

        // Logo
        InputStream logoStream = new ClassPathResource("static/img/logoMundoPan.jpg").getInputStream();
        byte[] bytes = IOUtils.toByteArray(logoStream);
        logoStream.close();

        int logoIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(0);
        anchor.setCol2(3);
        anchor.setRow2(3);
        drawing.createPicture(anchor, logoIndex).resize(1.0);

        // Título
        Row filaTitulo = sheet.createRow(1);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 6));
        Cell celdaTitulo = filaTitulo.createCell(3);
        celdaTitulo.setCellValue("LISTADO DE CLIENTES");

        CellStyle estiloTitulo = workbook.createCellStyle();
        Font fuenteTitulo = workbook.createFont();
        fuenteTitulo.setBold(true);
        fuenteTitulo.setFontHeightInPoints((short) 14);
        estiloTitulo.setFont(fuenteTitulo);
        estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
        celdaTitulo.setCellStyle(estiloTitulo);

        // Encabezados
        Row filaEncabezados = sheet.createRow(3);
        String[] columnas = {"ID", "Nombre", "Apellidos", "Correo", "Teléfono"};

        CellStyle estiloEncabezado = workbook.createCellStyle();
        Font fontEncabezado = workbook.createFont();
        fontEncabezado.setBold(true);
        estiloEncabezado.setFont(fontEncabezado);
        estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
        estiloEncabezado.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < columnas.length; i++) {
            Cell celda = filaEncabezados.createCell(i + 3);
            celda.setCellValue(columnas[i]);
            celda.setCellStyle(estiloEncabezado);
        }

        // Datos
        List<Clientes> clientes = (List<Clientes>) model.get("clientes");
        int filaNum = 4;

        for (Clientes cliente : clientes) {
            Row fila = sheet.createRow(filaNum++);
            fila.createCell(3).setCellValue(cliente.getIdCli());
            fila.createCell(4).setCellValue(cliente.getNombreCli());
            fila.createCell(5).setCellValue(cliente.getApellidosCli());
            fila.createCell(6).setCellValue(cliente.getCorreo());
            fila.createCell(7).setCellValue(cliente.getTelefono());
        }

        for (int i = 3; i <= 7; i++) {
            sheet.autoSizeColumn(i);
        } 
    }
    
}
