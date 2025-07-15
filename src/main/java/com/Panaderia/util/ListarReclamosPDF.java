package com.Panaderia.util;

import com.Panaderia.Modelo.Reclamo;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;

@Component
public class ListarReclamosPDF {

    public void exportarPDF(List<Reclamo> reclamos, OutputStream outputStream) throws Exception {
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

        document.add(new Paragraph("LISTADO GENERAL DE RECLAMOS").setBold().setFontSize(16));

        float[] columnWidths = {30, 100, 100, 180, 70};
        Table table = new Table(columnWidths);

        table.addHeaderCell("ID");
        table.addHeaderCell("Cliente");
        table.addHeaderCell("Motivo");
        table.addHeaderCell("Detalle");
        table.addHeaderCell("Estado");

        for (Reclamo reclamo : reclamos) {
            table.addCell(String.valueOf(reclamo.getIdReclamo()));
            table.addCell(reclamo.getIdCliente().getNombreCli());
            table.addCell(reclamo.getMotivoReclamo());
            table.addCell(reclamo.getDetalle());
            table.addCell(reclamo.getEstadoReclamo());
        }

        document.add(table);
        document.close();
    }
}
