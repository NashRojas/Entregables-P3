package Model;

import Controller.SistemaGestion;


public class GeneradorReportes extends Thread {

    private SistemaGestion sistema;
    private boolean activo = true;

    public GeneradorReportes(SistemaGestion sistema) {
        this.sistema = sistema;
        setDaemon(true); 
    }
    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        while (activo) {

            try {
                Thread.sleep(10000); 
                System.out.println("\n===== REPORTE AUTOMÁTICO =====");
                sistema.generarEstadisticas();
                System.out.println("------------------------------------------");

            } catch (InterruptedException e) {
                System.out.println("Generador de reportes interrumpido.");
            }
        }
    }
}
