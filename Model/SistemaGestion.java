package Model;

public class SistemaGestion {

    private Producto[] productos;
    private Cliente[] clientes;
    private Pedido[] pedidos;

    private int contProductos;
    private int contClientes;
    private int contPedidos;

    public SistemaGestion(int maxProductos, int maxClientes, int maxPedidos) {
        productos = new Producto[maxProductos];
        clientes = new Cliente[maxClientes];
        pedidos = new Pedido[maxPedidos];
    }

    

    public void registrarProducto(int id, String nombre, double precio, int stock) {
        if (buscarProductoPorId(id) != null) {
            throw new IllegalArgumentException("ID de producto duplicado");
        }
        productos[contProductos++] = new Producto(id, nombre, precio, stock);
    }

    public Producto buscarProductoPorId(int id) {
        for (int i = 0; i < contProductos; i++) {
            if (productos[i].getId() == id) {
                return productos[i];
            }
        }
        return null;
    }

    public void listarProductos() {
        for (int i = 0; i < contProductos; i++) {
            System.out.println("ID: " + productos[i].getId()
                    + " | Nombre: " + productos[i].getNombre()
                    + " | Precio: " + productos[i].getPrecio()
                    + " | Stock: " + productos[i].getStock());
        }
    }

    

    public void registrarCliente(Cliente cliente) {
        if (buscarClientePorId(cliente.getId()) != null) {
            throw new IllegalArgumentException("ID de cliente duplicado");
        }
        clientes[contClientes++] = cliente;
    }

    public Cliente buscarClientePorId(int id) {
        for (int i = 0; i < contClientes; i++) {
            if (clientes[i].getId() == id) {
                return clientes[i];
            }
        }
        return null;
    }

    

    public void crearPedido(int idPedido, int idCliente) {
        if (buscarPedidoPorId(idPedido) != null) {
            throw new IllegalArgumentException("ID de pedido duplicado");
        }

        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no existe");
        }

        pedidos[contPedidos++] = new Pedido(idPedido, cliente, 10);
    }

    public Pedido buscarPedidoPorId(int id) {
        for (int i = 0; i < contPedidos; i++) {
            if (pedidos[i].getId() == id) {
                return pedidos[i];
            }
        }
        return null;
    }

    public void agregarProductoAPedido(int idPedido, int idProducto, int cantidad) {

        Pedido pedido = buscarPedidoPorId(idPedido);
        Producto producto = buscarProductoPorId(idProducto);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no existe");
        }

        if (producto == null) {
            throw new IllegalArgumentException("Producto no existe");
        }

        if (producto.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente");
        }

        pedido.agregarProducto(producto, cantidad);
    }

    public void confirmarPedido(int idPedido) {

        Pedido pedido = buscarPedidoPorId(idPedido);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no existe");
        }

        if (!pedido.getEstado().equals(Pedido.BORRADOR)) {
            throw new IllegalStateException("Solo se puede confirmar pedidos en BORRADOR");
        }

        for (int i = 0; i < pedido.getCantidadDetalles(); i++) {
            DetallePedido d = pedido.getDetalles()[i];
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

            // restaurar stock
            for (int i = 0; i < pedido.getCantidadDetalles(); i++) {
                DetallePedido d = pedido.getDetalles()[i];
                d.getProducto().aumentarStock(d.getCantidad());
            }
        }

        pedido.cancelar();
    }

    public void listarPedidos() {
        for (int i = 0; i < contPedidos; i++) {
            System.out.println("ID Pedido: " + pedidos[i].getId()
                    + " | Estado: " + pedidos[i].getEstado()
                    + " | Total: " + pedidos[i].calcularTotal());
        }
    }

    public void verDetallePedido(int idPedido) {

        Pedido pedido = buscarPedidoPorId(idPedido);

        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no existe");
        }

        System.out.println("Estado: " + pedido.getEstado());
        System.out.println("Subtotal: " + pedido.calcularSubtotal());
        System.out.println("Descuento: " + pedido.calcularDescuento());
        System.out.println("Total: " + pedido.calcularTotal());

        for (int i = 0; i < pedido.getCantidadDetalles(); i++) {
            DetallePedido d = pedido.getDetalles()[i];
            System.out.println("Producto: " + d.getProducto().getNombre()
                    + " | Cantidad: " + d.getCantidad());
        }
    }
}
