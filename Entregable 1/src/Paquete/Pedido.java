package Paquete;

public class Pedido {
    public static final String BORRADOR = "BORRADOR";
    public static final String CONFIRMADO = "CONFIRMADO";
    public static final String CANCELADO = "CANCELADO";

    private int id;
    private Cliente cliente;
    private String estado;
    private DetallePedido[] detalles;
    private int contadorDetalles;

    public Pedido(int id, Cliente cliente, int maxDetalles){
        this.id = id;
        this.cliente = cliente;
        this.estado = BORRADOR;
        this.detalles = new DetallePedido[maxDetalles];
        this.contadorDetalles = 0;
    }

    public int getId(){
        return id;
    }

    public String getEstado() {
        return estado;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (!estado.equals(BORRADOR)) {
            System.out.println("Solo se puede agregar productos en estado BORRADOR....");
        }
        detalles[contadorDetalles++] = new DetallePedido(producto, cantidad);
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < contadorDetalles; i++) {
            subtotal += detalles[i].getSubtotal();
        }
        return subtotal;
    }

    public double calcularDescuento(){
        return cliente.calcularDescuento(calcularSubtotal());
    }

    public double calcularTotal(){
        return calcularSubtotal() - calcularDescuento();
    }

    public DetallePedido[] getDetalles() {
        return detalles;
    }

    public int getCantidadDetalles(){
        return contadorDetalles;
    }

    public void confirmar(){
        if (contadorDetalles == 0){
            System.out.println("No se puede confirmar un pedido sin productos...");
        }
        estado = CONFIRMADO;
    }

    public void cancelar(){
        estado = CANCELADO;
    }
}
