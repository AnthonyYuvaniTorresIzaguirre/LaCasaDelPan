package com.Panaderia.Servicios;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Rol;
import com.Panaderia.Repositorio.ClientesRepositorio;
import com.Panaderia.Repositorio.RolRepositorio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientesServicio {

    @Autowired
    private ClientesRepositorio clienteRepository;

    @Autowired
    private RolRepositorio rolRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void asignarRolACliente(Integer clienteId, String rolNombre) {
        Clientes cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        cliente.getRoles().add(rol);
        clienteRepository.save(cliente);
    }

    public List<Clientes> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    public void agregarCliente(Clientes cliente) {
          String passwordEncriptada = passwordEncoder.encode(cliente.getContraseña());
    cliente.setContraseña(passwordEncriptada);
        clienteRepository.save(cliente); // Guarda el cliente en la base de datos
    }

    public void borrarCliente(Integer id) {
        clienteRepository.deleteById(id); // Elimina el cliente por ID
    }

    public Optional<Clientes> findClienteByCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }
    public long contarClientes() {
        return clienteRepository.count();
    }

    @Transactional
    public void asignarRolPorDefecto(Clientes cliente) {
        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_USER no encontrado"));
        cliente.getRoles().add(rolUser);
    }
}

