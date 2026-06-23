package vistacontrol;

import utils.Lectura;
import utils.Utilitarios;
import java.io.*;

/**
 *
 * @author heyjhonna
 */
public class Indexpersona {

    private static final Lectura leer = new Lectura();
    private static final int[] MAX = new int[100];

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

    public static int validardni(int dni) {
        for (int i = 0; i < cont; i++) {
            if (dnis[i] == dni) {
                return i;
            }
        }
        return -1;
    }

    public static String obtenerNombre(int pos) {
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
                    cont = Eliminarpersona.ejecutar(codigos, nombres, paternos, maternos, dnis, fonos, dires, emails, cont); guardarEnArchivo();
                case 4 ->
                    System.out.println("Regresando al menu principal");
                default ->
                    error();
            }
        } while (opcion != 4);
    }

    public static void agregar() {
        System.out.println("\n--- INGRESE LOS DATOS ---");
        int codigo;
        do {
            System.out.print("Ingrese código: ");
            codigo = leer.entero();

            if (validarCodigo(codigo) != -1) {
                System.out.println("Error: El código ya existe.");
            }

        } while (validarCodigo(codigo) != -1);

        codigos[cont] = codigo;

        System.out.print("Ingrese nombre: ");
        nombres[cont] = leer.cadena();

        System.out.print("Ingrese apellido paterno: ");
        paternos[cont] = leer.cadena();

        System.out.print("Ingrese apellido materno: ");
        maternos[cont] = leer.cadena();

        int dni;
        do {
            System.out.print("Ingrese DNI: ");
            dni = leer.entero();

            if (validardni(dni) != -1) {
                System.out.println("Error: El DNI ya está registrado.");
            }

        } while (validardni(dni) != -1);

        dnis[cont] = dni;

        System.out.print("Ingrese teléfono: ");
        fonos[cont] = leer.entero();

        System.out.print("Ingrese direccion: ");
        dires[cont] = leer.cadena();

        System.out.print("Ingrese email: ");
        emails[cont] = leer.cadena();

        cont++;
        guardarEnArchivo();
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

    // Persistence: save/load to CSV
    public static void guardarEnArchivo() {
        File f = new File("personas.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (int i = 0; i < cont; i++) {
                String nombre = nombres[i] == null ? "" : nombres[i];
                String paterno = paternos[i] == null ? "" : paternos[i];
                String materno = maternos[i] == null ? "" : maternos[i];
                String dir = dires[i] == null ? "" : dires[i];
                String email = emails[i] == null ? "" : emails[i];
                pw.printf("%d;%s;%s;%s;%d;%d;%s;%s%n", codigos[i], escapeCSV(nombre), escapeCSV(paterno), escapeCSV(materno), dnis[i], fonos[i], escapeCSV(dir), escapeCSV(email));
            }
        } catch (IOException e) {
            System.out.println("Error guardando personas: " + e.getMessage());
        }
    }

    public static void cargarDesdeArchivo() {
        File f = new File("personas.csv");
        if (!f.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            cont = 0;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                if (parts.length >= 8) {
                    try {
                        codigos[cont] = Integer.parseInt(parts[0]);
                    } catch (NumberFormatException ex) {
                        codigos[cont] = 0;
                    }
                    nombres[cont] = unescapeCSV(parts[1]);
                    paternos[cont] = unescapeCSV(parts[2]);
                    maternos[cont] = unescapeCSV(parts[3]);
                    try {
                        dnis[cont] = parts[4].isEmpty() ? 0 : Integer.parseInt(parts[4]);
                    } catch (NumberFormatException ex) {
                        dnis[cont] = 0;
                    }
                    try {
                        fonos[cont] = parts[5].isEmpty() ? 0 : Integer.parseInt(parts[5]);
                    } catch (NumberFormatException ex) {
                        fonos[cont] = 0;
                    }
                    dires[cont] = unescapeCSV(parts[6]);
                    emails[cont] = unescapeCSV(parts[7]);
                    cont++;
                    if (cont >= codigos.length) break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando personas: " + e.getMessage());
        }
    }

    private static String escapeCSV(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace(";", "\\;");
    }

    private static String unescapeCSV(String s) {
        if (s == null) return "";
        return s.replace("\\;", ";").replace("\\\\", "\\");
    }
}
