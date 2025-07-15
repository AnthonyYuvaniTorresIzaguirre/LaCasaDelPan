package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Repositorio.ClientesRepositorio;
import com.Panaderia.util.ListarClientesExcel;
import com.Panaderia.util.ListarClientesPDF;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/adminclientes")
public class ControladorClienteAdmin {

    @Autowired
    private ClientesRepositorio clienteRepositorio;

    private final ListarClientesExcel listarClientesExcel;
    private final ListarClientesPDF listarClientesPDF;

    public ControladorClienteAdmin(ListarClientesExcel listarClientesExcel, ListarClientesPDF listarClientesPDF) {
        this.listarClientesExcel = listarClientesExcel;
        this.listarClientesPDF = listarClientesPDF;
    }

    @GetMapping("/exportar/excel")
    public ModelAndView exportarExcel() {
        List<Clientes> clientes = clienteRepositorio.findAll();
        Map<String, Object> model = Map.of("clientes", clientes);
        return new ModelAndView(listarClientesExcel, model);
    }

    @GetMapping("/exportar/pdf")
    public void exportarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=clientes.pdf");

        List<Clientes> clientes = clienteRepositorio.findAll();

        try {
            listarClientesPDF.exportarPDF(clientes, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
