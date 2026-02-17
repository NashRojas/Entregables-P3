package Model;
import java.util.*;
public class Pedido {
    public static final String BORRADOR = "BORRADOR";
    public static final String CONFIRMADO = "CONFIRMADO";
    public static final String CANCELADO = "CANCELADO";

    private int id;
    private Cliente cliente;
    private String estado;
    private Date fechaCreacion;
    private List<DetallePedido> detalles;


    public Pedido(int id, Cliente cliente){
        this.id = id;
        this.cliente = cliente;
        this.estado = BORRADOR;
        this.fechaCreacion = new Date();
        this.detalles = new ArrayList<>();
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
            return;
        }
        detalles.add(new DetallePedido(producto, cantidad));
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetallePedido detalle : detalles){
            subtotal += detalle.getSubtotal();
        }
        return subtotal;
    }
        
    public double calcularDescuento(){
        return cliente.calcularDescuento(calcularSubtotal());
    }

    public double calcularTotal(){
        return calcularSubtotal() - calcularDescuento();
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public int getCantidadDetalles(){
        return detalles.size();
    }

    public void confirmar(){
        if (detalles.isEmpty()){
            System.out.println("No se puede confirmar un pedido sin productos...");
            return;
        }
        estado = CONFIRMADO;
    }

    public void cancelar(){
        estado = CANCELADO;
    }
}
