package Paquete;

public class Producto {
    
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {
        if (precio <= 0){
            System.out.println("El precio debe ser mayor que 0");
        }
        if (stock < 0){
            System.out.println("El stock no puede ser negativo");
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
}
