package com.Panaderia.util;

import com.Panaderia.Modelo.Producto;
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

@Component("adminproductos.xlsx")
public class ListarProductosExcel extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=productos.xlsx");
        Sheet sheet = workbook.createSheet("Productos");

        InputStream logoStream = new ClassPathResource("static/img/logoMundoPan.jpg").getInputStream();
        byte[] bytes = IOUtils.toByteArray(logoStream);
        logoStream.close();

        int logoIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
        anchor.setCol1(0); anchor.setRow1(0); anchor.setCol2(3); anchor.setRow2(3);
        drawing.createPicture(anchor, logoIndex).resize(1.0);

        Row filaTitulo = sheet.createRow(1);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 9));
        Cell celdaTitulo = filaTitulo.createCell(3);
        celdaTitulo.setCellValue("LISTADO DE PRODUCTOS");

        CellStyle estiloTitulo = workbook.createCellStyle();
        Font fuenteTitulo = workbook.createFont();
        fuenteTitulo.setBold(true);
        fuenteTitulo.setFontHeightInPoints((short) 14);
        estiloTitulo.setFont(fuenteTitulo);
        estiloTitulo.setAlignment(HorizontalAlignment.CENTER);
        celdaTitulo.setCellStyle(estiloTitulo);

        Row encabezados = sheet.createRow(3);
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría", "Imagen"};

        CellStyle estiloEncabezado = workbook.createCellStyle();
        Font fuenteEncabezado = workbook.createFont();
        fuenteEncabezado.setBold(true);
        estiloEncabezado.setFont(fuenteEncabezado);
        estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
        estiloEncabezado.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estiloEncabezado.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < columnas.length; i++) {
            Cell celda = encabezados.createCell(i + 3);
            celda.setCellValue(columnas[i]);
            celda.setCellStyle(estiloEncabezado);
        }

        List<Producto> productos = (List<Producto>) model.get("productos");
        int filaNum = 4;

        for (Producto producto : productos) {
            Row fila = sheet.createRow(filaNum++);
            fila.createCell(3).setCellValue(producto.getId_prod());
            fila.createCell(4).setCellValue(producto.getNombre());
            fila.createCell(5).setCellValue(producto.getDescripcion());
            fila.createCell(6).setCellValue(producto.getPrecio());
            fila.createCell(7).setCellValue(producto.getStock());
            fila.createCell(8).setCellValue(producto.getCategoria());
            fila.createCell(9).setCellValue(producto.getImagen() != null ? producto.getImagen() : "-");
        }

        for (int i = 3; i <= 9; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
