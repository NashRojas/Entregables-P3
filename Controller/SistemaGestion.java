package Controller;

import java.io.*;
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

        cargarProductos();
        cargarClientes();
        cargarPedidos();
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
                System.out.println("Producto Eliminado..");
                return;
            }
        }
        throw new ProductoNoEncontradoException("Producto no encontrado");
    }

    public void listarClientes() {

    if (clientes.isEmpty()) {
        System.out.println("No hay clientes registrados.");
        return;
    }

    for (Cliente cliente : clientes) { // Iterable
        System.out.println("----------------------");
        System.out.println(cliente);
    }
    }

    private Producto buscarProductoPorNombre(String nombre) {
    for (Producto producto : productos) { // Iterable
        if (producto.getNombre().equalsIgnoreCase(nombre)) {
            return producto;
        }
    }
    return null;
    }

    public void mostrarProductoPorId(int id) throws ProductoNoEncontradoException {
    Producto producto = buscarProductoPorId(id);
        if (producto == null) {
            System.out.println("Producto no encontrado.");
        } else {
            System.out.println(producto);
        }
    }

    public void mostrarProductoPorNombre(String nombre) {
    Producto producto = buscarProductoPorNombre(nombre);
        if (producto == null) {
            System.out.println("Producto no encontrado.");
        } else {
            System.out.println(producto);
        }
    }

    public List<Pedido> getPedidos() {
    return pedidos;
    }

    public List<Producto> getProductos() {
    return productos;
    }

    public void guardarProductos() {
        try (DataOutputStream dos =
                new DataOutputStream(new FileOutputStream("productos.dat"))) {
            for (Producto p : productos) {
                dos.writeInt(p.getId());
                dos.writeUTF(p.getNombre());
                dos.writeDouble(p.getPrecio());
                dos.writeInt(p.getStock());
            }
            System.out.println("Productos guardados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar productos: " + e.getMessage());
        }
    }


    public void cargarProductos() {
        File archivo = new File("productos.dat");
        if (!archivo.exists()) return;
        try (DataInputStream dis =
                new DataInputStream(new FileInputStream(archivo))) {
            while (true) {
                int id = dis.readInt();
                String nombre = dis.readUTF();
                double precio = dis.readDouble();
                int stock = dis.readInt();
                productos.add(new Producto(id, nombre, precio, stock));
            }
        } catch (EOFException e) {
            System.out.println("Productos cargados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar productos: " + e.getMessage());
        }
    }

    public void guardarClientes() {
        try (DataOutputStream dos =
                new DataOutputStream(new FileOutputStream("clientes.dat"))) {
            for (Cliente c : clientes) {
                dos.writeUTF(c.getClass().getSimpleName()); 
                dos.writeInt(c.getId());
                dos.writeUTF(c.getNombre());

                if (c instanceof ClienteVIP) {
                ClienteVIP vip = (ClienteVIP) c;
                dos.writeDouble(vip.getPorcentajeDescuento());
                }
            }
            System.out.println("Clientes guardados correctamente");
        } catch (IOException e) {
            System.out.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    public void cargarClientes() {
        File archivo = new File("clientes.dat");
        if (!archivo.exists()) return;
        try (DataInputStream dis =
                new DataInputStream(new FileInputStream(archivo))) {
            while (true) {
                String tipo = dis.readUTF();
                int id = dis.readInt();
                String nombre = dis.readUTF();

                Cliente cliente = null;
                switch (tipo) {
                    case "ClienteRegular":
                        cliente = new ClienteRegular(id, nombre);
                        break;
                    case "ClienteVIP":
                        double porcentaje = dis.readDouble();
                        cliente = new ClienteVIP(id, nombre, porcentaje);
                        break;
                }
                if (cliente != null) {
                    clientes.add(cliente);
                }
            }
        } catch (EOFException e) {
            System.out.println("Clientes cargados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar clientes: " + e.getMessage());
        }
    }

    public void cargarPedidos() {
        File archivo = new File("pedidos.txt");
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\|");
                int idPedido = Integer.parseInt(partes[0]);
                int idCliente = Integer.parseInt(partes[1]);
                long fecha = Long.parseLong(partes[2]);
                String estado = partes[3];
                Cliente cliente = buscarClientePorId(idCliente);
                if (cliente != null) {
                    Pedido pedido = new Pedido(idPedido, cliente);
                    pedido.confirmar(); // Ajuste simple académico
                    pedidos.add(pedido);
                }
            }
            System.out.println("Pedidos cargados correctamente.");
        } catch (IOException e) {
            System.out.println("Error al cargar pedidos: " + e.getMessage());
        }
    }

    public void guardarPedidos() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("pedidos.txt"))) {
            for (Pedido p : pedidos) {
                pw.println(
                    p.getId() + "|" +
                    p.getCliente().getId() + "|" +
                    p.getFechaCreacion().getTime() + "|" +
                    p.getEstado() + "|" +
                    p.calcularTotal()
                );
            }

            System.out.println("Pedidos guardados correctamente.");

        } catch (IOException e) {
            System.out.println("Error al guardar pedidos: " + e.getMessage());
        }
    }

    public void generarEstadisticas() {
        System.out.println("Total clientes: " + clientes.size());
        System.out.println("Total productos: " + productos.size());
        System.out.println("Total pedidos: " + pedidos.size());
        int confirmados = 0;
        int procesados = 0;
        int borradores = 0;

        synchronized (pedidos) {
            for (Pedido p : pedidos) {
                switch (p.getEstado()) {
                    case Pedido.CONFIRMADO:
                        confirmados++;
                        break;
                    case Pedido.PROCESADO:
                        procesados++;
                        break;
                    case Pedido.BORRADOR:
                        borradores++;
                        break;
                }
            }
        }

        int stockBajo = 0;

        synchronized (productos) {
            for (Producto prod : productos) {
                if (prod.getStock() < 5) {
                    stockBajo++;
                }
            }
        }
        System.out.println("Pedidos Confirmados: " + confirmados);
        System.out.println("Pedidos Procesados: " + procesados);
        System.out.println("Pedidos Borrador: " + borradores);
        System.out.println("Productos con stock bajo: " + stockBajo);
        System.out.println("==============================\n");
    }
}
