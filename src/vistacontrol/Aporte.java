package vistacontrol;

/**
 * Clase Aporte con los datos de diezmos, ofrendas y relacionados
 * @author heymrjosh17
 */
public class Aporte {
    private int codigoPersona;
    private double diezmo;
    private double ofrenda;
    private String fecha;
    private String dia;
    private String iglesia;

    // Constructor
    public Aporte(int codigoPersona, double diezmo, double ofrenda, 
                  String fecha, String dia, String iglesia) {
        this.codigoPersona = codigoPersona;
        this.diezmo = diezmo;
        this.ofrenda = ofrenda;
        this.fecha = fecha;
        this.dia = dia;
        this.iglesia = iglesia;
    }

    // Getters
    public int getCodigoPersona() { return codigoPersona; }
    public double getDiezmo() { return diezmo; }
    public double getOfrenda() { return ofrenda; }
    public String getFecha() { return fecha; }
    public String getDia() { return dia; }
    public String getIglesia() { return iglesia; }

    // Setters
    public void setDiezmo(double diezmo) { this.diezmo = diezmo; }
    public void setOfrenda(double ofrenda) { this.ofrenda = ofrenda; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setDia(String dia) { this.dia = dia; }
    public void setIglesia(String iglesia) { this.iglesia = iglesia; }

    // Obtener total
    public double getTotal() {
        return diezmo + ofrenda;
    }

    @Override
    public String toString() {
        return "Código: " + codigoPersona + 
               " | Fecha: " + fecha + 
               " | Diezmo: " + String.format("%.2f", diezmo) + 
               " | Ofrenda: " + String.format("%.2f", ofrenda) + 
               " | Total: " + String.format("%.2f", getTotal()) + 
               " | Día: " + dia + 
               " | Iglesia: " + iglesia;
    }
}
