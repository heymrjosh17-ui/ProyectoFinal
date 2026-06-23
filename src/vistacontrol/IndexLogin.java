package vistacontrol;

import utils.Lectura;

/**
 *
 * @author heyjhonnita
 */
public class IndexLogin {

    public static Lectura leer = new Lectura();

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
                Index.inicio();
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
}
