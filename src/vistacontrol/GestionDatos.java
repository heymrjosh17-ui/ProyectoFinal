package vistacontrol;

import java.io.*;
import java.util.Locale;

/**
 * GestionDatos centraliza toda la persistencia CSV:
 * - data/personas.csv  (separador ';', formato usado por Indexpersona)
 * - data/aportes.csv   (separador ';', formato usado por Aportes)
 * - data/personas_aportes.csv (salida combinada, separado por comas)
 *
 * Usa arrays fijos (no ArrayList) y se conecta con Indexpersona / Aportes mediante accessors y setDatos.
 */
public class GestionDatos {

    private static final String DATA_DIR = "data";
    private static final String PERSONAS = "personas.csv";
    private static final String APORTES = "aportes.csv";
    private static final String OUT = "personas_aportes.csv";

    // Llamar al iniciar la app
    public static void initOnStart() {
        try {
            File dir = ensureDataDir();
            File fP = findFile(dir, PERSONAS);
            File fA = findFile(dir, APORTES);
            if (fP != null) {
                System.out.println("Cargando personas desde: " + fP.getAbsolutePath());
                cargarPersonas(fP);
            } else {
                System.out.println("No existe " + PERSONAS + " en data/ ni en working dir.");
            }
            if (fA != null) {
                System.out.println("Cargando aportes desde: " + fA.getAbsolutePath());
                cargarAportes(fA);
            } else {
                System.out.println("No existe " + APORTES + " en data/ ni en working dir.");
            }
        } catch (Exception e) {
            System.out.println("Error en initOnStart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Llamar al salir (guardar y generar reporte combinado)
    public static void saveOnExit() {
        try {
            File dir = ensureDataDir();
            File fp = new File(dir, PERSONAS);
            File fa = new File(dir, APORTES);
            System.out.println("Guardando personas en: " + fp.getAbsolutePath());
            guardarPersonas(fp);
            System.out.println("Guardando aportes en: " + fa.getAbsolutePath());
            guardarAportes(fa);
            File out = new File(dir, OUT);
            System.out.println("Generando reporte combinado en: " + out.getAbsolutePath());
            exportCombined(out);
        } catch (Exception e) {
            System.out.println("Error en saveOnExit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static File ensureDataDir() throws IOException {
        String wd = System.getProperty("user.dir");
        File dataDir = new File(wd, DATA_DIR);
        if (!dataDir.exists()) {
            boolean ok = dataDir.mkdirs();
            if (!ok) throw new IOException("No se pudo crear data/: " + dataDir.getAbsolutePath());
        }
        return dataDir;
    }

    private static File findFile(File dataDir, String name) {
        File f = new File(dataDir, name);
        if (f.exists()) return f;
        File f2 = new File(System.getProperty("user.dir"), name);
        if (f2.exists()) return f2;
        return null;
    }

    // --- Personas: guardar / cargar (usa arrays y setDatos/getters de Indexpersona) ---

    public static void guardarPersonas(File f) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            int cont = Indexpersona.getCont();
            int[] cod = Indexpersona.getCodigos();
            String[] nombres = Indexpersona.getNombres();
            String[] paternos = Indexpersona.getPaternos();
            String[] maternos = Indexpersona.getMaternos();
            int[] dnis = Indexpersona.getDnis();
            int[] fonos = Indexpersona.getFonos();
            String[] dires = Indexpersona.getDires();
            String[] emails = Indexpersona.getEmails();
            for (int i = 0; i < cont; i++) {
                String nombre = safe(nombres, i);
                String paterno = safe(paternos, i);
                String materno = safe(maternos, i);
                String dir = safe(dires, i);
                String mail = safe(emails, i);
                pw.printf("%d;%s;%s;%s;%d;%d;%s;%s%n",
                        cod[i],
                        escapeCSV(nombre),
                        escapeCSV(paterno),
                        escapeCSV(materno),
                        dnis[i],
                        fonos[i],
                        escapeCSV(dir),
                        escapeCSV(mail)
                );
            }
        } catch (IOException e) {
            System.out.println("Error guardando personas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void cargarPersonas(File f) {
        final int MAX = 100;
        int[] codigos = new int[MAX];
        String[] nombres = new String[MAX];
        String[] paternos = new String[MAX];
        String[] maternos = new String[MAX];
        int[] dnis = new int[MAX];
        int[] fonos = new int[MAX];
        String[] dires = new String[MAX];
        String[] emails = new String[MAX];
        int cont = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null && cont < MAX) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                if (parts.length < 8) continue;
                try { codigos[cont] = Integer.parseInt(parts[0]); } catch (NumberFormatException ex) { codigos[cont] = 0; }
                nombres[cont] = unescapeCSV(parts[1]);
                paternos[cont] = unescapeCSV(parts[2]);
                maternos[cont] = unescapeCSV(parts[3]);
                try { dnis[cont] = parts[4].isEmpty() ? 0 : Integer.parseInt(parts[4]); } catch (NumberFormatException ex) { dnis[cont] = 0; }
                try { fonos[cont] = parts[5].isEmpty() ? 0 : Integer.parseInt(parts[5]); } catch (NumberFormatException ex) { fonos[cont] = 0; }
                dires[cont] = unescapeCSV(parts[6]);
                emails[cont] = unescapeCSV(parts[7]);
                cont++;
            }
            Indexpersona.setDatos(codigos, nombres, paternos, maternos, dnis, fonos, dires, emails, cont);
        } catch (IOException e) {
            System.out.println("Error cargando personas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Aportes: guardar / cargar (usa arrays y setDatos/getters de Aportes) ---

    public static void guardarAportes(File f) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            int cont = Aportes.getCont();
            int[] cod = Aportes.getCodigosPersona();
            double[] diezmos = Aportes.getDiezmos();
            double[] ofrendas = Aportes.getOfrendas();
            String[] fechas = Aportes.getFechas();
            String[] dias = Aportes.getDias();
            String[] iglesias = Aportes.getIglesias();
            for (int i = 0; i < cont; i++) {
                pw.printf("%d;%s;%s;%s;%.2f;%.2f%n",
                        cod[i],
                        escapeCSV(safe(fechas, i)),
                        escapeCSV(safe(dias, i)),
                        escapeCSV(safe(iglesias, i)),
                        diezmos[i],
                        ofrendas[i]
                );
            }
        } catch (IOException e) {
            System.out.println("Error guardando aportes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void cargarAportes(File f) {
        final int MAX = 100;
        int[] codigos = new int[MAX];
        double[] diezmos = new double[MAX];
        double[] ofrendas = new double[MAX];
        String[] fechas = new String[MAX];
        String[] dias = new String[MAX];
        String[] iglesias = new String[MAX];
        int cont = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null && cont < MAX) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                if (parts.length < 6) continue;
                try { codigos[cont] = Integer.parseInt(parts[0]); } catch (NumberFormatException ex) { codigos[cont] = 0; }
                fechas[cont] = unescapeCSV(parts[1]);
                dias[cont] = unescapeCSV(parts[2]);
                iglesias[cont] = unescapeCSV(parts[3]);
                try { diezmos[cont] = parts[4].isEmpty() ? 0.0 : Double.parseDouble(parts[4]); } catch (NumberFormatException ex) { diezmos[cont] = 0.0; }
                try { ofrendas[cont] = parts[5].isEmpty() ? 0.0 : Double.parseDouble(parts[5]); } catch (NumberFormatException ex) { ofrendas[cont] = 0.0; }
                cont++;
            }
            Aportes.setDatos(codigos, diezmos, ofrendas, fechas, dias, iglesias, cont);
        } catch (IOException e) {
            System.out.println("Error cargando aportes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Export combinado (CSV con comas) ---
    public static void exportCombined(File out) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(out))) {
            pw.println("codigo,nombre_completo,dni,fono,direccion,email,fecha,dia,iglesia,diezmo,ofrenda");
            int pCount = Indexpersona.getCont();
            int[] pcod = Indexpersona.getCodigos();
            String[] pnom = Indexpersona.getNombres();
            String[] ppaterno = Indexpersona.getPaternos();
            String[] pmaterno = Indexpersona.getMaternos();
            int[] pdni = Indexpersona.getDnis();
            int[] pfono = Indexpersona.getFonos();
            String[] pdire = Indexpersona.getDires();
            String[] pemail = Indexpersona.getEmails();

            int aCount = Aportes.getCont();
            int[] acod = Aportes.getCodigosPersona();
            double[] adiez = Aportes.getDiezmos();
            double[] aofr = Aportes.getOfrendas();
            String[] afecha = Aportes.getFechas();
            String[] adia = Aportes.getDias();
            String[] aiglesia = Aportes.getIglesias();

            for (int i = 0; i < pCount; i++) {
                boolean found = false;
                for (int j = 0; j < aCount; j++) {
                    if (acod[j] == pcod[i]) {
                        pw.println(toCsvRow(pcod[i], pnom[i], ppaterno[i], pmaterno[i], pdni[i], pfono[i], pdire[i], pemail[i],
                                afecha[j], adia[j], aiglesia[j], adiez[j], aofr[j]));
                        found = true;
                    }
                }
                if (!found) {
                    pw.println(toCsvRow(pcod[i], pnom[i], ppaterno[i], pmaterno[i], pdni[i], pfono[i], pdire[i], pemail[i],
                            "", "", "", 0.0, 0.0));
                }
            }
        } catch (IOException e) {
            System.out.println("Error exportCombined: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Helpers ---

    private static String toCsvRow(int codigo, String nom, String pat, String mat, int dni, int fono, String dir, String email,
                                   String fecha, String dia, String iglesia, double diezmo, double ofrenda) {
        String nombre = joinName(nom, pat, mat);
        String[] cols = new String[] {
                String.valueOf(codigo),
                nombre,
                dni == 0 ? "" : String.valueOf(dni),
                fono == 0 ? "" : String.valueOf(fono),
                dir == null ? "" : dir,
                email == null ? "" : email,
                fecha == null ? "" : fecha,
                dia == null ? "" : dia,
                iglesia == null ? "" : iglesia,
                diezmo == 0.0 ? "" : String.format(Locale.US, "%.2f", diezmo),
                ofrenda == 0.0 ? "" : String.format(Locale.US, "%.2f", ofrenda)
        };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escapeComma(cols[i]));
        }
        return sb.toString();
    }

    private static String joinName(String a, String b, String c) {
        StringBuilder sb = new StringBuilder();
        if (a != null && !a.isEmpty()) sb.append(a);
        if (b != null && !b.isEmpty()) { if (sb.length()>0) sb.append(' '); sb.append(b); }
        if (c != null && !c.isEmpty()) { if (sb.length()>0) sb.append(' '); sb.append(c); }
        return sb.toString();
    }

    private static String escapeCSV(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace(";", "\\;");
    }

    private static String unescapeCSV(String s) {
        if (s == null) return "";
        return s.replace("\\;", ";").replace("\\\\", "\\");
    }

    private static String safe(String[] arr, int idx) { return arr[idx] == null ? "" : arr[idx]; }

    private static String escapeComma(String s) {
        if (s == null) return "";
        boolean need = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String out = s.replace("\"", "\"\"");
        if (need) out = "\"" + out + "\"";
        return out;
    }
}
