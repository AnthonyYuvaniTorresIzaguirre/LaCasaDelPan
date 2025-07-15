
package com.Panaderia.Controladores;
import com.Panaderia.Modelo.Clientes;
import com.Panaderia.dto.ClientesDTO;
import com.Panaderia.Repositorio.ClientesRepositorio;
import com.Panaderia.Servicios.ClientesServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/clientes")
public class ControladorClientes {

    @Autowired
    private ClientesServicio clienteService;
    
     @Autowired
    private ClientesRepositorio ClientesRepository;
    
     @Autowired
private PasswordEncoder passwordEncoder;
    
      @GetMapping("/")
public List<ClientesDTO> listarClientes() {
    List<Clientes> clientes = clienteService.obtenerTodosLosClientes();
    return clientes.stream().map(ClientesDTO::new).collect(Collectors.toList());
}
    
    @PostMapping("/")
public ResponseEntity<String> agregarCliente(@RequestBody Clientes cliente) {
    try {
        clienteService.agregarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente creado con éxito");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el cliente");
    }
}

@GetMapping("/{id}")
public ResponseEntity<ClientesDTO> obtenerCliente(@PathVariable Integer id) {
    Optional<Clientes> cliente = ClientesRepository.findById(id);
    if (cliente.isPresent()) {
        return ResponseEntity.ok(new ClientesDTO(cliente.get())); // Retorna los datos del cliente
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna un 404 si el cliente no existe
    }
}


@PutMapping("/{id}")
public ResponseEntity<String> editarCliente(@PathVariable Integer id, @RequestBody Clientes clienteActualizado) {
    Optional<Clientes> clienteExistenteOpt = ClientesRepository.findById(id);

    if (clienteExistenteOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
    }

    Clientes clienteExistente = clienteExistenteOpt.get();

    
    clienteExistente.setNombreCli(clienteActualizado.getNombreCli());
    clienteExistente.setApellidosCli(clienteActualizado.getApellidosCli());
    clienteExistente.setCorreo(clienteActualizado.getCorreo());
    clienteExistente.setDni(clienteActualizado.getDni());
    clienteExistente.setDireccion(clienteActualizado.getDireccion());
    clienteExistente.setTelefono(clienteActualizado.getTelefono());

    //encriptar contraseña si es nueva
    if (clienteActualizado.getContraseña() != null && !clienteActualizado.getContraseña().isBlank()) {
        String nuevaEncriptada = passwordEncoder.encode(clienteActualizado.getContraseña());
        clienteExistente.setContraseña(nuevaEncriptada);
}

    ClientesRepository.save(clienteExistente);
    return ResponseEntity.ok("Cliente actualizado con éxito");
}

    
@DeleteMapping("/{id}")
public ResponseEntity<String> borrarCliente(@PathVariable Integer id) {
    Optional<Clientes> clienteExistente = ClientesRepository.findById(id);
    if (clienteExistente.isPresent()) {
        clienteService.borrarCliente(id);
        return ResponseEntity.ok("Cliente borrado con éxito");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
    }
}


    
    @PostMapping("/{clienteId}/asignarRol")
    public void asignarRol(@PathVariable Integer clienteId, @RequestParam String rol) {
        clienteService.asignarRolACliente(clienteId, rol);
    }

    
}