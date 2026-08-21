package vistacontrol;

import utils.Lectura;
import utils.Utilitarios;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

/**
 * CRUD de Aportes usando ArrayList - OOP con Java
 * Gestiona diezmos, ofrendas y otros aportes relacionados con personas
 * 
 * @author heymrjosh17
 */
public class AportesArrayList {

    private static final Lectura leer = new Lectura();
    private static final List<Aporte> aportes = new ArrayList<>();
    private static final String ARCHIVO = "aportes_arraylist.csv";

    /**
     * CREAR - Registrar nuevo aporte
     * ArrayList crece automáticamente con .add()
     */
    public static void registrar() {
        Utilitarios.limpiarPantalla();
        System.out.println("\n========== REGISTRAR NUEVO APORTE ==========");

        System.out.print("Ingrese código de persona: ");
        int cod = leer.entero();

        // Validar que la persona exista
        if (IndexpersonaArrayList.validarCodigo(cod) == -1) {
            System.out.println("❌ Error: Persona no encontrada.");
            return;
        }

        System.out.println("\n--- DATOS DEL APORTE ---");

        System.out.print("Diezmo: S/. ");
        double diezmo = leer.decimal();

        System.out.print("Ofrenda: S/. ");
        double ofrenda = leer.decimal();

        System.out.print("Fecha (dd/mm/yyyy): ");
        String fecha = leer.cadena();

        System.out.print("Día (Lunes, Martes, etc.): ");
        String dia = leer.cadena();

        System.out.print("Iglesia: ");
        String iglesia = leer.cadena();

        // CREAR objeto Aporte y AGREGAR al ArrayList
        Aporte aporte = new Aporte(cod, diezmo, ofrenda, fecha, dia, iglesia);
        aportes.add(aporte);  // ArrayList crece automáticamente

        guardarEnArchivo();
        System.out.println("✅ Aporte registrado exitosamente!");
        System.out.println("📊 Total de aportes: " + aportes.size());
    }

