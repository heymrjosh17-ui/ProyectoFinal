package vistacontrol;

import utils.Lectura;
import utils.Utilitarios;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD de Personas usando ArrayList - OOP con Java
 * Concepto: ArrayList es una lista dinámica que crece/reduce automáticamente
 * 
 * @author heymrjosh17
 */
public class IndexpersonaArrayList {

    private static final Lectura leer = new Lectura();
    private static final List<Persona> personas = new ArrayList<>();

    /**
     * Validar si existe un código en la lista
     * @param codigo - código a validar
     * @return índice si existe, -1 si no existe
     */
    public static int validarCodigo(int codigo) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Validar si existe un DNI en la lista
     * @param dni - DNI a validar
     * @return índice si existe, -1 si no existe
     */
    public static int validarDni(int dni) {
        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getDni() == dni) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtener nombre completo de una persona por índice
     */
    public static String obtenerNombre(int pos) {
        if (pos >= 0 && pos < personas.size()) {
            return personas.get(pos).getNombreCompleto();
        }
        return null;
    }

    /**
     * Mensaje de error
     */
    public static void error() {
        System.out.println("❌ Error. Ingrese valor válido.");
    }

    /**
     * Menú Principal de Gestión de Personas
     */
    public static void menuPersona() {
        int opcion;
        do {
            System.out.println("""
                               ╔════════════════════════════════════╗
                               ║   GESTIÓN DE PERSONAS (ArrayList)  ║
                               ║  1. Agregar persona                ║
                               ║  2. Ver listado                    ║
                               ║  3. Eliminar persona               ║
                               ║  4. Editar persona                 ║
                               ║  5. Buscar persona                 ║
                               ║  6. Regresar                       ║
                               ╚════════════════════════════════════╝
                               """);
            System.out.print("Seleccione opción [1-6]: ");
            opcion = leer.entero();
            
            switch (opcion) {
                case 1 -> agregar();
                case 2 -> listar();
                case 3 -> eliminar();
                case 4 -> editar();
                case 5 -> buscar();
                case 6 -> System.out.println("📤 Regresando al menú principal...");
                default -> error();
            }
        } while (opcion != 6);
    }

    /**
     * CREAR - Agregar nueva persona al ArrayList
     * ArrayList crece automáticamente con .add()
     */
    public static void agregar() {
        Utilitarios.limpiarPantalla();
        System.out.println("\n========== AGREGAR NUEVA PERSONA ==========");

        // Validar código único
        int codigo;
        do {
            System.out.print("Ingrese código: ");
            codigo = leer.entero();
            if (validarCodigo(codigo) != -1) {
                System.out.println("⚠️  Error: El código ya existe.");
            }
        } while (validarCodigo(codigo) != -1);

        System.out.print("Ingrese nombre: ");
        String nombre = leer.cadena();

        System.out.print("Ingrese apellido paterno: ");
        String paterno = leer.cadena();

        System.out.print("Ingrese apellido materno: ");
        String materno = leer.cadena();

        // Validar DNI único
        int dni;
        do {
            System.out.print("Ingrese DNI: ");
            dni = leer.entero();
            if (validarDni(dni) != -1) {
                System.out.println("⚠️  Error: El DNI ya está registrado.");
            }
        } while (validarDni(dni) != -1);

        System.out.print("Ingrese teléfono: ");
        int fono = leer.entero();

        System.out.print("Ingrese dirección: ");
        String direccion = leer.cadena();

        System.out.print("Ingrese email: ");
        String email = leer.cadena();

        // CREAR objeto Persona y AGREGAR al ArrayList
        Persona p = new Persona(codigo, nombre, paterno, materno, dni, fono, direccion, email);
        personas.add(p);  // ArrayList crece automáticamente

        System.out.println("✅ ¡Persona agregada exitosamente!");
        System.out.println("📊 Total de personas registradas: " + personas.size());
    }

    /**
     * READ - Listar todas las personas
     * Recorre el ArrayList usando .size() y .get()
     */
    public static void listar() {
        Utilitarios.limpiarPantalla();
        System.out.println("\n========== LISTADO DE PERSONAS ==========");
        
        if (personas.isEmpty()) {  // Verificar si la lista está vacía
            System.out.println("❌ No hay datos registrados.");
            return;
        }

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.printf("║ %-3s %-10s %-25s %-20s │%n", "Nro", "Código", "Nombre Completo", "DNI");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        // Recorrer ArrayList con for tradicional
        for (int i = 0; i < personas.size(); i++) {
            Persona p = personas.get(i);  // .get() obtiene elemento por índice
            System.out.printf("│ %-3d %-10d %-25s %-20d │%n", 
                i + 1, p.getCodigo(), p.getNombreCompleto(), p.getDni());
        }
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println("\n📊 Total de registros: " + personas.size());
    }

