package  com.Panaderia.util;
import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Modelo.PedidoItem;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.util.List;

@Component
public class ListarGuiaPedidoPDF {
public void exportarPDFPedido(Pedido pedido, OutputStream outputStream) throws Exception {
    PdfWriter writer = new PdfWriter(outputStream);
    PdfDocument pdfDoc = new PdfDocument(writer);
    pdfDoc.addNewPage();
    Document document = new Document(pdfDoc);

  
      String imgPath = "src/main/resources/static/img/logoMundoPan.jpg";
        try {
            Image img = new Image(ImageDataFactory.create(imgPath));
            img.setOpacity(0.3f);
            img.scaleToFit(300, 300);

            PdfPage page = pdfDoc.getFirstPage();
            Rectangle pageSize = page.getPageSize();
            float x = (pageSize.getWidth() - img.getImageScaledWidth()) / 2;
            float y = (pageSize.getHeight() - img.getImageScaledHeight()) / 2;
            img.setFixedPosition(x, y);
            
        document.add(img);
    } catch (Exception ignored) {}

    document.add(new Paragraph("GUÍA DE PEDIDO").setBold().setFontSize(16).setMarginBottom(20));
    document.add(new Paragraph("Pedido ID: " + pedido.getId_pedido()).setBold());
    document.add(new Paragraph("Estado: " + pedido.getEstado()));

    float[] widths = {200, 60, 80, 80};
    Table table = new Table(widths);
    table.addHeaderCell(new Cell().add(new Paragraph("Producto")));
    table.addHeaderCell(new Cell().add(new Paragraph("Cant.")));
    table.addHeaderCell(new Cell().add(new Paragraph("Precio unit.")));
    table.addHeaderCell(new Cell().add(new Paragraph("Subtotal")));

    double totalPedido = 0;

    for (PedidoItem item : pedido.getItems()) {
        table.addCell(item.getIdProducto().getNombre());
        table.addCell(String.valueOf(item.getCantidad()));
        table.addCell("S/ " + item.getPrecioUnitario());
        table.addCell("S/ " + item.getTotal());
        totalPedido += item.getTotal().doubleValue();
    }

    document.add(table);
    document.add(new Paragraph("\nTotal Pedido: S/ " + String.format("%.2f", totalPedido))
            .setBold()
            .setFontSize(12)
            .setMarginTop(10));

    document.close();
}
   
}