    /**
     * READ - Listar todos los aportes
     * Recorre el ArrayList usando .size() y .get()
     */
    public static void listar() {
        Utilitarios.limpiarPantalla();
        System.out.println("\n========== LISTADO DE APORTES ==========");

        if (aportes.isEmpty()) {
            System.out.println("❌ No hay aportes registrados.");
            return;
        }

        System.out.println("╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.printf("║ %-3s %-8s %-15s %-15s %-15s %-12s %-15s %-15s │%n", 
            "Nro", "Código", "Diezmo", "Ofrenda", "Total", "Fecha", "Día", "Iglesia");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");

        double totalDiezmos = 0;
        double totalOfrendas = 0;
        double totalGeneral = 0;

        // Recorrer ArrayList con for tradicional
        for (int i = 0; i < aportes.size(); i++) {
            Aporte aporte = aportes.get(i);
            totalDiezmos += aporte.getDiezmo();
            totalOfrendas += aporte.getOfrenda();
            totalGeneral += aporte.getTotal();

            System.out.printf("│ %-3d %-8d S/. %-12.2f S/. %-12.2f S/. %-12.2f %-12s %-15s %-15s │%n", 
                i + 1, 
                aporte.getCodigoPersona(),
                aporte.getDiezmo(),
                aporte.getOfrenda(),
                aporte.getTotal(),
                aporte.getFecha(),
                aporte.getDia(),
                aporte.getIglesia());
        }

        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ TOTALES:                                    S/. %-12.2f S/. %-12.2f S/. %-12.2f                                                  │%n", 
            totalDiezmos, totalOfrendas, totalGeneral);
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println("\n📊 Total de aportes: " + aportes.size());
    }

    /**
     * UPDATE - Editar aporte existente
     * Utiliza .set() para actualizar elementos del ArrayList
     */
    public static void editar() {
        Utilitarios.limpiarPantalla();
        if (aportes.isEmpty()) {
            System.out.println("❌ No hay aportes para editar.");
            return;
        }

        listar();
        System.out.print("\n¿Qué aporte desea editar? (Ingrese número): ");
        int posicion = leer.entero() - 1;

        if (posicion < 0 || posicion >= aportes.size()) {
            System.out.println("❌ Posición inválida.");
            return;
        }

        Aporte aporte = aportes.get(posicion);
        System.out.println("\n========== EDITAR APORTE ==========");
        System.out.println("Datos actuales: " + aporte);

        System.out.print("Nuevo diezmo [S/. " + aporte.getDiezmo() + "]: ");
        String diezmoStr = leer.cadena();
        if (!diezmoStr.isEmpty()) aporte.setDiezmo(Double.parseDouble(diezmoStr));

        System.out.print("Nueva ofrenda [S/. " + aporte.getOfrenda() + "]: ");
        String ofrendaStr = leer.cadena();
        if (!ofrendaStr.isEmpty()) aporte.setOfrenda(Double.parseDouble(ofrendaStr));

        System.out.print("Nueva fecha [" + aporte.getFecha() + "]: ");
        String fecha = leer.cadena();
        if (!fecha.isEmpty()) aporte.setFecha(fecha);

        System.out.print("Nuevo día [" + aporte.getDia() + "]: ");
        String dia = leer.cadena();
        if (!dia.isEmpty()) aporte.setDia(dia);

        System.out.print("Nueva iglesia [" + aporte.getIglesia() + "]: ");
        String iglesia = leer.cadena();
        if (!iglesia.isEmpty()) aporte.setIglesia(iglesia);

        // .set() actualiza el elemento en el ArrayList
        aportes.set(posicion, aporte);

        guardarEnArchivo();
        System.out.println("✅ Aporte actualizado correctamente.");
    }

    /**
     * DELETE - Eliminar aporte del ArrayList
     * Utiliza .remove() para eliminar elemento por índice
     */
    public static void eliminar() {
        Utilitarios.limpiarPantalla();
        if (aportes.isEmpty()) {
            System.out.println("❌ No hay aportes para eliminar.");
            return;
        }

        listar();
        System.out.print("\n¿Qué aporte desea eliminar? (Ingrese número): ");
        int posicion = leer.entero() - 1;

        if (posicion < 0 || posicion >= aportes.size()) {
            System.out.println("❌ Posición inválida.");
            return;
        }

        Aporte aporte = aportes.get(posicion);
        System.out.println("¿Está seguro de eliminar este aporte? (s/n): ");
        System.out.println(aporte);
        char confirmacion = leer.letra();

        if (confirmacion == 's' || confirmacion == 'S') {
            aportes.remove(posicion);  // .remove() elimina elemento por índice
            guardarEnArchivo();
            System.out.println("✅ Aporte eliminado.");
            System.out.println("📊 Total de aportes ahora: " + aportes.size());
        } else {
            System.out.println("❌ Operación cancelada.");
        }
    }

    /**
     * BUSCAR - Búsqueda de aportes por código de persona
     */
    public static void buscar() {
        Utilitarios.limpiarPantalla();
        if (aportes.isEmpty()) {
            System.out.println("❌ No hay aportes registrados.");
            return;
        }

        System.out.println("\n========== BUSCAR APORTES ==========");
        System.out.print("Ingrese código de persona a buscar: ");
        int codigo = leer.entero();

        List<Aporte> resultados = new ArrayList<>();

        // Buscar en ArrayList
        for (Aporte aporte : aportes) {
            if (aporte.getCodigoPersona() == codigo) {
                resultados.add(aporte);
            }
        }

        if (resultados.isEmpty()) {
            System.out.println("❌ No se encontraron aportes para ese código.");
        } else {
            System.out.println("\n✅ Se encontraron " + resultados.size() + " aporte(s):");
            System.out.println("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");

            double totalDiezmos = 0;
            double totalOfrendas = 0;

            for (int i = 0; i < resultados.size(); i++) {
                Aporte aporte = resultados.get(i);
                totalDiezmos += aporte.getDiezmo();
                totalOfrendas += aporte.getOfrenda();
                System.out.println((i + 1) + ". " + aporte);
            }

            System.out.println("╠════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("TOTALES → Diezmos: S/. %.2f | Ofrendas: S/. %.2f | General: S/. %.2f%n", 
                totalDiezmos, totalOfrendas, totalDiezmos + totalOfrendas);
            System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        }
    }

    /**
     * MENÚ Principal de Gestión de Aportes
     */
    public static void menuAporte() {
        cargarDesdeArchivo();  // Cargar aportes al iniciar
        int opcion;
        do {
            Utilitarios.limpiarPantalla();
            System.out.println("""
                               ╔════════════════════════════════════╗
                               ║  GESTIÓN DE APORTES (ArrayList)    ║
                               ║  1. Registrar aporte               ║
                               ║  2. Listar aportes                 ║
                               ║  3. Editar aporte                  ║
                               ║  4. Eliminar aporte                ║
                               ║  5. Buscar por persona             ║
                               ║  6. Regresar                       ║
                               ╚════════════════════════════════════╝
                               """);
            System.out.print("Seleccione opción [1-6]: ");
            opcion = leer.entero();

            switch (opcion) {
                case 1 -> registrar();
                case 2 -> listar();
                case 3 -> editar();
                case 4 -> eliminar();
                case 5 -> buscar();
                case 6 -> {
                    guardarEnArchivo();
                    System.out.println("📤 Regresando al menú principal...");
                }
                default -> System.out.println("❌ Opción inválida.");
            }
        } while (opcion != 6);
    }

    /**
     * Guardar aportes en archivo CSV
     */
    public static void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            // Encabezado
            pw.println("codigo_persona;diezmo;ofrenda;fecha;dia;iglesia");
            
            // Datos
            for (Aporte aporte : aportes) {
                pw.printf("%d;%.2f;%.2f;%s;%s;%s%n",
                    aporte.getCodigoPersona(),
                    aporte.getDiezmo(),
                    aporte.getOfrenda(),
                    aporte.getFecha(),
                    aporte.getDia(),
                    aporte.getIglesia());
            }
            System.out.println("✅ Aportes guardados en: " + ARCHIVO);
        } catch (IOException e) {
            System.out.println("❌ Error guardando aportes: " + e.getMessage());
        }
    }

    /**
     * Cargar aportes desde archivo CSV
     */
    public static void cargarDesdeArchivo() {
        File f = new File(ARCHIVO);
        if (!f.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            br.readLine();  // Saltar encabezado
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    try {
                        int codigo = Integer.parseInt(parts[0]);
                        double diezmo = Double.parseDouble(parts[1]);
                        double ofrenda = Double.parseDouble(parts[2]);
                        String fecha = parts[3];
                        String dia = parts[4];
                        String iglesia = parts[5];
                        
                        Aporte aporte = new Aporte(codigo, diezmo, ofrenda, fecha, dia, iglesia);
                        aportes.add(aporte);
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️  Error en línea: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️  No se pudo cargar archivo de aportes: " + e.getMessage());
        }
    }

    /**
     * Obtener cantidad de aportes
     */
    public static int getCantidad() {
        return aportes.size();
    }

    /**
     * Verificar si la lista está vacía
     */
    public static boolean estaVacia() {
        return aportes.isEmpty();
    }

    /**
     * Obtener el ArrayList de aportes
     */
    public static List<Aporte> getAportes() {
        return new ArrayList<>(aportes);
    }

    /**
     * Limpiar todos los aportes
     */
    public static void limpiar() {
        aportes.clear();
    }
}
