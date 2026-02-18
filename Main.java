
import java.util.*;

import Controller.*;
import Model.*;
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        SistemaGestion sistema = new SistemaGestion();

        int opcion;

        do {
            System.out.println("\n======= Sistema De Gestion =======");
            System.out.println("1) Registrar producto");
            System.out.println("2) Registrar cliente");
            System.out.println("3) Crear pedido");
            System.out.println("4) Agregar producto a pedido");
            System.out.println("5) Ver detalle de pedido");
            System.out.println("6) Listar productos");
            System.out.println("7) Listar pedidos");
            System.out.println("8) Cambiar estado de pedido");
            System.out.println("0) Salir");
            System.out.println("==================================");
            System.out.print("Inserte una opcion: ");
            opcion = sc.nextInt();
            sc.nextLine();

            try {

                switch (opcion) {

                    case 1:
                        System.out.print("ID: ");
                        int idProd = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Nombre: ");
                        String nombreProd = sc.nextLine();

                        System.out.print("Precio: ");
                        double precio = sc.nextDouble();

                        System.out.print("Stock: ");
                        int stock = sc.nextInt();

                        sistema.registrarProducto(new Producto(idProd, nombreProd, precio, stock));
                        break;

                    case 2:
                        System.out.print("ID: ");
                        int idCli = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Nombre: ");
                        String nombreCli = sc.nextLine();

                        System.out.print("1-Regular  2-VIP: ");
                        int tipo = sc.nextInt();

                        if (tipo == 1) {
                            sistema.registrarCliente(new ClienteRegular(idCli, nombreCli));
                        } else {
                            System.out.print("Porcentaje descuento(Ej 0.15 = 15%): ");
                            double desc = sc.nextDouble();
                            sistema.registrarCliente(new ClienteVIP(idCli, nombreCli, desc));
                        }
                        break;

                    case 3:
                        System.out.print("ID Pedido: ");
                        int idPed = sc.nextInt();

                        System.out.print("ID Cliente: ");
                        int idCliente = sc.nextInt();

                        sistema.crearPedido(idPed, idCliente);
                        break;

                    case 4:
                        System.out.print("ID Pedido: ");
                        int idPedido = sc.nextInt();

                        System.out.print("ID Producto: ");
                        int idProducto = sc.nextInt();

                        System.out.print("Cantidad: ");
                        int cantidad = sc.nextInt();

                        sistema.agregarProductoAPedido(idPedido, idProducto, cantidad);
                        break;

                    case 5:
                        System.out.print("ID Pedido: ");
                        sistema.verDetallePedido(sc.nextInt());
                        break;

                    case 6:
                        sistema.listarProductos();
                        break;

                    case 7:
                        sistema.listarPedidos();
                        break;

                    case 8:
                        System.out.print("ID Pedido: ");
                        int idCambiar = sc.nextInt();

                        System.out.print("1-Confirmar  2-Cancelar: ");
                        int accion = sc.nextInt();

                        if (accion == 1) {
                            sistema.confirmarPedido(idCambiar);
                        } else {
                            sistema.cancelarPedido(idCambiar);
                        }
                        break;

                }

            } catch (ProductoNoEncontradoException | StockInsuficienteException | PedidoInvalidoException e) {

                System.out.println("Error controlado: " + e.getMessage());

            } catch (IllegalArgumentException e) {

                System.out.println("Error de entrada: " + e.getMessage());

            } catch (Exception e) {

                System.out.println("Error inesperado: " + e.getMessage());

            } finally {
                System.out.println("Operación finalizada.");
            }

        } while (opcion != 0);

        sc.close();
    }
}
