package com.Panaderia.Repositorio;

import com.Panaderia.Modelo.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long>{
   List<Pedido> findByCliente_IdCli(Integer idCli);
@Query("SELECT COUNT(p) > 0 FROM Pedido p JOIN p.items i WHERE i.idProducto.id = :idProducto")
boolean existeProductoEnPedidos(@Param("idProducto") Long idProducto);

}
