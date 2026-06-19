package vistacontrol;

import utils.Lectura;
import utils.Utilitarios;

/**
 *
 * @author heyjhonna
 */
public class Indexpersona {

    private static final Lectura leer = new Lectura();
    private static final int [] MAX = new int [100];

    private static int[] codigos = new int[100];
    private static String[] nombres = new String[100];
    private static String[] paternos = new String[100];
    private static String[] maternos = new String[100];
    private static int[] dnis = new int[100];
    private static int[] fonos = new int[100];
    private static String[] dires = new String[100];
    private static String[] emails = new String[100];

    private static int cont = 0;
    
    //VALIDAR CODIGO
    public static int validarCodigo(int codigo) {
        for (int i = 0; i < cont; i++) {
            if (codigos[i] == codigo) {
                return i; 
            }
        }
        return -1; 
    }
    public static String obtenerNombre(int pos){
        return nombres[pos] + " " + paternos[pos] + " " + maternos[pos];
        
    }
    public static void cositas2() {
        Utilitarios.limpiarPantalla();
        Utilitarios.imprimirSeparador();
    }

    public static void error() {
        System.out.println("Error. Ingrese valor valido.");
    }

    public static void menuPersona() {
        int opcion;
        do {
            System.out.println("""
                               GESTION DE PERSONAS
                               1. Agregar personas
                               2. Ver listado
                               3. Eliminar persona
                               4. Regresar
                               """);
            System.out.println("Seleccione una opcion [1-4]: ");
            opcion = leer.entero();
            switch (opcion) {
                case 1 ->
                    agregar();
                case 2 ->
                    listar();
                case 3 ->
                    cont = EliminarPersona.ejecutar(codigos, nombres, paternos, maternos, dnis, fonos, dires, emails, cont);
                case 4 ->
                    System.out.println("Regresando al menu principal");
                default ->
                    error();
            }
        } while (opcion != 4);
    }

    public static void agregar() {
        System.out.println("\n--- INGRESE LOS DATOS ---");
        System.out.print("Ingrese código: ");
        codigos[cont] = leer.entero();

        System.out.print("Ingrese nombre: ");
        nombres[cont] = leer.cadena();

        System.out.print("Ingrese apellido paterno: ");
        paternos[cont] = leer.cadena();

        System.out.print("Ingrese apellido materno: ");
        maternos[cont] = leer.cadena();

        System.out.print("Ingrese DNI: ");
        dnis[cont] = leer.entero();

        System.out.print("Ingrese teléfono: ");
        fonos[cont] = leer.entero();

        System.out.print("Ingrese direccion: ");
        dires[cont] = leer.cadena();

        System.out.print("Ingrese email: ");
        emails[cont] = leer.cadena();
        cont++;
        System.out.println("¡Persona agregada exitosamente!");
    }

    public static void listar() {
        System.out.println("--- LISTADO DE PERSONAS ---");
        if (cont == 0) {
            System.out.println("No hay datos registrados.");
        } else {
            for (int i = 0; i < cont; i++) {
                System.out.println("------------------------------------");
                System.out.println("Persona #" + (i + 1));
                System.out.println("Código: " + codigos[i] + " | DNI: " + dnis[i]);
                System.out.println("Nombre: " + nombres[i] + " " + paternos[i] + " " + maternos[i]);
                System.out.println("Fono: " + fonos[i] + " | Email: " + emails[i]);
                System.out.println("Dirección: " + dires[i]);
            }
        }
        System.out.println("Volviendo al menu principal");
    }
}
