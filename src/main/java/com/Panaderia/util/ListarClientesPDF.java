
package com.Panaderia.util;

import com.Panaderia.Modelo.Clientes;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;

@Component
public class ListarClientesPDF {
      public void exportarPDF(List<Clientes> clientes, OutputStream outputStream) throws Exception {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.addNewPage();
        Document document = new Document(pdfDoc);
         // Logo
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

        // Título
        document.add(new Paragraph("LISTADO GENERAL DE CLIENTES").setBold().setFontSize(16));

        // Tabla
        float[] columnWidths = {40, 100, 100, 150, 80};
        Table table = new Table(columnWidths);

        table.addHeaderCell("ID");
        table.addHeaderCell("Nombre");
        table.addHeaderCell("Apellidos");
        table.addHeaderCell("Correo");
        table.addHeaderCell("Teléfono");

        for (Clientes cliente : clientes) {
            table.addCell(String.valueOf(cliente.getIdCli()));
            table.addCell(cliente.getNombreCli());
            table.addCell(cliente.getApellidosCli());
            table.addCell(cliente.getCorreo());
            table.addCell(cliente.getTelefono());
        }

        document.add(table);
        document.close();
}
}