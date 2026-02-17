package Model;

public abstract class Cliente {
    
    private int id;
    private String nombre;

    public Cliente(int id, String nombre){
        if ( nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        this.id = id;
        this.nombre = nombre;
    }

    public int getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public abstract double calcularDescuento(double subtotal);

    @Override
    public String toString(){
        return "ID: " + id + " | Nombre: " + nombre;
    }
}
