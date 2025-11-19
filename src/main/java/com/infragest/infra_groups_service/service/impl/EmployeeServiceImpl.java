package com.infragest.infra_groups_service.service.impl;

import com.infragest.infra_groups_service.entity.Employees;
import com.infragest.infra_groups_service.enums.EmployeStatus;
import com.infragest.infra_groups_service.exception.EmployeeException;
import com.infragest.infra_groups_service.model.EmployeeRq;
import com.infragest.infra_groups_service.model.EmployeeRs;
import com.infragest.infra_groups_service.model.EmployeeSummaryDto;
import com.infragest.infra_groups_service.repository.EmployeesRepository;
import com.infragest.infra_groups_service.service.EmployeeService;
import com.infragest.infra_groups_service.util.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación de EmployeeService.
 *
 * Provee operaciones CRUD para empleados: validaciones básicas (email único, campos obligatorios),
 * asigna estado por defecto, mapea entidad <-> DTO y maneja errores de persistencia lanzando EmployeeException.
 *
 * @author bunnystring
 * @since 2025-11-11
 */
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    /**
     * Inyección de dependencia: EmployeesRepository
     */
    private final EmployeesRepository employeesRepository;

    /**
     * Crea un constructor con los repositorios necesarios para el servicio.
     * @param employeesRepository
     */
    public EmployeeServiceImpl(EmployeesRepository employeesRepository) {
        this.employeesRepository = employeesRepository;
    }

    /**
     * Crea un nuevo empleado en la base de datos.
     *
     * @param rq DTO con datos para crear el empleado; no puede ser {@code null} y debe contener
     *           {@code fullName}, {@code email}, {@code documentType} y {@code documentNumber}.
     * @return DTO {@link EmployeeRs} del empleado creado
     * @throws EmployeeException con Type.BAD_REQUEST si la petición es inválida o el email ya existe
     * @throws EmployeeException con Type.INTERNAL_SERVER si ocurre un error de persistencia
     */
    @Override
    public EmployeeRs createEmployee(EmployeeRq rq) {
        if (rq == null) {
            log.warn("createEmployee called with null request");
            throw new EmployeeException(MessageException.INVALID_REQUEST, EmployeeException.Type.BAD_REQUEST);
        }

        String email = rq.getEmail() == null ? "" : rq.getEmail().trim();

        try {
            // Validación: email único (case-insensitive)
            if (employeesRepository.existsByEmailIgnoreCase(email)) {
                log.warn("Attempt to create employee with existing email: {}", email);
                throw new EmployeeException(String.format(MessageException.EMPLOYEE_ALREADY_EXISTS, email),
                        EmployeeException.Type.BAD_REQUEST);
            }

            // Construir entidad y aplicar valores por defecto
            Employees e = new Employees();
            e.setFullName(rq.getFullName());
            e.setDocumentType(rq.getDocumentType());
            e.setDocumentNumber(rq.getDocumentNumber());
            e.setEmail(email);
            e.setStatus(rq.getStatus() == null ? EmployeStatus.ACTIVE : rq.getStatus());

            Employees saved = employeesRepository.save(e);
            return toRs(saved);
        } catch (DataAccessException dae) {
            log.error("Error saving employee: {}", rq, dae);
            throw new EmployeeException(MessageException.DATABASE_ERROR, EmployeeException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Devuelve la lista de todos los empleados.
     *La operación se realiza en modo read-only para optimizar la interacción con JPA/Hibernate.
     *
     * @return lista de {@link EmployeeRs}
     * @throws EmployeeException con Type.INTERNAL_SERVER si ocurre un error de lectura
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeRs> listEmployees() {
        try {
            return employeesRepository.findAll()
                    .stream()
                    .map(this::toRs)
                    .collect(Collectors.toList());
        } catch (DataAccessException dae) {
            log.error("Error reading employees", dae);
            throw new EmployeeException(MessageException.DATABASE_ERROR, EmployeeException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Recupera un empleado por su identificador.
     *
     * @param id UUID del empleado; no puede ser {@code null}
     * @return {@link EmployeeRs} del empleado encontrado
     * @throws EmployeeException con Type.BAD_REQUEST si {@code id} es {@code null}
     * @throws EmployeeException con Type.NOT_FOUND si no existe el empleado
     * @throws EmployeeException con Type.INTERNAL_SERVER si ocurre un error de lectura
     */
    @Override
    @Transactional(readOnly = true)
    public EmployeeRs getById(UUID id) {
        if (id == null) {
            throw new EmployeeException(String.format(MessageException.INVALID_UUID, "null"), EmployeeException.Type.BAD_REQUEST);
        }
        try {
            return employeesRepository.findById(id)
                    .map(this::toRs)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.EMPLOYEE_NOT_FOUND, id);
                        log.debug("Employee not found: {}", id);
                        return new EmployeeException(msg, EmployeeException.Type.NOT_FOUND);
                    });
        } catch (DataAccessException dae) {
            log.error("Error reading employee by id {}", id, dae);
            throw new EmployeeException(MessageException.DATABASE_ERROR, EmployeeException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Actualiza un empleado existente.
     *
     * @param id UUID del empleado a actualizar
     * @param rq DTO con los campos a actualizar
     * @return {@link EmployeeRs} con los datos actualizados
     * @throws EmployeeException con Type.BAD_REQUEST si parámetros inválidos o email en conflicto
     * @throws EmployeeException con Type.NOT_FOUND si el empleado no existe
     * @throws EmployeeException con Type.INTERNAL_SERVER si ocurre un error de persistencia
     */
    @Override
    @Transactional
    public EmployeeRs updateEmployee(UUID id, EmployeeRq rq) {
        if (id == null) {
            throw new EmployeeException(String.format(MessageException.INVALID_UUID, "null"), EmployeeException.Type.BAD_REQUEST);
        }
        if (rq == null) {
            throw new EmployeeException(MessageException.INVALID_REQUEST, EmployeeException.Type.BAD_REQUEST);
        }

        String newEmail = rq.getEmail() == null ? null : rq.getEmail().trim();
        try {
            Employees existing = employeesRepository.findById(id)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.EMPLOYEE_NOT_FOUND, id);
                        log.debug("Employee not found for update: {}", id);
                        return new EmployeeException(msg, EmployeeException.Type.NOT_FOUND);
                    });

            if (newEmail != null && !newEmail.equalsIgnoreCase(existing.getEmail())) {
                if (employeesRepository.existsByEmailIgnoreCase(newEmail)) {
                    log.warn("Attempt to change employee {} email to existing email {}", id, newEmail);
                    throw new EmployeeException(String.format(MessageException.EMPLOYEE_ALREADY_EXISTS, newEmail),
                            EmployeeException.Type.BAD_REQUEST);
                }
                existing.setEmail(newEmail);
            }

            if (rq.getFullName() != null) {
                existing.setFullName(rq.getFullName());
            }
            if (rq.getStatus() != null) {
                existing.setStatus(rq.getStatus());
            }

            Employees saved = employeesRepository.save(existing);
            return toRs(saved);
        } catch (DataAccessException dae) {
            log.error("Error updating employee id {} payload {}", id, rq, dae);
            throw new EmployeeException(MessageException.DATABASE_ERROR, EmployeeException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Elimina un empleado por su identificador.
     *
     * @param id UUID del empleado a eliminar; no puede ser {@code null}
     * @throws EmployeeException con Type.BAD_REQUEST si {@code id} es {@code null}
     * @throws EmployeeException con Type.NOT_FOUND si no existe el empleado
     * @throws EmployeeException con Type.INTERNAL_SERVER si ocurre un error de persistencia
     */
    @Override
    @Transactional
    public void deleteEmployee(UUID id) {
        if (id == null) {
            throw new EmployeeException(String.format(MessageException.INVALID_UUID, "null"), EmployeeException.Type.BAD_REQUEST);
        }
        try {
            Employees existing = employeesRepository.findById(id)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.EMPLOYEE_NOT_FOUND, id);
                        log.debug("Employee not found for deletion: {}", id);
                        return new EmployeeException(msg, EmployeeException.Type.NOT_FOUND);
                    });
            employeesRepository.delete(existing);
            log.info("Employee {} deleted", id);
        } catch (DataAccessException dae) {
            log.error("Error deleting employee {}", id, dae);
            throw new EmployeeException(MessageException.DATABASE_ERROR, EmployeeException.Type.INTERNAL_SERVER);
        }
    }


    /**
     * Mapea entidad {@link Employees} a {@link EmployeeRs}.
     *
     * @param e entidad a mapear
     * @return DTO de respuesta
     */
    private EmployeeRs toRs(Employees e) {
        EmployeeRs r = new EmployeeRs();
        r.setId(e.getId());
        r.setFullName(e.getFullName());
        r.setEmail(e.getEmail());
        r.setStatus(e.getStatus());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        r.setVersion(e.getVersion());
        return r;
    }

    /**
     * Mapea entidad {@link Employees} a {@link EmployeeSummaryDto} (resumen).
     *
     * @param e entidad a mapear
     * @return DTO resumen
     */
    private EmployeeSummaryDto toSummary(Employees e) {
        return EmployeeSummaryDto.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .email(e.getEmail())
                .status(e.getStatus())
                .build();
    }
}
