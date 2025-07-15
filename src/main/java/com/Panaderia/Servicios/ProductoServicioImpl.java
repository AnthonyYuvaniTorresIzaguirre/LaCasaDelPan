package com.Panaderia.Servicios;

import com.Panaderia.dao.ProductoDAO;
import com.Panaderia.Modelo.Producto;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.dao.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServicioImpl implements ProductoServicio {

    @Autowired
    private ProductoDAO productoDAO;
    @Autowired
    private ProductoRepositorio productoRepositorio;
    @Autowired
    private PedidoRepositorio pedidoRepositorio;    

    @Override
    public boolean estaEnPedido(Long idProducto) {
    return pedidoRepositorio.existeProductoEnPedidos(idProducto);
        }
    
    @Transactional
    @Override
    public List<Producto> get() {
        return productoDAO.get();
    }

    @Transactional
    @Override
    public Producto get(Long id) {
        return productoDAO.get(id);
    }

    @Transactional
    @Override
    public void save(Producto producto) {
        productoDAO.save(producto);
    }

    @Transactional
    @Override
    public void update(Producto producto) {
        productoDAO.update(producto);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        productoDAO.delete(id);
    }

    @Transactional
    @Override
    public List<Producto> getByCategoria(String categoria) {
        return productoDAO.getByCategoria(categoria);
    }

    @Override
    public List<Producto> buscar(String texto) {
        return productoRepositorio.buscarPorTexto(texto);
    }

    public List<Producto> buscarPorTextoYCategoria(String texto, String categoria) {
        // Primero obtenemos los productos de esa categoría
        List<Producto> filtradosPorCategoria = getByCategoria(categoria);

        // Luego los filtramos por texto (nombre o descripción contiene el texto)
        return filtradosPorCategoria.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(texto.toLowerCase())
                || p.getDescripcion().toLowerCase().contains(texto.toLowerCase()))
                .toList();
    }
}
