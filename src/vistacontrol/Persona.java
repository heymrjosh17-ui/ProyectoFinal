package vistacontrol;

/**
 * Clase Persona con los atributos requeridos
 * @author heymrjosh17
 */
public class Persona {
    private int codigo;
    private String nombre;
    private String paterno;
    private String materno;
    private int dni;
    private int fono;
    private String direccion;
    private String email;

    // Constructor
    public Persona(int codigo, String nombre, String paterno, String materno, 
                   int dni, int fono, String direccion, String email) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.paterno = paterno;
        this.materno = materno;
        this.dni = dni;
        this.fono = fono;
        this.direccion = direccion;
        this.email = email;
    }

    // Getters
    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getPaterno() { return paterno; }
    public String getMaterno() { return materno; }
    public int getDni() { return dni; }
    public int getFono() { return fono; }
    public String getDireccion() { return direccion; }
    public String getEmail() { return email; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPaterno(String paterno) { this.paterno = paterno; }
    public void setMaterno(String materno) { this.materno = materno; }
    public void setDni(int dni) { this.dni = dni; }
    public void setFono(int fono) { this.fono = fono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setEmail(String email) { this.email = email; }

    // Método para obtener nombre completo
    public String getNombreCompleto() {
        return nombre + " " + paterno + " " + materno;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + 
               " | DNI: " + dni + 
               " | Nombre: " + getNombreCompleto() + 
               " | Fono: " + fono + 
               " | Email: " + email + 
               " | Dirección: " + direccion;
    }
}
