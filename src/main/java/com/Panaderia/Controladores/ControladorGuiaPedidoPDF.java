package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.util.ListarGuiaPedidoPDF;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adminpedidos")
public class ControladorGuiaPedidoPDF {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private ListarGuiaPedidoPDF listarGuiaPedidoPDF;


    
       @GetMapping("/pdf/{id}")
public void descargarPedidoPdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
    Optional<Pedido> pedidoOpt = pedidoRepositorio.findById(id);

    if (pedidoOpt.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    Pedido pedido = pedidoOpt.get();

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=guia-pedido-" + id + ".pdf");

    listarGuiaPedidoPDF.exportarPDFPedido(pedido, response.getOutputStream());
}
     
    
    
}