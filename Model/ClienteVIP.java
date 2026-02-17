package Model;

public class ClienteVIP extends Cliente {
    
    private double porcentajeDescuento;

    public ClienteVIP(int id, String nombre, double porcentajeDescuento){
        super(id,nombre);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double calcularDescuento(double subtotal){
        return subtotal * porcentajeDescuento / 100;
    }
}
