package vistacontrol;

import utils.Lectura;
import utils.Utilitarios;

/**
 *
 * @author heyjhonna
 */
public class Index {

    private static final Lectura leer = new Lectura();

    public static boolean loginmostrar() {

        String usuariocorrecto = "usiel";
        String clavecorrecta = "123";

        System.out.println("/// Usuario /// ");
        String usuario = leer.cadena();

        System.out.println("/// Contraseña /// ");
        String clave = leer.cadena();

        if (usuario.equals(usuariocorrecto) && clave.equals(clavecorrecta)) {
            System.out.println("Bienvenido al sistema");
            return true;
        } else {
            System.out.println("Usuario o contraseña incorrectos");
        }
        return false;
    }

    public static void login() {
        int intentos = 0;
        int limitedeint = 3;
        while (intentos < limitedeint) {
            if (loginmostrar()) {
                inicio();
                return;
            } else {
                intentos++;
                int restantes = limitedeint - intentos;
                if (intentos == 1) {
                    System.out.println("Te queda " + restantes + " intentos");
                } else {
                    System.out.println("Te quedan " + restantes + " intento");
                }
                if (intentos == 3) {
                    System.out.println("Usuario bloqueado");
                }
            }
        }
    }

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
        login();
    }
}
