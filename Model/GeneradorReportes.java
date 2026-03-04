package Model;

import Controller.SistemaGestion;
import Model.Pedido;
import Model.Producto;

import java.util.List;

public class GeneradorReportes extends Thread {

    private SistemaGestion sistema;

    public GeneradorReportes(SistemaGestion sistema) {
        this.sistema = sistema;
        setDaemon(true); // ✅ Hilo demonio
    }

    @Override
    public void run() {

        while (true) {

            try {
                generarReporte();
                Thread.sleep(10000); // cada 10 segundos
            } catch (InterruptedException e) {
                System.out.println("Generador de reportes interrumpido.");
            }
        }
    }

    private void generarReporte() {

        System.out.println("\n===== REPORTE AUTOMÁTICO =====");

        List<Pedido> pedidos = sistema.getPedidos();
        List<Producto> productos = sistema.getProductos();

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
