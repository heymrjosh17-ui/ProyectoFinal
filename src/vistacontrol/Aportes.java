package vistacontrol;

import utils.Lectura;
import utils.Utilitarios;

/**
 *
 * @author heyjhonna
 */
public class Aportes {

    private static final Lectura leer = new Lectura();

    // Relación con persona
    private static int[] codigosPersona = new int[100];

    //Datos del aporte
    private static double[] diezmos = new double[100];
    private static double[] ofrendas = new double[100];
    private static String[] fechas = new String[100];
    private static String[] dias = new String[100];
    private static String[] iglesias = new String[100];

    private static int cont = 0;

    //REGISTRAR APORTE
    public static void registrar() {

        if (cont >= 100) {
            System.out.println("No hay espacio.");
            return;
        }

        System.out.println("Ingrese código de persona: ");
        int cod = leer.entero();

        int indice = Indexpersona.validarCodigo(cod);
        if (indice == -1) {
            System.out.println("Persona no encontrada. ");
            return;
        }
        codigosPersona[cont] = cod;

        System.out.println("--- REGISTRAR APORTE ---");

        System.out.print("Diezmo: ");
        diezmos[cont] = leer.decimal();

        System.out.print("Ofrenda: ");
        ofrendas[cont] = leer.decimal();

        System.out.print("Fecha: ");
        fechas[cont] = leer.cadena();

        System.out.print("Día: ");
        dias[cont] = leer.cadena();

        System.out.print("Iglesia: ");
        iglesias[cont] = leer.cadena();

        cont++;

        System.out.println("✅ Aporte registrado");

    }

    public static void listar() {

        if (cont == 0) {
            System.out.println("No hay aportes.");
            return;
        }

        for (int i = 0; i < cont; i++) {
            System.out.println("----------------------");
            System.out.println("Código Persona: " + codigosPersona[i]);

            int pos = Indexpersona.validarCodigo(codigosPersona[i]);
            if (pos != -1) {
                System.out.println("Nombre: " + Indexpersona.obtenerNombre(pos));
            }
            System.out.println("Aporte #" + (i + 1));
            System.out.println("Diezmo: " + diezmos[i]);
            System.out.println("Ofrenda: " + ofrendas[i]);
            System.out.println("Fecha: " + fechas[i]);
            System.out.println("Día: " + dias[i]);
            System.out.println("Iglesia: " + iglesias[i]);

            double total = diezmos[i] + ofrendas[i];
            System.out.println("Total: " + total);
        }
    }

    //METODO CLAVE PARA EVITAR ERROR
    public static boolean tieneAportes(int codigo) {
        for (int i = 0; i < cont; i++) {
            if (codigosPersona[i] == codigo) {
                return true;
            }
        }
        return false;
    }

    //MENÚ
    public static void menuAporte() {
        int opcion;
        do {
                Utilitarios.imprimirSeparador();
            System.out.println("""
                               GESTION DE APORTES
                               1. Registrar aporte
                               2. Listar aportes
                               3. Regresar
                               """);
            System.out.println("Seleccione: ");
            opcion = leer.entero();

            switch (opcion) {
                case 1:
                    registrar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    System.out.println("Regresando...");
                default:
                    System.out.println("Error...");
            }
        } while (opcion != 3);
    }
}
