package Model;

public class Producto {
    
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacio");
        }
        if (precio <= 0){
            throw new IllegalArgumentException("El precio del producto debe ser mayor que 0");
        }
        if (stock < 0){
            throw new IllegalArgumentException("El Stock del producto debe ser mayor que 0");
        }

        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public double getPrecio(){
        return precio;
    }

    public int getStock(){
        return stock;
    }

    public void disminuirStock(int cantidad){
        if (cantidad > stock){
            System.out.println("No hay Stock disponible....");
        }
        stock -= cantidad;
    }

    public void aumentarStock(int cantidad){
        stock += cantidad;
    }

    @Override
    public String toString(){
        return "ID: " + id + "| Nombre: " + nombre + "| Precio: " + precio + "| Stock: " + stock;
    }
}
