import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private GestorEmpleados gestor = new GestorEmpleados();
    private Empleado clonPendiente = null;

    private JTextField txtCodigo = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JComboBox<String> cbTipo = new JComboBox<String>();
    private JTextField txtBase = new JTextField();
    private JTextField txtBonificacion = new JTextField();
    private JTextField txtDescuento = new JTextField();
    private JTextField txtHoras = new JTextField();
    private JTextField txtTarifa = new JTextField();
    private JTextField txtBuscar = new JTextField(10);

    private JButton btnRegistrar = new JButton("Registrar");
    private JButton btnCalcular = new JButton("Calcular salario");
    private JButton btnClonar = new JButton("Clonar empleado");
    private JButton btnRegistrarClon = new JButton("Registrar clon");
    private JButton btnComparar = new JButton("Comparar objetos");
    private JButton btnMaximo = new JButton("Salario maximo");
    private JButton btnPromedio = new JButton("Promedio");
    private JButton btnLimpiar = new JButton("Limpiar");
    private JButton btnBuscar = new JButton("Buscar");

    private DefaultTableModel modelo = new DefaultTableModel();
    private JTable tabla = new JTable(modelo);

    public VentanaPrincipal() {
        setTitle("Sistema de Empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cbTipo.addActionListener(e -> {
            boolean admin = cbTipo.getSelectedItem().equals("Administrativo");
            txtBonificacion.setEnabled(admin);
            txtDescuento.setEnabled(admin);
            txtHoras.setEnabled(!admin);
            txtTarifa.setEnabled(!admin);
        });
        cbTipo.addItem("Administrativo");
        cbTipo.addItem("Por horas");

        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Tipo");
        modelo.addColumn("Salario calculado");
        modelo.addColumn("Categoria");
        tabla.setDefaultEditor(Object.class, null);

        JPanel panelForm = new JPanel(new GridLayout(4, 4, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del empleado"));
        panelForm.add(new JLabel("Codigo"));
        panelForm.add(txtCodigo);
        panelForm.add(new JLabel("Nombre"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Tipo"));
        panelForm.add(cbTipo);
        panelForm.add(new JLabel("Salario base"));
        panelForm.add(txtBase);
        panelForm.add(new JLabel("Bonificacion"));
        panelForm.add(txtBonificacion);
        panelForm.add(new JLabel("Descuento (%)"));
        panelForm.add(txtDescuento);
        panelForm.add(new JLabel("Horas"));
        panelForm.add(txtHoras);
        panelForm.add(new JLabel("Tarifa por hora"));
        panelForm.add(txtTarifa);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCalcular);
        panelBotones.add(btnClonar);
        panelBotones.add(btnRegistrarClon);
        panelBotones.add(btnComparar);
        panelBotones.add(btnMaximo);
        panelBotones.add(btnPromedio);
        panelBotones.add(btnLimpiar);
        panelBotones.add(new JLabel("Codigo:"));
        panelBotones.add(txtBuscar);
        panelBotones.add(btnBuscar);

        btnRegistrar.addActionListener(e -> {
            try {
                Empleado emp = leerDatos();
                gestor.registrar(emp);
                agregarFila(emp);
                limpiar();
                mensaje("Empleado registrado.");
            } catch (IllegalArgumentException ex) {
                mensaje(ex.getMessage());
            }
        });

        btnCalcular.addActionListener(e -> {
            try {
                Empleado emp;
                if (tabla.getSelectedRow() == -1) {
                    emp = leerDatos();
                } else {
                    emp = gestor.getEmpleados().get(tabla.getSelectedRow());
                }
                mensaje(emp.mostrarInformacion());
            } catch (IllegalArgumentException ex) {
                mensaje(ex.getMessage());
            }
        });

        btnBuscar.addActionListener(e -> {
            Empleado emp = gestor.buscar(txtBuscar.getText());
            if (emp == null) {
                mensaje("No existe un empleado con ese codigo.");
                return;
            }
            int fila = gestor.getEmpleados().indexOf(emp);
            tabla.setRowSelectionInterval(fila, fila);
            mensaje(emp.mostrarInformacion());
        });

        btnClonar.addActionListener(e -> {
            if (tabla.getSelectedRow() == -1) {
                mensaje("Seleccione un empleado de la tabla para clonar.");
                return;
            }
            Empleado original = gestor.getEmpleados().get(tabla.getSelectedRow());
            clonPendiente = gestor.clonar(original);
            txtCodigo.setText("");
            txtNombre.setText(clonPendiente.getNombre());
            txtBase.setText("" + clonPendiente.getSalarioBase());
            cbTipo.setSelectedItem(clonPendiente.getTipo());
            if (clonPendiente instanceof EmpleadoAdministrativo) {
                EmpleadoAdministrativo admin = (EmpleadoAdministrativo) clonPendiente;
                txtBonificacion.setText("" + admin.getBonificacion());
                txtDescuento.setText("" + admin.getDescuento());
                txtHoras.setText("");
                txtTarifa.setText("");
            } else {
                EmpleadoPorHoras horas = (EmpleadoPorHoras) clonPendiente;
                txtHoras.setText("" + horas.getHorasTrabajadas());
                txtTarifa.setText("" + horas.getTarifaPorHora());
                txtBonificacion.setText("");
                txtDescuento.setText("");
            }
            mensaje("Clon preparado. Escriba un codigo nuevo y pulse 'Registrar clon'");
        });

        btnRegistrarClon.addActionListener(e -> {
            if (clonPendiente == null) {
                mensaje("No hay clon pendiente. Use 'Clonar empleado' primero.");
                return;
            }
            try {
                gestor.registrarClon(clonPendiente, leerDatos());
                agregarFila(clonPendiente);
                limpiar();
                mensaje("Clon registrado.");
            } catch (IllegalArgumentException ex) {
                mensaje(ex.getMessage());
            }
        });

        btnComparar.addActionListener(e -> {
            int[] filas = tabla.getSelectedRows();
            if (filas.length != 2) {
                mensaje("Seleccione exactamente dos registros (Ctrl + clic).");
                return;
            }
            Empleado a = gestor.getEmpleados().get(filas[0]);
            Empleado b = gestor.getEmpleados().get(filas[1]);
            mensaje(gestor.comparar(a, b));
        });

        btnMaximo.addActionListener(e -> {
            Empleado emp = gestor.salarioMaximo();
            if (emp == null) {
                mensaje("No hay empleados registrados.");
                return;
            }
            mensaje("Mayor salario:\n" + emp.mostrarInformacion());
        });

        btnPromedio.addActionListener(e -> {
            if (gestor.getEmpleados().isEmpty()) {
                mensaje("No hay empleados registrados.");
                return;
            }
            mensaje("Salario promedio: " + String.format("%.2f", gestor.promedio()));
        });

        btnLimpiar.addActionListener(e -> {
            limpiar();
        });

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelForm, BorderLayout.NORTH);
        panelNorte.add(panelBotones, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Listado de empleados"));

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        setSize(960, 560);
        setLocationRelativeTo(null);
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    private Empleado leerDatos() {
        try {
            if (cbTipo.getSelectedItem().equals("Administrativo")) {
                return new EmpleadoAdministrativo(txtCodigo.getText(), txtNombre.getText(),
                        Double.parseDouble(txtBase.getText()),
                        Double.parseDouble(txtBonificacion.getText()),
                        Double.parseDouble(txtDescuento.getText()));
            }
            return new EmpleadoPorHoras(txtCodigo.getText(), txtNombre.getText(),
                    Double.parseDouble(txtBase.getText()),
                    Integer.parseInt(txtHoras.getText()),
                    Double.parseDouble(txtTarifa.getText()));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Revise los campos numericos: estan vacios o tienen formato invalido.");
        }
    }

    private void agregarFila(Empleado emp) {
        modelo.addRow(new String[] { emp.getCodigo(), emp.getNombre(), emp.getTipo(),
                String.format("%.2f", emp.calcularSalario()), emp.obtenerCategoria() });
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtBase.setText("");
        txtBonificacion.setText("");
        txtDescuento.setText("");
        txtHoras.setText("");
        txtTarifa.setText("");
        cbTipo.setSelectedIndex(0);
        clonPendiente = null;
        tabla.clearSelection();
    }
}
