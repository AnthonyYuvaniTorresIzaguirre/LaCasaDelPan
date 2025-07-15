package com.Panaderia.util;

import com.Panaderia.Modelo.Producto;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;

@Component
public class ListarProductosPDF {

    public void exportarPDF(List<Producto> productos, OutputStream outputStream) throws Exception {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.addNewPage();
        Document document = new Document(pdfDoc);

        String imagePath = "src/main/resources/static/img/logoMundoPan.jpg";
        Image img = new Image(ImageDataFactory.create(imagePath));
        img.setOpacity(0.3f);
        img.scaleToFit(300, 300);

        PdfPage page = pdfDoc.getFirstPage();
        Rectangle pageSize = page.getPageSize();
        float x = (pageSize.getWidth() - img.getImageScaledWidth()) / 2;
        float y = (pageSize.getHeight() - img.getImageScaledHeight()) / 2;
        img.setFixedPosition(x, y);
        document.add(img);

        document.add(new Paragraph("LISTADO GENERAL DE PRODUCTOS").setBold().setFontSize(16));

        float[] columnWidths = {30, 100, 150, 60, 50, 100, 120};
        Table table = new Table(columnWidths);

        table.addHeaderCell("ID");
        table.addHeaderCell("Nombre");
        table.addHeaderCell("Descripción");
        table.addHeaderCell("Precio");
        table.addHeaderCell("Stock");
        table.addHeaderCell("Categoría");
        table.addHeaderCell("Imagen");

        for (Producto producto : productos) {
            table.addCell(String.valueOf(producto.getId_prod()));
            table.addCell(producto.getNombre());
            table.addCell(producto.getDescripcion());
            table.addCell(String.format("S/ %.2f", producto.getPrecio()));
            table.addCell(String.valueOf(producto.getStock()));
            table.addCell(producto.getCategoria());
            table.addCell(producto.getImagen() != null ? producto.getImagen() : "-");
        }

        document.add(table);
        document.close();
    }
}
