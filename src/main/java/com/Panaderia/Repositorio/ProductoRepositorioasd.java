package com.Panaderia.Repositorio;

import com.Panaderia.Modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepositorioasd extends JpaRepository<Producto, Long> {

}
