
public abstract class Empleado implements Cloneable {

    private String codigo;
    private String nombre;
    private double salarioBase;

    public Empleado(String codigo, String nombre, double salarioBase) {
        setCodigo(codigo);
        setNombre(nombre);
        setSalarioBase(salarioBase);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isEmpty()){
            throw new IllegalArgumentException("El codigo no puede estar vacio.");
        }
        this.codigo = codigo.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre es no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        if (salarioBase < 0) {
            throw new IllegalArgumentException("El salario base no puede ser negativo.");
        }
        this.salarioBase = salarioBase;
    }

    public abstract double calcularSalario();

    public abstract String getTipo();

    public boolean existe(Empleado emple) {
        return emple != null
                && codigo.equals(emple.codigo)
                && nombre.equals(emple.nombre)
                && salarioBase == emple.salarioBase;
    }

    public String mostrarInformacion() {
        return String.format("Codigo: %s%nNombre: %s%nTipo: %s%nSalario base: %.2f%nSalario calculado: %.2f%nCategoria: %s",
                codigo, nombre, getTipo(), salarioBase, calcularSalario(), obtenerCategoria());
    }

    public String obtenerCategoria() {
        double salario = calcularSalario();
        if (salario < 1000) {
            return "Categoria A";
        } else if (salario < 3000) {
            return "Categoria B";
        } else if (salario < 5000) {
            return "Categoria C";
        }else{
            return "Categoria D";
        }
    }

    @Override
    public Empleado clone() {
        try {
            return (Empleado) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar la clase");
        }
        
    }
}
