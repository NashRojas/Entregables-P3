package Model;

public class DetallePedido {
    
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido(Producto producto, int cantidad){
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
    }

    public Producto getProducto(){
        return producto;
    }

    public int getCantidad(){
        return cantidad;
    }

    public double getSubtotal(){
        return cantidad * precioUnitario;
    }

    @Override
    public String toString() {
        return producto.getNombre() + "| Cantidad: " + cantidad + "| Subtotal: " + getSubtotal();
    }
}
