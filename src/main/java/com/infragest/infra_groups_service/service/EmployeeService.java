package com.infragest.infra_groups_service.service;

import com.infragest.infra_groups_service.model.EmployeeRq;
import com.infragest.infra_groups_service.model.EmployeeRs;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para operaciones CRUD sobre empleados.
 *
 * <p>Define la API que debe exponer la implementación {@code EmployeeServiceImpl}:
 * listar, obtener por ID, crear, actualizar y eliminar empleados.</p>
 *
 * Las implementaciones deben lanzar {@link com.infragest.infra_groups_service.exception.EmployeeException}
 * en caso de error (NOT_FOUND, BAD_REQUEST, INTERNAL_SERVER).
 *
 * @author bunnystring
 * @since 2025-11-11
 */
public interface EmployeeService {

    /**
     * Devuelve la lista de todos los empleados.
     *
     * @return lista de {@link EmployeeRs}
     */
    List<EmployeeRs> listEmployees();

    /**
     * Recupera un empleado por su id.
     *
     * @param id identificador del empleado
     * @return {@link EmployeeRs} si existe
     */
    EmployeeRs getById(UUID id);

    /**
     * Crea un nuevo empleado.
     *
     * @param rq datos del empleado a crear
     * @return {@link EmployeeRs} del empleado creado
     */
    EmployeeRs createEmployee(EmployeeRq rq);

    /**
     * Actualiza un empleado existente.
     *
     * @param id identificador del empleado a actualizar
     * @param rq datos a actualizar
     * @return {@link EmployeeRs} con los datos actualizados
     */
    EmployeeRs updateEmployee(UUID id, EmployeeRq rq);

    /**
     * Elimina un empleado por id.
     *
     * @param id identificador del empleado a eliminar
     */
    void deleteEmployee(UUID id);

}
