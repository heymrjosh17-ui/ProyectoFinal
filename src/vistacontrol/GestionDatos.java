package vistacontrol;

import java.io.*;

/**
 * Version simplificada de GestionDatos usando archivos .txt planos.
 * - formatos simples sin escape: campos separados por '|'.
 * - archivos: data/personas.txt, data/aportes.txt, data/personas_aportes.txt
 * - usa arrays fijos (no ArrayList) y se integra con Indexpersona y Aportes vía getters/setDatos.
 */
public class GestionDatos {

    private static final String DATA_DIR = "data";
    private static final String PERSONAS = "personas.txt";
    private static final String APORTES = "aportes.txt";
    private static final String OUT = "personas_aportes.txt";

    // Cargar al iniciar
    public static void initOnStart() {
        try {
            File dir = ensureDataDir();
            File fP = findFile(dir, PERSONAS);
            File fA = findFile(dir, APORTES);
            if (fP != null) cargarPersonas(fP);
            if (fA != null) cargarAportes(fA);
        } catch (Exception e) {
            System.out.println("GestionDatos.initOnStart error: " + e.getMessage());
        }
    }

    // Guardar al salir
    public static void saveOnExit() {
        try {
            File dir = ensureDataDir();
            guardarPersonas(new File(dir, PERSONAS));
            guardarAportes(new File(dir, APORTES));
            exportCombined(new File(dir, OUT));
        } catch (Exception e) {
            System.out.println("GestionDatos.saveOnExit error: " + e.getMessage());
        }
    }

    private static File ensureDataDir() throws IOException {
        String wd = System.getProperty("user.dir");
        File dataDir = new File(wd, DATA_DIR);
        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) throw new IOException("No se pudo crear data/ en: " + dataDir.getAbsolutePath());
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

    // Guardar personas: formato simple campos separados por '|'
    public static void guardarPersonas(File f) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            int count = Indexpersona.getCont();
            int[] cod = Indexpersona.getCodigos();
            String[] nombres = Indexpersona.getNombres();
            String[] paternos = Indexpersona.getPaternos();
            String[] maternos = Indexpersona.getMaternos();
            int[] dnis = Indexpersona.getDnis();
            int[] fonos = Indexpersona.getFonos();
            String[] dires = Indexpersona.getDires();
            String[] emails = Indexpersona.getEmails();
            for (int i = 0; i < count; i++) {
                pw.println(cod[i] + "|" + safe(nombres, i) + "|" + safe(paternos, i) + "|" + safe(maternos, i) + "|" +
                        dnis[i] + "|" + fonos[i] + "|" + safe(dires, i) + "|" + safe(emails, i));
            }
        } catch (IOException e) {
            System.out.println("Error guardando personas.txt: " + e.getMessage());
        }
    }

    // Cargar personas desde txt simple
    public static void cargarPersonas(File f) {
        final int MAX = 100;
        int[] cod = new int[MAX];
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
                String[] p = line.split("\|", -1);
                // esperamos al menos 8 campos
                if (p.length < 8) continue;
                try { cod[cont] = Integer.parseInt(p[0]); } catch (NumberFormatException ex) { cod[cont] = 0; }
                nombres[cont] = p[1];
                paternos[cont] = p[2];
                maternos[cont] = p[3];
                try { dnis[cont] = p[4].isEmpty() ? 0 : Integer.parseInt(p[4]); } catch (NumberFormatException ex) { dnis[cont] = 0; }
                try { fonos[cont] = p[5].isEmpty() ? 0 : Integer.parseInt(p[5]); } catch (NumberFormatException ex) { fonos[cont] = 0; }
                dires[cont] = p[6];
                emails[cont] = p[7];
                cont++;
            }
            Indexpersona.setDatos(cod, nombres, paternos, maternos, dnis, fonos, dires, emails, cont);
        } catch (IOException e) {
            System.out.println("Error cargando personas.txt: " + e.getMessage());
        }
    }

    // Guardar aportes
    public static void guardarAportes(File f) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            int count = Aportes.getCont();
            int[] cod = Aportes.getCodigosPersona();
            double[] diezmos = Aportes.getDiezmos();
            double[] ofrendas = Aportes.getOfrendas();
            String[] fechas = Aportes.getFechas();
            String[] dias = Aportes.getDias();
            String[] iglesias = Aportes.getIglesias();
            for (int i = 0; i < count; i++) {
                pw.println(cod[i] + "|" + safe(fechas, i) + "|" + safe(dias, i) + "|" + safe(iglesias, i) + "|" +
                        diezmos[i] + "|" + ofrendas[i]);
            }
        } catch (IOException e) {
            System.out.println("Error guardando aportes.txt: " + e.getMessage());
        }
    }

    // Cargar aportes
    public static void cargarAportes(File f) {
        final int MAX = 100;
        int[] cod = new int[MAX];
        double[] diezmos = new double[MAX];
        double[] ofr = new double[MAX];
        String[] fechas = new String[MAX];
        String[] dias = new String[MAX];
        String[] iglesias = new String[MAX];
        int cont = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null && cont < MAX) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\|", -1);
                if (p.length < 6) continue;
                try { cod[cont] = Integer.parseInt(p[0]); } catch (NumberFormatException ex) { cod[cont] = 0; }
                fechas[cont] = p[1];
                dias[cont] = p[2];
                iglesias[cont] = p[3];
                try { diezmos[cont] = p[4].isEmpty() ? 0.0 : Double.parseDouble(p[4]); } catch (NumberFormatException ex) { diezmos[cont] = 0.0; }
                try { ofr[cont] = p[5].isEmpty() ? 0.0 : Double.parseDouble(p[5]); } catch (NumberFormatException ex) { ofr[cont] = 0.0; }
                cont++;
            }
            Aportes.setDatos(cod, diezmos, ofr, fechas, dias, iglesias, cont);
        } catch (IOException e) {
            System.out.println("Error cargando aportes.txt: " + e.getMessage());
        }
    }

    // Export combinado simple
    public static void exportCombined(File out) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(out))) {
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
                        pw.println(pcod[i] + "|" + joinName(pnom[i], ppaterno[i], pmaterno[i]) + "|" + pdni[i] + "|" + pfono[i] + "|" +
                                safe(pdire, i) + "|" + safe(pemail, i) + "|" + safe(afecha, j) + "|" + safe(adia, j) + "|" + safe(aiglesia, j) + "|" +
                                adiez[j] + "|" + aofr[j]);
                        found = true;
                    }
                }
                if (!found) {
                    pw.println(pcod[i] + "|" + joinName(pnom[i], ppaterno[i], pmaterno[i]) + "|" + pdni[i] + "|" + pfono[i] + "|" +
                            safe(pdire, i) + "|" + safe(pemail, i) + "||||0.0|0.0");
                }
            }
        } catch (IOException e) {
            System.out.println("Error exportando personas_aportes.txt: " + e.getMessage());
        }
    }

    // Helpers
    private static String safe(String[] arr, int idx) { return arr[idx] == null ? "" : arr[idx]; }
    private static String joinName(String a, String b, String c) {
        StringBuilder sb = new StringBuilder();
        if (a != null && !a.isEmpty()) sb.append(a);
        if (b != null && !b.isEmpty()) { if (sb.length()>0) sb.append(' '); sb.append(b); }
        if (c != null && !c.isEmpty()) { if (sb.length()>0) sb.append(' '); sb.append(c); }
        return sb.toString();
    }
}
