package com.Panaderia.util;

import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Modelo.PedidoItem;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListarPedidosPDF {

    public void exportarPDF(List<Pedido> pedidos, OutputStream outputStream) throws Exception {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.addNewPage();
        Document document = new Document(pdfDoc);

        // Logo (ajusta la ruta según tu estructura)
        String imagePath = "src/main/resources/static/img/logoMundoPaN.jpg";
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
        document.add(new Paragraph("LISTADO GENERAL DE PEDIDOS").setBold().setFontSize(16));

        // Columnas
        float[] columnWidths = {30, 90, 70, 130, 40, 60, 60};
        Table table = new Table(columnWidths);

        // Encabezados
        table.addHeaderCell("ID");
        table.addHeaderCell("Cliente");
        table.addHeaderCell("Fecha");
        table.addHeaderCell("Pedido");
        table.addHeaderCell("Cant.");
        table.addHeaderCell("Monto");
        table.addHeaderCell("Estado");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Pedido pedido : pedidos) {
            String clienteNombre = pedido.getCliente().getNombreCli() + " " + pedido.getCliente().getApellidosCli();
            String fecha = pedido.getFecha() != null ? pedido.getFecha().format(formatter) : "-";

            // Productos
            String productos = pedido.getItems() != null
                    ? pedido.getItems().stream()
                        .map(i -> i.getIdProducto().getNombre())
                        .collect(Collectors.joining(", "))
                    : "-";

            // Cantidad total
            int cantidad = pedido.getItems() != null
                    ? pedido.getItems().stream()
                        .mapToInt(i -> i.getCantidad() != null ? i.getCantidad() : 0)
                        .sum()
                    : 0;

            // Total monto (usamos el campo total del pedido directamente)
            BigDecimal total = pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO;

            // Estado
            String estado = pedido.getEstado() != null ? pedido.getEstado() : "-";

            table.addCell(String.valueOf(pedido.getId_pedido()));
            table.addCell(clienteNombre);
            table.addCell(fecha);
            table.addCell(productos);
            table.addCell(String.valueOf(cantidad));
            table.addCell("$" + total.setScale(2, BigDecimal.ROUND_HALF_UP));
            table.addCell(estado);
        }

        document.add(table);
        document.close();
    }
}