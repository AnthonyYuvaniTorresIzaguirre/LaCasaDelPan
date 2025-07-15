package com.Panaderia.Servicios;

import com.Panaderia.Modelo.Producto;
import com.Panaderia.dao.ProductoRepositorio;

import java.util.List;

public interface ProductoServicio {
    List<Producto> get();
    Producto get(Long id);
    void save(Producto producto);
    void update(Producto producto);
    void delete(Long id);
    List<Producto> getByCategoria(String categoria);
    List<Producto> buscar(String texto);
    List<Producto> buscarPorTextoYCategoria(String texto, String categoria);
    
    
     boolean estaEnPedido(Long idProducto);
}