package com.Panaderia.Servicios;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Repositorio.ClientesRepositorio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

@Service
public class ClientesUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientesRepositorio clientesRepositorio;
@Override
public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
    Clientes cliente = clientesRepositorio.findByCorreo(correo)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    List<GrantedAuthority> authorities = new ArrayList<>();

    // Si el correo termina en @admin.com, es ADMIN
    if (correo.endsWith("@admin.com")) {
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    } else {
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    return new User(cliente.getCorreo(), cliente.getContraseña(), authorities);
}
    
}