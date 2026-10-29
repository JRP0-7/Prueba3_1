import java.util.ArrayList;
import java.util.List;

public class GestorEmpleados {

    private final ArrayList<Empleado> empleados = new ArrayList<>();

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public Empleado buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        return buscar(codigo.trim(), 0);
    }

    private Empleado buscar(String codigo, int indice) {
        if (indice >= empleados.size()) {
            return null;
        }
        Empleado e = empleados.get(indice);
        if (e.getCodigo().equalsIgnoreCase(codigo)) {
            return e;
        }
        return buscar(codigo, indice + 1);
    }

    public void registrar(Empleado e) {
        if (buscar(e.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un empleado con el codigo " + e.getCodigo() + ".");
        }
        empleados.add(e);
    }

    public Empleado clonar(Empleado original) {
        return original.clone();
    }

    public void registrarClon(Empleado clon, Empleado datos) {
        if (datos.getClass() != clon.getClass()) {
            throw new IllegalArgumentException("El tipo no puede cambiar en un clon.");
        }
        if (buscar(datos.getCodigo()) != null) {
            throw new IllegalArgumentException("El codigo " + datos.getCodigo() + " ya existe; use uno nuevo.");
        }
        clon.setCodigo(datos.getCodigo());
        clon.setNombre(datos.getNombre());
        clon.setSalarioBase(datos.getSalarioBase());
        if (clon instanceof EmpleadoAdministrativo) {
            EmpleadoAdministrativo c = (EmpleadoAdministrativo) clon;
            EmpleadoAdministrativo d = (EmpleadoAdministrativo) datos;
            c.setBonificacion(d.getBonificacion());
            c.setDescuento(d.getDescuento());
        } else {
            EmpleadoPorHoras c = (EmpleadoPorHoras) clon;
            EmpleadoPorHoras d = (EmpleadoPorHoras) datos;
            c.setHorasTrabajadas(d.getHorasTrabajadas());
            c.setTarifaPorHora(d.getTarifaPorHora());
        }
        empleados.add(clon);
    }

    public Empleado salarioMaximo() {
        Empleado max = null;
        for (Empleado e : empleados) {
            if (max == null || e.calcularSalario() > max.calcularSalario()) {
                max = e;
            }
        }
        return max;
    }

    public double promedio() {
        if (empleados.isEmpty()) {
            return 0;
        }
        double suma = 0;
        for (Empleado em : empleados) {
            suma += em.calcularSalario();
        }
        return suma / empleados.size();
    }

    public String comparar(Empleado a, Empleado b) {
        return "Misma referencia: " + (a == b) + "\nMismos datos: " + a.existe(b)
                + "\n\nA:\n" + a.mostrarInformacion() + "\n\nB:\n" + b.mostrarInformacion();
    }
}
