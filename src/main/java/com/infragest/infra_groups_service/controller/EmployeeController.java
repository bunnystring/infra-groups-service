package com.infragest.infra_groups_service.controller;

import com.infragest.infra_groups_service.model.EmployeeRq;
import com.infragest.infra_groups_service.model.EmployeeRs;
import com.infragest.infra_groups_service.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("groups/employees")
public class EmployeeController {

    /**
     * Inyección de la dependencia: EmployeeService.
     */
    private final EmployeeService employeeService;

    /**
     * Constructor para la inyección de dependencias.
     * @param employeeService
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Crea un nuevo empleado.
     *
     * @param rq datos del empleado a crear
     * @return {@link EmployeeRs} del empleado creado
     */
    @PostMapping
    public ResponseEntity<EmployeeRs> createEmployee(@Valid @RequestBody EmployeeRq rq) {
        return ResponseEntity.ok(employeeService.createEmployee(rq));
    }

    /**
     * Devuelve la lista de todos los empleados.
     *
     * @return lista de {@link EmployeeRs}
     */
    @GetMapping
    public ResponseEntity<List<EmployeeRs>> listEmployees() {
        return ResponseEntity.ok(employeeService.listEmployees());
    }

    /**
     * Recupera un empleado por su id.
     *
     * @param id identificador del empleado
     * @return {@link EmployeeRs} si existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRs> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    /**
     * Actualiza un empleado existente.
     *
     * @param id identificador del empleado a actualizar
     * @param rq datos a actualizar
     * @return {@link EmployeeRs} con los datos actualizados
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeRs> updateEmployee(@PathVariable UUID id,
                                                     @Valid @RequestBody EmployeeRq rq) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, rq));
    }

    /**
     * Elimina un empleado por ID.
     *
     * @param id identificador del empleado
     * @return ResponseEntity sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}
