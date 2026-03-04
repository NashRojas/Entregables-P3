package Model;

import Controller.SistemaGestion;
import java.util.List;

public class ProcesadorPedidos extends Thread {

    private SistemaGestion sistema;
    private boolean activo = true;

    public ProcesadorPedidos(SistemaGestion sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {

        while (activo) {

            try {
                procesarPedidosConfirmados();
                Thread.sleep(2000); 
            } catch (InterruptedException e) {
                System.out.println("Hilo procesador interrumpido.");
            }
        }
    }

    private void procesarPedidosConfirmados() {

        List<Pedido> pedidos = sistema.getPedidos();

        synchronized (pedidos) {

            for (Pedido p : pedidos) {

                if (p.getEstado().equals(Pedido.CONFIRMADO)) {

                    System.out.println("Procesando pedido ID: " + p.getId());

                    try {
                        Thread.sleep(3000); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (DetallePedido detalle : p.getDetalles()) {
                        detalle.getProducto()
                            .disminuirStock(detalle.getCantidad());
                    }

                    p.marcarComoProcesado();

                    System.out.println("Pedido ID " + p.getId() +
                                    " procesado correctamente.");
                }
            }
        }
    }

    public void detener() {
        activo = false;
    }
}