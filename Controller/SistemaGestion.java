package Controller;

import java.text.SimpleDateFormat;
import java.util.*;
import Model.*;


public class SistemaGestion {

    private List<Producto> productos;
    private List<Cliente> clientes;
    private List<Pedido> pedidos;

    private int contProductos;
    private int contClientes;
    private int contPedidos;

    public SistemaGestion() {
        productos = new ArrayList<>();
        clientes = new ArrayList<>();
        pedidos = new ArrayList<>();
    }

    

    public void registrarProducto(Producto producto) throws ProductoNoEncontradoException {
        if (buscarProductoPorId(producto.getId()) != null) {
            throw new IllegalArgumentException("ID de producto duplicado");
        }
        productos.add(producto);
    }

    public Producto buscarProductoPorId(int id) throws ProductoNoEncontradoException{
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new ProductoNoEncontradoException("Producto con ID" + id + " no existe.");
    }

    public void listarProductos() {
        for (Producto p : productos) {
            System.out.println("ID: " + p.getId()
                    + " | Nombre: " + p.getNombre()
                    + " | Precio: " + p.getPrecio()
                    + " | Stock: " + p.getStock());
        }
    }

    

    public void registrarCliente(Cliente cliente) {
        if (buscarClientePorId(cliente.getId()) != null) {
            throw new IllegalArgumentException("ID de cliente duplicado");
        }
        clientes.add(cliente);
    }

    public Cliente buscarClientePorId(int id) {
        for (Cliente c : clientes) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    

    public void crearPedido(int idPedido, int idCliente)throws PedidoInvalidoException {
        if (buscarPedidoPorId(idPedido) != null) {
            throw new PedidoInvalidoException("ID de pedido duplicado");
        }

        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            throw new PedidoInvalidoException("Cliente no existe");
        }
        pedidos.add(new Pedido(idPedido, cliente));
    }

    public Pedido buscarPedidoPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void agregarProductoAPedido(int idPedido, int idProducto, int cantidad)throws ProductoNoEncontradoException, StockInsuficienteException, PedidoInvalidoException {

        Pedido pedido = buscarPedidoPorId(idPedido);
        Producto producto = buscarProductoPorId(idProducto);

        if (pedido == null) {
            throw new PedidoInvalidoException("Pedido no existe");
        }

        if (producto == null) {
            throw new ProductoNoEncontradoException("Producto no existe");
        }

        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente");
        }

        pedido.agregarProducto(producto, cantidad);
    }


    public void confirmarPedido(int idPedido)throws PedidoInvalidoException {

        Pedido pedido = buscarPedidoPorId(idPedido);

        if (pedido == null) {
            throw new PedidoInvalidoException("Pedido no existe");
        }

        if (!pedido.getEstado().equals(Pedido.BORRADOR)) {
            throw new PedidoInvalidoException("Solo se puede confirmar pedidos en BORRADOR");
        }

        if ( pedido.getDetalles().isEmpty()){
            throw new PedidoInvalidoException("No se puede confirmar un pedido vacio");
        }
        for (DetallePedido d : pedido.getDetalles()) {
            d.getProducto().disminuirStock(d.getCantidad());
        }

        pedido.confirmar();
    }


    public void cancelarPedido(int idPedido) {

        Pedido pedido = buscarPedidoPorId(idPedido);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no existe");
        }

        if (pedido.getEstado().equals(Pedido.CONFIRMADO)) {

            for (DetallePedido d : pedido.getDetalles()) {
                d.getProducto().aumentarStock(d.getCantidad());
            }
        }
        pedido.cancelar();
    }

    public void listarPedidos() {
    if (pedidos.isEmpty()) {
        System.out.println("No hay pedidos registrados.");
        return;
    }

    for (Pedido p : pedidos) {
        System.out.println("------------------------");
        System.out.println(p);
    }
}


    public void listarPedidosPorFecha(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaBuscada = sdf.format(fecha);

        boolean encontrado = false;

        for (Pedido p : pedidos) {
            String fechaPedido = sdf.format(p.getFechaCreacion());

            if ( fechaPedido.equals(fechaBuscada)) {
                System.out.println(p);
                encontrado = true;
            }
        }
        if (!encontrado){
            System.out.println("No se encontraron pedidos en esa fecha.");
        }
    }

    public void verDetallePedido(int idPedido) {

        Pedido pedido = buscarPedidoPorId(idPedido);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no existe");
        }

        System.out.println("Estado: " + pedido.getEstado());
        System.out.println("Fecha: " + pedido.getFechaCreacion());
        System.out.println("Subtotal: " + pedido.calcularSubtotal());
        System.out.println("Descuento: " + pedido.calcularDescuento());
        System.out.println("Total: " + pedido.calcularTotal());

        for (DetallePedido d : pedido.getDetalles()) {
            System.out.println("Producto: " + d.getProducto().getNombre()
                    + " | Cantidad: " + d.getCantidad());
        }
    }

    public void eliminarProducto(int id) throws ProductoNoEncontradoException {
        Iterator<Producto> iterator = productos.iterator();

        while (iterator.hasNext()) {
            Producto p = iterator.next();

            if (p.getId() == id) {
                iterator.remove();
                return;
            }
        }
        throw new ProductoNoEncontradoException("Producto no encontrado");
    }
}
