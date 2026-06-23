package vistacontrol;

import utils.Lectura;

public class Eliminarpersona {

    private static final Lectura leer = new Lectura();

    public static int ejecutar(
            int[] codigos,
            String[] nombres,
            String[] paternos,
            String[] maternos,
            int[] dnis,
            int[] fonos,
            String[] dires,
            String[] emails,
            int cont) {

        if (cont == 0) {
            System.out.println("No hay personas para eliminar.");
            return cont;
        }

        System.out.print("Ingrese el código a eliminar: ");
        int cod = leer.entero();
        if (Aportes.tieneAportes(cod)) {
            System.out.println("No se puede eliminar a la persona porque tiene aportes registrados.");
            return cont;
        }
        int indice = -1;
        // BUSCAR PERSONA
        for (int i = 0; i < cont; i++) {
            if (codigos[i] == cod) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            System.out.println("Error: Código no encontrado.");
            return cont;
        } else {
            for (int i = indice; i < cont - 1; i++) {
                codigos[i] = codigos[i + 1];
                nombres[i] = nombres[i + 1];
                paternos[i] = paternos[i + 1];
                maternos[i] = maternos[i + 1];
                dnis[i] = dnis[i + 1];
                fonos[i] = fonos[i + 1];
                dires[i] = dires[i + 1];
                emails[i] = emails[i + 1];
            }
            System.out.println("¡Persona eliminada exitosamente!");
            return cont - 1;
        }
    }
}
