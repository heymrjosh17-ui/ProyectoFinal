package vistacontrol;

/*
 * STUB: delega a IndexpersonaArrayList para que el resto del proyecto siga funcionando.
 * La versión original queda en archived/Indexpersona.java
 */

public class Indexpersona {

    public static int validarCodigo(int codigo) {
        return IndexpersonaArrayList.validarCodigo(codigo);
    }

    public static int validardni(int dni) {
        return IndexpersonaArrayList.validardni(dni);
    }

    public static String obtenerNombre(int pos) {
        return IndexpersonaArrayList.obtenerNombre(pos);
    }

    public static void menuPersona() {
        IndexpersonaArrayList.menuPersona();
    }

    public static void agregar() {
        IndexpersonaArrayList.agregar();
    }

    public static void listar() {
        IndexpersonaArrayList.listar();
    }

    public static int[] getCodigos() { return IndexpersonaArrayList.getCodigos(); }
    public static String[] getNombres() { return IndexpersonaArrayList.getNombres(); }
    public static int getCont() { return IndexpersonaArrayList.getCont(); }
}
