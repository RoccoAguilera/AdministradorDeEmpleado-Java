package proyectofinallogicadeprogramación;

import java.util.ArrayList;
import java.util.Scanner;

public class ProyectoFinalLogicaDeProgramación {

    static ArrayList<String[]> employees = new ArrayList<>();
    static ArrayList<String[][]> history = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int option;
        initInitialData();
        do {
            option = showMainMenu();
            switch (option) {
                case 1: {
                    do {
                        option = showActionMenu();
                        switch (option) {
                            case 1:
                                addEmployee();
                                break;
                            case 2:
                                searchEmployee();
                                break;
                            case 3:
                                editEmployee();
                                break;
                            case 4:
                                deleteEmployee();
                                break;
                            case 5:
                                deleteLastEmployee();
                                break;
                            case 6:
                                sortEmployees();
                                break;
                            case 7:
                                clearEmployee();
                                break;
                            case 8:
                                break;
                            default:
                                System.out.println("\nOpcion invalida.");
                                break;
                        }
                    } while (option != 8);
                    break;
                }
                case 2: {
                    do {
                        option = showViewMenu();
                        switch (option) {
                            case 1:
                                showAllEmployees();
                                break;
                            case 2:
                                showAllEmployeesReversed();
                                break;
                            case 3:
                                showLastEmployee();
                                break;
                            case 4:
                                showTotalCount();
                                break;
                            case 5:
                                ShowHistory();
                                break;
                            case 6:
                                break;
                            default:
                                System.out.println("\nOpcion invalida.");
                                break;
                        }
                    } while (option != 6);
                    break;
                }
                case 3: {
                    System.out.println("Saliendo...");
                    break;
                }
                default: {
                    System.out.println("\nOpcion invalida.");
                    break;
                }
            }
        } while (option != 3);
    }

    public static String getValidString(String message) {
        String value;
        do {
            System.out.print(message);
            value = sc.nextLine();
            if (!value.trim().isEmpty()) {
                return value;
            } else {
                System.out.println("Error: Valor vacio, intente de nuevo.");
            }
        } while (true);
    }

    public static String getValidSalary(String message) {
        double value;
        do {
            System.out.print(message);
            try {
                value = sc.nextDouble();
                if (value >= 0) {
                    sc.nextLine();
                    return String.valueOf(value);
                } else {
                    System.out.println("Error: Valor negativo, intente de nuevo.");
                    sc.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Error: Debe ingresar un valor numerico.");
                sc.nextLine(); // Evita el bucle infinito limpiando el texto erroneo
            }
        } while (true);
    }

    public static String getValidField(int dataPosition, String flagMessage, boolean editMode, int indexInput) {
        boolean isValid = true;
        String value;

        do {
            String input = getValidString(flagMessage);
            int recoverIndex = searchEmployeeInArray(dataPosition, input);

            if (recoverIndex != -1) {
                String recoverInput = employees.get(indexInput)[dataPosition];
                if (recoverInput.equalsIgnoreCase(input) && editMode) {
                    System.out.println("El valor ingresado es el mismo al valor anterior,");
                    System.out.println("Deseas guardarlo igual? 1. Si / 2. No.");
                    System.out.print("Opcion: ");
                    try {
                        if (sc.nextInt() == 1) {
                            isValid = false;
                        }
                    } catch (Exception e) {
                        System.out.println("Error de formato, se asume 'No'.");
                    } finally {
                        sc.nextLine();
                    }
                } else {
                    System.out.println("El valor ingresado ya le pertenece a un empleado.");
                }
            } else {
                isValid = false;
            }

            value = input;
        } while (isValid);
        return value;
    }

    public static boolean isEmptyArray() {
        if (employees.isEmpty()) {
            System.out.println("\nNo hay empleado registrado.");
            return true;
        }
        return false;
    }

    /*
        Métodos: Crear, Buscar, Actualizar y Eliminar.
     */
    public static String[] showInputEmployee() {
        return showInputEmployee(false, 0);
    }

    public static String[] showInputEmployee(boolean editMode, int indexInput) {
        String[] employee = new String[5];
        employee[0] = getValidField(0, "Cedula: ", editMode, indexInput);
        employee[1] = getValidString("Nombre: ");
        employee[2] = getValidString("Apellido: ");
        employee[3] = getValidField(3, "Correo: ", editMode, indexInput);
        employee[4] = getValidSalary("Sueldo: ");
        return employee;
    }

    public static int searchEmployeeInArray(int index, String value) {
        for (int i = 0; i < employees.size(); i++) {
            String temp = employees.get(i)[index];
            if (temp.equalsIgnoreCase(value.trim())) {
                return i;
            }
        }
        return -1;
    }

    public static int recoverEmployeeIndex() {
        String value = getValidString("Cedula o Email: ");
        int id = searchEmployeeInArray(0, value);
        int email = searchEmployeeInArray(3, value);
        return (id != -1) ? id : (email != -1) ? email : -1;
    }

    public static void addEmployee() {
        textFlag("NUEVO EMPLEADO");
        String[] input = showInputEmployee();
        employees.addFirst(input);
        System.out.println("\nEmpleado registrado con exito.");
    }

    public static void editEmployee() {
        if (isEmptyArray()) {
            return;
        }

        textFlag("EDITAR EMPLEADO");
        int index = recoverEmployeeIndex();
        textFlag(null, false);
        if (index != -1) {
            showEmployee(index, true);
            textFlag(null, false);
            String[][] employeeHistory = new String[2][5];
            employeeHistory[0] = showInputEmployee(true, index);
            employeeHistory[1] = employees.set(index, employeeHistory[0]);
            textFlag(null, false);
            showCompareEmployee(employeeHistory[1], employeeHistory[0]);
            textFlag();
            history.addFirst(employeeHistory);
            System.out.println("Empleado actualizado con exito.");
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    public static void deleteEmployee() {
        if (isEmptyArray()) {
            return;
        }

        textFlag("ELIMINAR EMPLEADO");
        int index = recoverEmployeeIndex();
        textFlag(null, false);
        if (index != -1) {
            String[] employeeData = employees.remove(index);
            showEmployee(employeeData, false);
            textFlag();
            System.out.println("Empleado eliminado con exito.");
        } else {
            System.out.println("Empleado no encontrado.");
        }
    }

    public static void deleteLastEmployee() {
        if (isEmptyArray()) {
            return;
        }

        textFlag("ELIMINAR ULTIMO EMPLEADO");
        String[] employeeData = employees.removeFirst();
        showEmployee(employeeData, true);
        textFlag(null);
        System.out.println("Empleado eliminado con exito.");
    }

    public static void searchEmployee() {
        if (isEmptyArray()) {
            return;
        }

        textFlag("BUSCAR EMPLEADO");
        int index = recoverEmployeeIndex();
        if (index != -1) {
            textFlag(null, false);
            System.out.printf("Posicion: %s\n", index + 1);
            showEmployee(index, false);
            textFlag();
            System.out.println("Empleado encontrado con exito.");
        } else {
            textFlag();
            System.out.println("Empleado no encontrado.");
        }
    }

    public static void sortEmployees() {
        if (isEmptyArray()) {
            return;
        }

        int option;
        do {
            textFlag("ORDENAR POR:");
            System.out.println("1. Cedula\n2. Nombre\n3. Apellido\n4. Correo\n5. Sueldo");
            textFlag();
            System.out.print("Option: ");
            try {
                option = sc.nextInt();
                sc.nextLine();
                if (option > 0 && option <= 5) {
                    break;
                } else {
                    System.out.println("\nOpcion invalida.");
                }
            } catch (Exception e) {
                System.out.println("\nError: Debe ingresar un numero entero.");
                sc.nextLine();
            }
        } while (true);

        final int value = option - 1;
        ArrayList<String[]> copyEmployees = (ArrayList) employees.clone();
        copyEmployees.sort((current, next) -> {
            return current[value].compareToIgnoreCase(next[value]);
        });
        textFlag("EMPLEADOS ORDENADOS");
        for (String[] i : copyEmployees) {
            showEmployee(i, true);
            textFlag(null, false);
        }
    }

    public static void clearEmployee() {
        if (isEmptyArray()) {
            return;
        }

        employees.clear();
        System.out.println("\nLos registros se ha limpiado con exito.");
    }

    /*
        Mostrar: Ménu, Empleado, Empleados.
     */
    public static void textFlag() {
        textFlag(null, true);
    }

    public static void textFlag(String message) {
        textFlag(message, true);
    }

    public static void textFlag(String message, boolean onSeparator) {
        int length = 30;
        String template = "-";
        String separator = (onSeparator) ? "\n" : "";
        String output;
        if (message != null && !message.trim().isEmpty()) {
            int messagenLength = message.length();
            String part = template.repeat((length - messagenLength) / 2);
            output = separator + String.join(" ", part, message, part);
        } else {
            output = template.repeat(length + 2) + separator;
        }
        System.out.println(output);
    }

    public static int inputOption() {
        int option = -1;
        try {
            option = sc.nextInt();
        } catch (Exception e) {
            option = -1;
        } finally {
            sc.nextLine();
        }
        return option;
    }

    public static int showMainMenu() {
        textFlag("MENU PRINCIPAL");
        System.out.println("1. Acciones");
        System.out.println("2. Mostrar");
        System.out.println("3. Salir");
        textFlag();
        System.out.print("Opcion: ");
        return inputOption();
    }

    public static int showActionMenu() {
        textFlag("MENU ACCIONES");
        System.out.println("1. Agregar empleado");
        System.out.println("2. Buscar empleado");
        System.out.println("3. Editar empleado");
        System.out.println("4. Eliminar empleado");
        System.out.println("5. Eliminar ultimo empleado");
        System.out.println("6. Ordenar empleados");
        System.out.println("7. Limpiar los registros");
        System.out.println("8. Regresar");
        textFlag();
        System.out.print("Opcion: ");
        return inputOption();
    }

    public static int showViewMenu() {
        textFlag("MENU MOSTRAR");
        System.out.println("1. Mostrar todos los registros");
        System.out.println("2. Mostrar todos los registros en reverso");
        System.out.println("3. Mostrar ultimo registros");
        System.out.println("4. Mostrar cantidad de registros");
        System.out.println("5. Mostrar historial de cambios");
        System.out.println("6. Regresar");
        textFlag();
        System.out.print("Opcion: ");
        return inputOption();
    }

    public static void showEmployee(int index, boolean option) {
        String[] employee = employees.get(index);
        showEmployee(employee, option);
    }

    public static void showEmployee(String[] data, boolean option) {
        if (option) {
            System.out.printf(
                    "Cedula: %s \nNombre: %s %s \nCorreo: %s \nSalario: %s\n",
                    data[0], data[1], data[2], data[3], data[4]
            );
        } else {
            System.out.printf(
                    "Nombre: %s %s \nCorreo: %s \nSalario: %s\n",
                    data[1], data[2], data[3], data[4]
            );
        }
    }

    public static void showCompareEmployee(String[] oldData, String[] newData) {
        System.out.printf("Cedula: %s -> %s\n", oldData[0], newData[0]);
        System.out.printf("Nombre: %s -> %s\n", oldData[1], newData[1]);
        System.out.printf("Apellido: %s -> %s\n", oldData[2], newData[2]);
        System.out.printf("Correo: %s -> %s\n", oldData[3], newData[3]);
        System.out.printf("Sueldo: %s -> %s\n", oldData[4], newData[4]);
    }

    public static void showAllEmployees() {
        if (isEmptyArray()) {
            return;
        }

        textFlag("EMPLEADOS");
        for (String[] i : employees) {
            showEmployee(i, true);
            textFlag(null, false);
        }
    }

    public static void showAllEmployeesReversed() {
        if (isEmptyArray()) {
            return;
        }

        textFlag("EMPLEADOS REVERSO");
        for (String[] i : employees.reversed()) {
            showEmployee(i, true);
            textFlag(null, false);
        }
    }

    public static void showLastEmployee() {
        if (isEmptyArray()) {
            return;
        }

        textFlag("ULTIMO REGISTRADO");
        showEmployee(employees.getFirst(), true);
        textFlag(null, false);
    }

    public static void showTotalCount() {
        textFlag("CANTIDAD DE REGISTRADO");
        System.out.println("Total: " + employees.size());
        textFlag(null, false);
    }

    public static void ShowHistory() {
        if (history.isEmpty()) {
            System.out.println("\nNo hay historial.");
            return;
        }

        textFlag("HISTORIAL DE CAMBIOS");
        for (String[][] i : history) {
            showCompareEmployee(i[0], i[1]);
            textFlag(null, false);
        }
    }

    public static void initInitialData() {
        String[][] value = {
            {"01", "Miguel", "Gonzale", "miguel@email.com", "575.264"},
            {"02", "Jose", "Sanchez", "jose@email.com", "480.50"},
            {"03", "Luis", "Mercedes", "luis@email.com", "620.74"},
            {"04", "Antonio", "Martinez", "antonio@email.com", "525.25"},
            {"05", "Alexander", "Perez", "alexander@email.com", "500.25"},
            {"06", "Fernando", "Hernandez", "fernando@email.com", "720"}
        };
        for (String[] i : value) {
            employees.addFirst(i);
        }
    }
}
