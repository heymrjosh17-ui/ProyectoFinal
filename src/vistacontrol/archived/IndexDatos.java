package vistacontrol;

import java.io.*;
import java.util.Locale;

/**
 * IndexDatos (sin usar ArrayList)
 * Lee data/personas.csv y data/aportes.csv (o working dir) y genera
 * data/personas_aportes.csv (separado por comas) relacionando personas con sus aportes.
 *
 * Usa arrays fijos (igual que el resto del proyecto) para respetar tu petición de no usar ArrayList.
 */
public class IndexDatos {

    private static final String DATA_DIR_NAME = "data";
    private static final String PERSONAS_FILE = "personas.csv";
    private static final String APORTES_FILE = "aportes.csv";
    private static final String OUTPUT_FILE = "personas_aportes.csv"; // comma-separated

    // límites (coinciden con arrays del resto del proyecto)
    private static final int MAX_PERSONAS = 100;
    private static final int MAX_APORTES = 500;

    public static void main(String[] args) {
        try {
            File dataDir = ensureDataDir();

            File personasSrc = findExistingFile(dataDir, PERSONAS_FILE);
            File aportesSrc = findExistingFile(dataDir, APORTES_FILE);

            if (personasSrc == null) {
                System.out.println("No se encontró '" + PERSONAS_FILE + "' en data/ ni en el working dir. Nada que exportar.");
                return;
            }

            System.out.println("Leyendo personas desde: " + personasSrc.getAbsolutePath());
            Person[] personas = new Person[MAX_PERSONAS];
            int personCount = readPersonas(personasSrc, personas);

            Aporte[] aportes = new Aporte[MAX_APORTES];
            int aporteCount = 0;
            if (aportesSrc != null) {
                System.out.println("Leyendo aportes desde: " + aportesSrc.getAbsolutePath());
                aporteCount = readAportes(aportesSrc, aportes);
            } else {
                System.out.println("No se encontró '" + APORTES_FILE + "' — se exportarán personas sin aportes.");
            }

            File out = new File(dataDir, OUTPUT_FILE);
            writeCombinedCSV(out, personas, personCount, aportes, aporteCount);
            System.out.println("Exportado a: " + out.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Error exportando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static File ensureDataDir() throws IOException {
        String wd = System.getProperty("user.dir");
        File dataDir = new File(wd, DATA_DIR_NAME);
        if (!dataDir.exists()) {
            boolean ok = dataDir.mkdirs();
            if (!ok) throw new IOException("No se pudo crear el directorio data/ en: " + dataDir.getAbsolutePath());
        }
        return dataDir;
    }

    private static File findExistingFile(File dataDir, String fileName) {
        File inData = new File(dataDir, fileName);
        if (inData.exists()) return inData;
        File inWd = new File(System.getProperty("user.dir"), fileName);
        if (inWd.exists()) return inWd;
        return null;
    }

    // Lee personas en el array 'personas', devuelve cantidad leída
    private static int readPersonas(File f, Person[] personas) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null && count < personas.length) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                if (parts.length < 8) continue;
                Person p = new Person();
                try { p.codigo = Integer.parseInt(parts[0]); } catch (NumberFormatException ex) { p.codigo = 0; }
                p.nombre = unescapeCSV(parts[1]);
                p.paterno = unescapeCSV(parts[2]);
                p.materno = unescapeCSV(parts[3]);
                try { p.dni = parts[4].isEmpty() ? 0 : Integer.parseInt(parts[4]); } catch (NumberFormatException ex) { p.dni = 0; }
                try { p.fono = parts[5].isEmpty() ? 0 : Integer.parseInt(parts[5]); } catch (NumberFormatException ex) { p.fono = 0; }
                p.direccion = unescapeCSV(parts[6]);
                p.email = unescapeCSV(parts[7]);
                personas[count++] = p;
            }
        } catch (IOException e) {
            System.out.println("Error leyendo personas: " + e.getMessage());
        }
        return count;
    }

    // Lee aportes en el array 'aportes', devuelve cantidad leída
    private static int readAportes(File f, Aporte[] aportes) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null && count < aportes.length) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                if (parts.length < 6) continue;
                Aporte a = new Aporte();
                try { a.codigoPersona = Integer.parseInt(parts[0]); } catch (NumberFormatException ex) { a.codigoPersona = 0; }
                a.fecha = unescapeCSV(parts[1]);
                a.dia = unescapeCSV(parts[2]);
                a.iglesia = unescapeCSV(parts[3]);
                try { a.diezmo = parts[4].isEmpty() ? 0.0 : Double.parseDouble(parts[4]); } catch (NumberFormatException ex) { a.diezmo = 0.0; }
                try { a.ofrenda = parts[5].isEmpty() ? 0.0 : Double.parseDouble(parts[5]); } catch (NumberFormatException ex) { a.ofrenda = 0.0; }
                aportes[count++] = a;
            }
        } catch (IOException e) {
            System.out.println("Error leyendo aportes: " + e.getMessage());
        }
        return count;
    }

    private static void writeCombinedCSV(File out, Person[] personas, int personCount, Aporte[] aportes, int aporteCount) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(out))) {
            pw.println("codigo,nombre_completo,dni,fono,direccion,email,fecha,dia,iglesia,diezmo,ofrenda");
            for (int i = 0; i < personCount; i++) {
                Person p = personas[i];
                boolean hasAporte = false;
                for (int j = 0; j < aporteCount; j++) {
                    Aporte a = aportes[j];
                    if (a != null && a.codigoPersona == p.codigo) {
                        pw.println(rowFor(p, a));
                        hasAporte = true;
                    }
                }
                if (!hasAporte) {
                    pw.println(rowFor(p, null));
                }
            }
        } catch (IOException e) {
            System.out.println("Error escribiendo archivo de salida: " + e.getMessage());
        }
    }

    private static String rowFor(Person p, Aporte a) {
        String nombreCompleto = joinName(p.nombre, p.paterno, p.materno);
        String codigo = String.valueOf(p.codigo);
        String dni = p.dni == 0 ? "" : String.valueOf(p.dni);
        String fono = p.fono == 0 ? "" : String.valueOf(p.fono);
        String dir = p.direccion == null ? "" : p.direccion;
        String email = p.email == null ? "" : p.email;
        String fecha = a == null ? "" : (a.fecha == null ? "" : a.fecha);
        String dia = a == null ? "" : (a.dia == null ? "" : a.dia);
        String iglesia = a == null ? "" : (a.iglesia == null ? "" : a.iglesia);
        String diezmo = a == null ? "" : String.format(Locale.US, "%.2f", a.diezmo);
        String ofrenda = a == null ? "" : String.format(Locale.US, "%.2f", a.ofrenda);

        String[] cols = new String[] { codigo, nombreCompleto, dni, fono, dir, email, fecha, dia, iglesia, diezmo, ofrenda };
        return escapeAndJoin(cols);
    }

    private static String joinName(String a, String b, String c) {
        StringBuilder sb = new StringBuilder();
        if (a != null && !a.isEmpty()) sb.append(a);
        if (b != null && !b.isEmpty()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(b);
        }
        if (c != null && !c.isEmpty()) {
            if (sb.length() > 0) sb.append(' ');
            sb.append(c);
        }
        return sb.toString();
    }

    private static String escapeAndJoin(String[] cols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escapeComma(cols[i]));
        }
        return sb.toString();
    }

    private static String escapeComma(String s) {
        if (s == null) return "";
        boolean need = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String out = s.replace("\"", "\"\"");
        if (need) out = "\"" + out + "\"";
        return out;
    }

    private static String unescapeCSV(String s) {
        if (s == null) return "";
        return s.replace("\\;", ";").replace("\\\\", "\\");
    }

    private static class Person {
        int codigo;
        String nombre;
        String paterno;
        String materno;
        int dni;
        int fono;
        String direccion;
        String email;
    }

    private static class Aporte {
        int codigoPersona;
        String fecha;
        String dia;
        String iglesia;
        double diezmo;
        double ofrenda;
    }
}
