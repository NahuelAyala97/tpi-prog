package main;

import service.HistoriaClinicaService;
import entities.Paciente;
import entities.GrupoSanguineo;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import services.PacienteService;

public class AppMenu {

    private final Scanner scanner;
    private final PacienteService pacienteService;

    public AppMenu() {
        scanner = new Scanner(System.in);
        pacienteService = new PacienteService();
        new HistoriaClinicaService();
    }

    public void iniciar() {
        int opcion = -1;
        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> crearPaciente();
                    case 2 -> listarPacientes();
                    case 3 -> buscarPorDni();
                    case 4 -> actualizarPaciente();
                    case 5 -> eliminarPaciente();
                    case 0 -> System.out.println("👋 Saliendo del sistema...");
                    default -> System.out.println("❌ Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Ingrese un número válido.");
            }
        } while (opcion != 0);
    }

    private void mostrarMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Crear paciente");
        System.out.println("2. Listar pacientes activos");
        System.out.println("3. Buscar paciente por DNI");
        System.out.println("4. Actualizar datos del paciente");
        System.out.println("5. Eliminar (baja física)");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }


    // CREAR PACIENTE (Historia clínica automática)
    private void crearPaciente() {
        try {
            System.out.println("\n=== Nuevo Paciente ===");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();

            System.out.print("DNI: ");
            int dni = Integer.parseInt(scanner.nextLine());

            System.out.print("Fecha de nacimiento (AAAA-MM-DD): ");
            LocalDate fecha = LocalDate.parse(scanner.nextLine().trim());

            // CREAR OBJETO PACIENTE (la HC se asigna automáticamente después)
            Paciente p = new Paciente(
                    0,
                    nombre,
                    apellido,
                    dni,
                    fecha,
                    null
            );

            pacienteService.crearPacienteAsignandoHistoria(p);

            System.out.println("✅ Paciente creado correctamente.");

        } catch (Exception e) {
            System.out.println("❌ Error al crear paciente: " + e.getMessage());
        }
    }

    // LISTAR PACIENTES
    private void listarPacientes() {
        try {
            System.out.println("\n=== LISTADO DE PACIENTES ===");
            List<Paciente> lista = pacienteService.obtenerTodos();
            if (lista.isEmpty()) {
                System.out.println("No hay pacientes cargados.");
            } else {
                lista.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("❌ Error al listar pacientes: " + e.getMessage());
        }
    }


    // BUSCAR POR DNI
    private void buscarPorDni() {
        System.out.print("Ingrese DNI a buscar: ");
        String dni = scanner.nextLine().trim();
        try {
            Paciente p = pacienteService.buscarPorDni(dni);
            if (p != null) {
                System.out.println(p);
            } else {
                System.out.println("⚠️ No se encontró un paciente con ese DNI.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al buscar: " + e.getMessage());
        }
    }


    // ACTUALIZAR PACIENTE
    private void actualizarPaciente() {
        try {
            System.out.print("Ingrese ID del paciente a actualizar: ");
            Long id = Long.parseLong(scanner.nextLine());
            Paciente existente = pacienteService.obtenerPorId(id);

            if (existente == null) {
                System.out.println("⚠️ No existe paciente con ese ID.");
                return;
            }

            System.out.println("Paciente actual: " + existente);

            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nombre = scanner.nextLine();
            if (!nombre.isBlank()) existente.setNombre(nombre);

            System.out.print("Nuevo apellido (Enter para mantener): ");
            String apellido = scanner.nextLine();
            if (!apellido.isBlank()) existente.setApellido(apellido);

            pacienteService.actualizar(existente);
            System.out.println("✅ Paciente actualizado.");

        } catch (Exception e) {
            System.out.println("❌ Error al actualizar: " + e.getMessage());
        }
    }


    // ELIMINAR PACIENTE
    private void eliminarPaciente() {
        try {
            System.out.print("Ingrese ID del paciente a eliminar: ");
            Long id = Long.parseLong(scanner.nextLine());
            pacienteService.eliminarPaciente(id);
            System.out.println("✅ Paciente eliminado.");
        } catch (Exception e) {
            System.out.println("❌ Error al eliminar: " + e.getMessage());
        }
    }
}
