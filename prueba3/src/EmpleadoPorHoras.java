public class EmpleadoPorHoras extends Empleado {
    private int horasTrabajadas;
    private double tarifaPorHora;

    public EmpleadoPorHoras(String codigo, String nombre, double salarioBase, int horasTrabajadas, double tarifaPorHora) {
        super(codigo, nombre, salarioBase);
        setHorasTrabajadas(horasTrabajadas);
        setTarifaPorHora(tarifaPorHora);
    }

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        if (horasTrabajadas < 0) {
            throw new IllegalArgumentException("Las horas no pueden ser negativas.");
        }
        this.horasTrabajadas = horasTrabajadas;
    }

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    public void setTarifaPorHora(double tarifaPorHora) {
        if (tarifaPorHora < 0) {
            throw new IllegalArgumentException("La tarifa no puede ser negativa.");
        }
        this.tarifaPorHora = tarifaPorHora;
    }

    @Override
    public double calcularSalario() {
        return getSalarioBase() + horasTrabajadas * tarifaPorHora;
    }

    @Override
    public String getTipo() {
        return "Por horas";
    }

    @Override
    public boolean existe(Empleado emple) {
        if (!super.existe(emple)) {
            return false;
        }
        EmpleadoPorHoras e = (EmpleadoPorHoras) emple;
        return horasTrabajadas == e.horasTrabajadas
                && tarifaPorHora == e.tarifaPorHora;
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion()
                + String.format("%nHoras: %d%nTarifa por hora: %.2f", horasTrabajadas, tarifaPorHora);
    }
}
