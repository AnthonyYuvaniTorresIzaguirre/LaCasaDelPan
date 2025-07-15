package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Reclamo;
import com.Panaderia.Repositorio.ReclamoRepositorio;
import com.Panaderia.util.ListarReclamosExcel;
import com.Panaderia.util.ListarReclamosPDF;

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
@RequestMapping("/adminreclamos")
public class ControladorReclamosAdmin {

    @Autowired
    private ReclamoRepositorio reclamoRepositorio;

    private final ListarReclamosExcel listarReclamosExcel;
    private final ListarReclamosPDF listarReclamosPDF;

    public ControladorReclamosAdmin(ListarReclamosExcel listarReclamosExcel, ListarReclamosPDF listarReclamosPDF) {
        this.listarReclamosExcel = listarReclamosExcel;
        this.listarReclamosPDF = listarReclamosPDF;
    }

    @GetMapping("/exportar/excel")
    public ModelAndView exportarExcel() {
        List<Reclamo> reclamos = reclamoRepositorio.findAll();
        Map<String, Object> model = Map.of("reclamos", reclamos);
        return new ModelAndView(listarReclamosExcel, model);
    }

    @GetMapping("/exportar/pdf")
    public void exportarPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reclamos.pdf");

        List<Reclamo> reclamos = reclamoRepositorio.findAll();

        try {
            listarReclamosPDF.exportarPDF(reclamos, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
