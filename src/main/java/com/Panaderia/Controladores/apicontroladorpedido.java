package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.Servicios.PedidoServicio;
import com.Panaderia.dto.ClientesDTO;
import com.Panaderia.dto.PedidoDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/adminventas")
public class apicontroladorpedido {

    @Autowired
    private PedidoRepositorio pedidoRepository;
      @Autowired
    private PedidoServicio pedidoServicio;

    @GetMapping("/listar")
    public List<PedidoDTO> listarPedidos() {
        return pedidoServicio.listarPedidos();       
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedido(@PathVariable Long id) {
        PedidoDTO pedido = pedidoServicio.listarPedidos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
        return ResponseEntity.ok(pedido);
    }


@PostMapping("/editar/{id}")
public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Long id, @RequestBody Map<String, String> body) {
    try {
        String estado = body.get("estado").toLowerCase().trim();
        if (!List.of("pendiente", "en proceso", "entregado", "cancelado").contains(estado)) {
            return ResponseEntity.badRequest().body("Estado inválido");
        }
        pedidoServicio.actualizarEstadoPedido(id, estado);
        return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al actualizar estado del pedido");
    }
}

   /* @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
        pedidoServicio.eliminarPedido(id);
        return ResponseEntity.ok().build();
    }*/
    
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoDTO>> listarMisPedidos(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // No autorizado
        }

        String correo = authentication.getName(); // obtiene el correo del usuario autenticado
        Clientes cliente = pedidoServicio.obtenerClientePorCorreo(correo); // Asegúrate de tener este método
        if (cliente == null) {
            return ResponseEntity.status(404).build(); // cliente no encontrado
        }

        Integer idCliente = cliente.getIdCli();
        List<PedidoDTO> pedidos = pedidoServicio.listarPedidosPorCliente(idCliente);
        return ResponseEntity.ok(pedidos);
    }
}