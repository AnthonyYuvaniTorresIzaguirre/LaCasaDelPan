package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Repositorio.ClientesRepositorio;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.Servicios.ClientesServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/adminventas")
public class ControladorAdminPedidos {

    private final PedidoRepositorio pedidoRepository;
    private final ClientesRepositorio clienteRepository;
    private final ClientesServicio clientesServicio;

    public ControladorAdminPedidos(PedidoRepositorio pedidoRepository, ClientesRepositorio clienteRepository, ClientesServicio clientesServicio) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.clientesServicio = clientesServicio;
    }

    @GetMapping
    public String listarPedidos(Model model) {
        agregarNombreUsuarioAlModelo(model);
        model.addAttribute("pedidos", pedidoRepository.findAll());
        return "AdminPedidos";
    }

     @PostMapping("/editar/{id}")
    @ResponseBody
    public ResponseEntity<String> actualizarEstado(
            @PathVariable Long id,
            @RequestParam("estado") String estado
    ) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(estado); // Actualizamos el estado del pedido
            pedidoRepository.save(pedido);
            return ResponseEntity.ok(estado); // Retornamos el nuevo estado del pedido
        }).orElse(ResponseEntity.notFound().build());
    }
    
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        agregarNombreUsuarioAlModelo(model);
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("clientes", clienteRepository.findAll());
        return "FormularioPedido";
    }

    @PostMapping("/guardar")
    public String guardarPedido(@ModelAttribute Pedido pedido) {
        pedidoRepository.save(pedido);
        return "redirect:/adminventas";
    }

      // Formulario para editar un pedido
     @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        agregarNombreUsuarioAlModelo(model);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("pedido", pedido);
        model.addAttribute("clientes", clienteRepository.findAll());
        return "FormularioPedido";
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
