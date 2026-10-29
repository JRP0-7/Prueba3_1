public class EmpleadoAdministrativo extends Empleado {
    private double bonificacion;
    private double descuento;

    public EmpleadoAdministrativo(String codigo, String nombre, double salarioBase, double bonificacion, double descuento) {
        super(codigo, nombre, salarioBase);
        setBonificacion(bonificacion);
        setDescuento(descuento);
    }

    public double getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(double bonificacion) {
        if (bonificacion < 0) {
            throw new IllegalArgumentException("La bonificacion no puede ser negativa.");
        }
        this.bonificacion = bonificacion;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        if (descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
        this.descuento = descuento;
    }

    @Override
    public double calcularSalario() {
        return (getSalarioBase() + bonificacion) * (1 - descuento / 100);
    }

    @Override
    public String getTipo() {
        return "Administrativo";
    }

    @Override
    public boolean existe(Empleado emple) {
        if (!super.existe(emple)) {
            return false;
        }
        EmpleadoAdministrativo e = (EmpleadoAdministrativo) emple;
        return bonificacion == e.bonificacion
                && descuento == e.descuento;
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion()
                + String.format("%nBonificacion: %.2f%nDescuento: %.2f%%", bonificacion, descuento);
    }
}
