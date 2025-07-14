package com.Panaderia.util;

import com.Panaderia.Modelo.Pedido;
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

@Component("adminpedidos.xlsx") 
public class ListarPedidosExcel extends AbstractXlsxView  {

   @Override
protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {

    response.setHeader("Content-Disposition", "attachment; filename=pedidos.xlsx");
    Sheet sheet = workbook.createSheet("Pedidos");

 
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


    Row filaTitulo = sheet.createRow(1);
    sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 7));
    Cell celdaTitulo = filaTitulo.createCell(3);
    celdaTitulo.setCellValue("LISTADO DE PEDIDOS");

    CellStyle estiloTitulo = workbook.createCellStyle();
    Font fuenteTitulo = workbook.createFont();
    fuenteTitulo.setBold(true);
    fuenteTitulo.setFontHeightInPoints((short) 14);
    estiloTitulo.setFont(fuenteTitulo);
    estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
    celdaTitulo.setCellStyle(estiloTitulo);


    Row filaEncabezados = sheet.createRow(3);
    String[] columnas = {"ID", "Cliente", "Fecha Pedido", "Estado", "Total"};

    CellStyle estiloEncabezado = workbook.createCellStyle();
    Font fontEncabezado = workbook.createFont();
    fontEncabezado.setBold(true);
    estiloEncabezado.setFont(fontEncabezado);
    estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
    estiloEncabezado.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    estiloEncabezado.setBorderBottom(BorderStyle.THIN);

    for (int i = 0; i < columnas.length; i++) {
        Cell celda = filaEncabezados.createCell(i + 3);
        celda.setCellValue(columnas[i]);
        celda.setCellStyle(estiloEncabezado);
    }

 
    List<Pedido> pedidos = (List<Pedido>) model.get("pedidos");
    int filaNum = 4;

    for (Pedido pedido : pedidos) {
        Row fila = sheet.createRow(filaNum++);
        fila.createCell(3).setCellValue(pedido.getId_pedido());
        fila.createCell(4).setCellValue(pedido.getCliente().getNombreCli());
        fila.createCell(5).setCellValue(pedido.getFecha().toString());
        fila.createCell(6).setCellValue(pedido.getEstado());
        fila.createCell(7).setCellValue(
            pedido.getTotal() != null ? pedido.getTotal().doubleValue() : 0
        );
    }

    // 👉 5. Autoajustar columnas (1 a 5)
     for (int i = 3; i <= 7; i++) {
        sheet.autoSizeColumn(i);
    }
}
}