    /**
     * UPDATE - Editar persona existente
     * Utiliza .set() para actualizar elementos del ArrayList
     */
    public static void editar() {
        Utilitarios.limpiarPantalla();
        if (personas.isEmpty()) {
            System.out.println("❌ No hay personas para editar.");
            return;
        }

        listar();
        System.out.print("\n¿Qué persona desea editar? (Ingrese número): ");
        int posicion = leer.entero() - 1;

        if (posicion < 0 || posicion >= personas.size()) {
            error();
            return;
        }

        Persona p = personas.get(posicion);
        System.out.println("\n========== EDITAR PERSONA ==========");
        System.out.println("Datos actuales: " + p);

        System.out.print("Nuevo nombre [" + p.getNombre() + "]: ");
        String nombre = leer.cadena();
        if (!nombre.isEmpty()) p.setNombre(nombre);

        System.out.print("Nuevo apellido paterno [" + p.getPaterno() + "]: ");
        String paterno = leer.cadena();
        if (!paterno.isEmpty()) p.setPaterno(paterno);

        System.out.print("Nuevo apellido materno [" + p.getMaterno() + "]: ");
        String materno = leer.cadena();
        if (!materno.isEmpty()) p.setMaterno(materno);

        System.out.print("Nuevo teléfono [" + p.getFono() + "]: ");
        String fonoStr = leer.cadena();
        if (!fonoStr.isEmpty()) p.setFono(Integer.parseInt(fonoStr));

        System.out.print("Nueva dirección [" + p.getDireccion() + "]: ");
        String direccion = leer.cadena();
        if (!direccion.isEmpty()) p.setDireccion(direccion);

        System.out.print("Nuevo email [" + p.getEmail() + "]: ");
        String email = leer.cadena();
        if (!email.isEmpty()) p.setEmail(email);

        // .set() actualiza el elemento en el ArrayList
        personas.set(posicion, p);

        System.out.println("✅ Registro editado correctamente.");
    }

    /**
     * DELETE - Eliminar persona del ArrayList
     * Utiliza .remove() para eliminar elemento por índice
     */
    public static void eliminar() {
        Utilitarios.limpiarPantalla();
        if (personas.isEmpty()) {
            System.out.println("❌ No hay personas para eliminar.");
            return;
        }

        listar();
        System.out.print("\n¿Qué persona desea eliminar? (Ingrese número): ");
        int posicion = leer.entero() - 1;

        if (posicion < 0 || posicion >= personas.size()) {
            error();
            return;
        }

        Persona p = personas.get(posicion);
        System.out.println("¿Está seguro de eliminar a: " + p.getNombreCompleto() + "? (s/n): ");
        char confirmacion = leer.letra();

        if (confirmacion == 's' || confirmacion == 'S') {
            personas.remove(posicion);  // .remove() elimina elemento por índice
            System.out.println("✅ Registro eliminado.");
            System.out.println("📊 Total de personas ahora: " + personas.size());
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }

    /**
     * BUSCAR - Búsqueda por criterios con comodines
     * Ejemplos: "Car*", "*men", "*al*"
     */
    public static void buscar() {
        Utilitarios.limpiarPantalla();
        if (personas.isEmpty()) {
            System.out.println("❌ No hay personas registradas.");
            return;
        }

        System.out.println("\n========== BUSCAR PERSONA ==========");
        System.out.println("Búsqueda por comodines: Car*, *men, *al*");
        System.out.print("Ingrese criterio de búsqueda: ");
        String criterio = leer.cadena().toUpperCase();

        List<Persona> resultados = new ArrayList<>();

        // Buscar en ArrayList con coincidencia de patrones
        for (Persona p : personas) {
            if (coincidePatron(p.getNombreCompleto().toUpperCase(), criterio)) {
                resultados.add(p);
            }
        }

        if (resultados.isEmpty()) {
            System.out.println("❌ No se encontraron resultados.");
        } else {
            System.out.println("\n✅ Se encontraron " + resultados.size() + " resultado(s):");
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            for (int i = 0; i < resultados.size(); i++) {
                Persona p = resultados.get(i);
                System.out.println((i + 1) + ". " + p);
            }
            System.out.println("╚════════════════════════════════════════════════════════════════╝");
        }
    }

    /**
     * Verificar si un criterio con comodines coincide
     * Ejemplos: "Car*" busca todo lo que empiece con "Car"
     */
    private static boolean coincidePatron(String texto, String patron) {
        if (patron.contains("*")) {
            if (patron.startsWith("*") && patron.endsWith("*")) {
                // *al* -> contiene "al"
                return texto.contains(patron.replace("*", ""));
            } else if (patron.startsWith("*")) {
                // *men -> termina con "men"
                return texto.endsWith(patron.replace("*", ""));
            } else if (patron.endsWith("*")) {
                // Car* -> comienza con "Car"
                return texto.startsWith(patron.replace("*", ""));
            }
        }
        return texto.equals(patron);
    }

    /**
     * Obtener el ArrayList de personas
     */
    public static List<Persona> getPersonas() {
        return new ArrayList<>(personas);
    }

    /**
     * Obtener cantidad de personas
     */
    public static int getCantidad() {
        return personas.size();
    }

    /**
     * Verificar si la lista está vacía
     */
    public static boolean estaVacia() {
        return personas.isEmpty();
    }

    /**
     * Limpiar todas las personas
     */
    public static void limpiar() {
        personas.clear();
    }
}
