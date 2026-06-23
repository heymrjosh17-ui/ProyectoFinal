package vistacontrol;

import utils.Lectura;
import utils.Utilitarios;
import java.io.*;

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
        guardarEnArchivo();

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

    // Persistence: save/load to CSV
    public static void guardarEnArchivo() {
        File f = new File("aportes.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (int i = 0; i < cont; i++) {
                String fecha = fechas[i] == null ? "" : fechas[i];
                String dia = dias[i] == null ? "" : dias[i];
                String iglesia = iglesias[i] == null ? "" : iglesias[i];
                pw.printf("%d;%s;%s;%s;%.2f;%.2f%n", codigosPersona[i], escapeCSV(fecha), escapeCSV(dia), escapeCSV(iglesia), diezmos[i], ofrendas[i]);
            }
        } catch (IOException e) {
            System.out.println("Error guardando aportes: " + e.getMessage());
        }
    }

    public static void cargarDesdeArchivo() {
        File f = new File("aportes.csv");
        if (!f.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            cont = 0;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                if (parts.length >= 6) {
                    try {
                        codigosPersona[cont] = Integer.parseInt(parts[0]);
                    } catch (NumberFormatException ex) {
                        codigosPersona[cont] = 0;
                    }
                    fechas[cont] = unescapeCSV(parts[1]);
                    dias[cont] = unescapeCSV(parts[2]);
                    iglesias[cont] = unescapeCSV(parts[3]);
                    try {
                        diezmos[cont] = parts[4].isEmpty() ? 0.0 : Double.parseDouble(parts[4]);
                    } catch (NumberFormatException ex) {
                        diezmos[cont] = 0.0;
                    }
                    try {
                        ofrendas[cont] = parts[5].isEmpty() ? 0.0 : Double.parseDouble(parts[5]);
                    } catch (NumberFormatException ex) {
                        ofrendas[cont] = 0.0;
                    }
                    cont++;
                    if (cont >= codigosPersona.length) break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error cargando aportes: " + e.getMessage());
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
