package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.Servicios.ClientesServicio;
import com.Panaderia.util.ListarPedidosExcel;
import com.Panaderia.util.ListarPedidosPDF;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adminpedidos")
public class ControladorPedidoAdmin {

    @Autowired
    private PedidoRepositorio pedidoRepository;

    @Autowired
    private ClientesServicio clientesServicio;

    private final ListarPedidosExcel listarPedidosExcel;
    private final ListarPedidosPDF listarPedidosPDF;

    // Constructor para inyectar los beans de utilidades
    public ControladorPedidoAdmin(ListarPedidosExcel listarPedidosExcel, ListarPedidosPDF listarPedidosPDF) {
        this.listarPedidosExcel = listarPedidosExcel;
        this.listarPedidosPDF = listarPedidosPDF;
    }

   @GetMapping("/exportar/excel")
public ModelAndView exportarExcel() {
    List<Pedido> pedidos = pedidoRepository.findAll();
    Map<String, Object> model = Map.of("pedidos", pedidos);
    return new ModelAndView(listarPedidosExcel, model);
}

    @GetMapping("/exportar/pdf")
    public void exportarPdf(HttpServletResponse response) throws IOException {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=pedidos.pdf");

    List<Pedido> pedidos = pedidoRepository.findAll();

    try {
        listarPedidosPDF.exportarPDF(pedidos, response.getOutputStream());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}