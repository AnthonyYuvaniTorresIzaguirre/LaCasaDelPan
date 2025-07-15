package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Producto;
import com.Panaderia.Servicios.ClientesServicio;
import com.Panaderia.Servicios.ProductoServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

@Controller
public class ControladorAdminProductos {

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private ClientesServicio clientesServicio;
    
    @GetMapping("/adminproductos")
    public String listarProductos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categoria,
            Model model) {

        agregarNombreUsuarioAlModelo(model);


        List<Producto> productos;

        boolean hayCategoria = categoria != null && !categoria.isEmpty() && !categoria.equals("--Todas--");
        boolean hayBusqueda = search != null && !search.isEmpty();

        if (hayCategoria && hayBusqueda) {
            productos = productoServicio.buscarPorTextoYCategoria(search, categoria);
        } else if (hayCategoria) {
            productos = productoServicio.getByCategoria(categoria);
        } else if (hayBusqueda) {
            productos = productoServicio.buscar(search);
        } else {
            productos = productoServicio.get();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", productoServicio.get().stream().map(Producto::getCategoria).distinct().toList());
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("search", search);
        // Total productos
        long totalProductos = productos.size();
        model.addAttribute("totalProductos", totalProductos);

        // Total categorías
        long totalCategorias = productos.stream()
                .map(Producto::getCategoria)
                .filter(c -> c != null && !c.trim().isEmpty())
                .distinct()
                .count();
        model.addAttribute("totalCategorias", totalCategorias);

        // Productos sin stock
        long productosSinStock = productos.stream()
                .filter(p -> p.getStock() == 0)
                .count();
        model.addAttribute("productosSinStock", productosSinStock);

        // Productos con stock bajo
        long productosStockBajo = productos.stream()
                .filter(p -> p.getStock() > 0 && p.getStock() <= 10)
                .count();
        model.addAttribute("productosStockBajo", productosStockBajo);

        return "adminproductos";
    }

    @PostMapping("/adminproductos/guardar")
    public String guardarProducto(@Valid @ModelAttribute Producto producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("producto", producto);
            model.addAttribute("modalError", true);  // abrir modal en la vista
            return "adminproductos";
        }
        productoServicio.save(producto);
        return "redirect:/adminproductos";
    }

    @GetMapping("/adminproductos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoServicio.get(id));
        return "adminproductos_form";
    }

    @DeleteMapping("/adminproductos/eliminar/{id}")
@ResponseBody
public ResponseEntity<String> eliminarProducto(@PathVariable Long id) {
    if (productoServicio.estaEnPedido(id)) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body("El producto está siendo utilizado en un pedido y no puede ser eliminado.");
    }

    productoServicio.delete(id);
    return ResponseEntity.ok("Producto eliminado correctamente.");
}

    @GetMapping("/listapanes")
    public String listaPanes(Model modelo) {
        List<Producto> lista = productoServicio.getByCategoria("Panes");
        modelo.addAttribute("listaproductos", lista);
        agregarNombreClienteAlModelo(modelo);
        return "ListaPanes";
    }

    @GetMapping("/listakekes")
    public String listaKekes(Model modelo) {
        List<Producto> lista = productoServicio.getByCategoria("Kekes");
        modelo.addAttribute("listaproductos", lista);
        agregarNombreClienteAlModelo(modelo);
        return "ListaKekes";
    }

    @GetMapping("/listatortas")
    public String listaTortas(Model modelo) {
        List<Producto> lista = productoServicio.getByCategoria("Tortas");
        modelo.addAttribute("listaproductos", lista);
        agregarNombreClienteAlModelo(modelo);
        return "ListaTortas";
    }

    @GetMapping("/listabocaditos")
    public String listaBocaditos(Model modelo) {
        List<Producto> lista = productoServicio.getByCategoria("Bocaditos");
        modelo.addAttribute("listaproductos", lista);
        agregarNombreClienteAlModelo(modelo);
        return "ListaBocaditos";
    }

    @GetMapping("/listasalados")
    public String listaSalados(Model modelo) {
        List<Producto> lista = productoServicio.getByCategoria("Salados Personales");
        modelo.addAttribute("listaproductos", lista);
        agregarNombreClienteAlModelo(modelo);
        return "ListaSalados";
    }

    private void agregarNombreClienteAlModelo(Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            String correo = authentication.getName();
            Optional<Clientes> clienteOpt = clientesServicio.findClienteByCorreo(correo);

            if (clienteOpt.isPresent()) {
                modelo.addAttribute("nombreCliente", clienteOpt.get().getNombreCli());
                return;
            }
        }
        modelo.addAttribute("nombreCliente", "Invitado");
    }
    
    private void agregarNombreUsuarioAlModelo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String correo = auth.getName();
            clientesServicio.findClienteByCorreo(correo).ifPresentOrElse(
                cliente -> model.addAttribute("nombreUsuario", cliente.getNombreCli()),
                () -> model.addAttribute("nombreUsuario", correo)
            );
        } else {
            model.addAttribute("nombreUsuario", "Invitado");
        }
    }

}
