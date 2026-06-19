package vistacontrol;

import utils.Lectura;
import utils.Utilitarios;

/**
 *
 * @author heyjhonna
 */
public class Index {

    private static final Lectura leer = new Lectura();

    public static void cositas() {
        Utilitarios.limpiarPantalla();
        Utilitarios.imprimirSeparador();
    }

    public static void salir() {
        System.out.println("Gracias por su visita: ");
    }

    public static void error() {
        System.out.println("Error. Ingrese valor valido...");
    }

    public static void menu() {
        cositas();
        System.out.println("""
                           --- SISTEMA DE TESORERÍA ---     
                               1. GESTION DE PERSONAS
                               2. GESTION DE APORTES
                               3. SALIR
                               """);
        System.out.print("Seleccione una opción [1-3]:  ");
    }

    public static void inicio() {
        int opcion;
        do {
            menu();
            opcion = leer.entero();

            switch (opcion) {
                case 1 ->
                    Indexpersona.menuPersona();
                case 2 ->
                    Aportes.menuAporte();
                case 3 ->
                    salir();
                default ->
                    error();

            }
        } while (opcion != 3);
    }

    public static void main(String[] args) {
        inicio();
    }
}
