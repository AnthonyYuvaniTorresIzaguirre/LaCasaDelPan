package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Producto;
import com.Panaderia.util.ListarProductosExcel;
import com.Panaderia.util.ListarProductosPDF;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.Panaderia.dao.ProductoRepositorio;


@Controller
@RequestMapping("/adminproductos")
public class ControladorProductosAdmin{

    @Autowired
    private ProductoRepositorio productoRepositorio;

    private final ListarProductosExcel listarProductosExcel;
    private final ListarProductosPDF listarProductosPDF;

    public ControladorProductosAdmin(ListarProductosExcel listarProductosExcel, ListarProductosPDF listarProductosPDF) {
        this.listarProductosExcel = listarProductosExcel;
        this.listarProductosPDF = listarProductosPDF;
    }

    @GetMapping("/exportar/excel")
    public ModelAndView exportarExcel() {
        List<Producto> productos = productoRepositorio.findAll();
        Map<String, Object> model = Map.of("productos", productos);
        return new ModelAndView(listarProductosExcel, model);
    }

    @GetMapping("/exportar/pdf")
    public void exportarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=productos.pdf");

        List<Producto> productos = productoRepositorio.findAll();

        try {
            listarProductosPDF.exportarPDF(productos, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
