package vistacontrol;

import java.util.List;

/*
 * STUB: versión archivada del Aportes original.
 * Implementa firmas compatibles y delega a AportesArrayList para que el código existente siga funcionando.
 * El original se mueve a src/vistacontrol/archived/Aportes.java
 */

public class Aportes {

    public static void registrar() {
        AportesArrayList.registrar();
    }

    public static void listar() {
        AportesArrayList.listar();
    }

    public static boolean tieneAportes(int codigo) {
        List<Aporte> lista = AportesArrayList.getAportes();
        for (Aporte a : lista) {
            if (a.getCodigoPersona() == codigo) return true;
        }
        return false;
    }

    public static void guardarEnArchivo() {
        AportesArrayList.guardarEnArchivo();
    }

    public static void cargarDesdeArchivo() {
        AportesArrayList.cargarDesdeArchivo();
    }

    public static int getCont() {
        return AportesArrayList.getCantidad();
    }

}
