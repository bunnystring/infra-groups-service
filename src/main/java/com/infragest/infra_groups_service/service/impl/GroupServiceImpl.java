package com.infragest.infra_groups_service.service.impl;

import com.infragest.infra_groups_service.entity.Employees;
import com.infragest.infra_groups_service.entity.Group;
import com.infragest.infra_groups_service.exception.GroupException;
import com.infragest.infra_groups_service.model.AssignEmployeesRq;
import com.infragest.infra_groups_service.model.EmployeeSummaryDto;
import com.infragest.infra_groups_service.model.GroupRq;
import com.infragest.infra_groups_service.model.GroupRs;
import com.infragest.infra_groups_service.repository.EmployeesRepository;
import com.infragest.infra_groups_service.repository.GroupsRepository;
import com.infragest.infra_groups_service.service.GroupService;
import com.infragest.infra_groups_service.util.MessageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación de GroupService.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    /**
     * Inyección de dependencia: GroupsRepository
     */
    private final GroupsRepository groupRepository;

    /**
     * Inyección de dependencia: EmployeesRepository
     */
    private final EmployeesRepository employeeRepository;

    /**
     * Crea un constructor con los repositorios necesarios para el servicio.
     * @param groupRepository
     * @param employeeRepository
     */
    public GroupServiceImpl(GroupsRepository groupRepository,
                            EmployeesRepository employeeRepository) {
        this.groupRepository = groupRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Crea un grupo nuevo.
     *
     * @param rq datos del grupo a crear
     * @return representación del grupo creado
     * @throws GroupException si ocurre un error de persistencia
     */
    @Override
    public GroupRs createGroup(GroupRq rq) {
        String name = rq.getName() == null ? "" : rq.getName().trim();

        try {
            if (groupRepository.existsByNameIgnoreCase(name)) {
                log.warn("Attempt to create group with existing name: {}", name);
                throw new GroupException(
                        String.format(MessageException.GROUP_ALREADY_EXISTS, name),
                        GroupException.Type.BAD_REQUEST
                );
            }

            Group g = Group.builder()
                    .name(rq.getName())
                    .address(rq.getAddress())
                    .employees(new HashSet<>())
                    .build();

            Group saved = groupRepository.save(g);
            return toRs(saved);
        } catch (org.springframework.dao.DataAccessException dae) {
            log.error("Error saving Group: {}", rq, dae);
            throw new GroupException(MessageException.DATABASE_ERROR, GroupException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Devuelve la lista de todos los grupos.
     *
     * @return lista de GroupRs
     * @throws GroupException si falla la lectura de datos
     */
    @Override
    @Transactional(readOnly = true)
    public List<GroupRs> listGroups() {
        try {
            return groupRepository.findAll()
                    .stream()
                    .map(this::toRs)
                    .collect(Collectors.toList());
        } catch (org.springframework.dao.DataAccessException dae) {
            log.error("Error reading from repository", dae);
            throw new GroupException(MessageException.DATABASE_ERROR, GroupException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Recupera un grupo por su id.
     *
     * @param id identificador del grupo
     * @return GroupRs si existe
     * @throws GroupException si el grupo no existe o falla la lectura
     */
    @Override
    public GroupRs getById(UUID id) {
        if (id == null) {
            log.error("getById called with null id");
            throw new GroupException(String.format(MessageException.INVALID_UUID, "null"), GroupException.Type.BAD_REQUEST);
        }

        try {
            return groupRepository.findById(id)
                    .map(this::toRs)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.GROUP_NOT_FOUND, id);
                        log.debug("Group not found: {}", id);
                        return new GroupException(msg, GroupException.Type.NOT_FOUND);
                    });
        } catch (org.springframework.dao.DataAccessException dae) {
            log.error("Error reading Group by id {}", id, dae);
            throw new GroupException(MessageException.DATABASE_ERROR, GroupException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Actualiza los datos de un grupo.
     *
     * @param id identificador del grupo a actualizar
     * @param rq datos a actualizar
     * @return grupo actualizado
     * @throws RuntimeException si no existe el grupo o falla la persistencia
     */
    @Override
    public GroupRs updateGroup(UUID id, GroupRq rq) {

        if (id == null) {
            log.warn("updateGroup called with null id");
            throw new GroupException(String.format(MessageException.INVALID_UUID, "null"), GroupException.Type.BAD_REQUEST);
        }
        if (rq == null) {
            log.warn("updateGroup called with null request for id {}", id);
            throw new GroupException(MessageException.INVALID_REQUEST, GroupException.Type.BAD_REQUEST);
        }
        String newName = rq.getName() == null ? null : rq.getName().trim();
        String newAddress = rq.getAddress() == null ? null : rq.getAddress().trim();

        if (newName != null && newName.isBlank()) {
            log.warn("updateGroup called with blank name for id {}", id);
            throw new GroupException(MessageException.INVALID_REQUEST, GroupException.Type.BAD_REQUEST);
        }
        try {
            Group existing = groupRepository.findById(id)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.GROUP_NOT_FOUND, id);
                        log.debug("Group not found for update: {}", id);
                        return new GroupException(msg, GroupException.Type.NOT_FOUND);
                    });

            // Si se solicita cambiar el nombre y es distinto (case-insensitive), validar unicidad
            if (newName != null && !newName.equalsIgnoreCase(existing.getName())) {
                if (groupRepository.existsByNameIgnoreCase(newName)) {
                    log.warn("Attempt to rename group {} to an existing name: {}", id, newName);
                    throw new GroupException(
                            String.format(MessageException.GROUP_ALREADY_EXISTS, newName),
                            GroupException.Type.BAD_REQUEST
                    );
                }
                existing.setName(newName);
            }

            // Actualizar dirección si fue enviada y cambia
            if (newAddress != null && !newAddress.equals(existing.getAddress())) {
                existing.setAddress(newAddress);
            }

            // persistir cambios
            Group saved = groupRepository.save(existing);
            return toRs(saved);

        } catch (DataAccessException dae) {
            log.error("Error updating Group id {} with payload {}", id, rq, dae);
            throw new GroupException(MessageException.DATABASE_ERROR, GroupException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Elimina un grupo por id.
     *
     * @param id identificador del grupo a eliminar
     * @throws RuntimeException si no existe el grupo o falla la eliminación
     */
    @Override
    public void deleteGroup(UUID id) {
        if (id == null) {
            log.warn("deleteGroup called with null id");
            throw new GroupException(String.format(MessageException.INVALID_UUID, "null"), GroupException.Type.BAD_REQUEST);
        }
        try {
            Group existing = groupRepository.findById(id)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.GROUP_NOT_FOUND, id);
                        log.debug("Group not found for deletion: {}", id);
                        return new GroupException(msg, GroupException.Type.NOT_FOUND);
                    });

            // Regla de negocio: no permitir eliminar un grupo que tenga empleados asociados
            if (existing.getEmployees() != null && !existing.getEmployees().isEmpty()) {
                log.warn("Attempt to delete group {} which still has employees", id);
                throw new GroupException(
                        String.format(MessageException.GROUP_DELETE_NOT_ALLOWED, id),
                        GroupException.Type.BAD_REQUEST
                );
            }

            groupRepository.delete(existing);
            log.info("Group {} deleted", id);
        } catch (DataAccessException dae) {
            log.error("Error deleting Group id {}", id, dae);
            throw new GroupException(MessageException.DATABASE_ERROR, GroupException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Asigna una lista de empleados al grupo.
     *
     * @param id identificador del grupo
     * @param rq request con lista de employeeIds
     * @return grupo actualizado
     * @throws RuntimeException si el grupo no existe, si la lista es inválida,
     *                          si faltan empleados o falla la persistencia
     */
    @Override
    @Transactional
    public GroupRs assignEmployees(UUID id, AssignEmployeesRq rq) {
        if (id == null) {
            log.warn("assignEmployees called with null group id");
            throw new GroupException(String.format(MessageException.INVALID_UUID, "null"), GroupException.Type.BAD_REQUEST);
        }
        if (rq == null || rq.getEmployeeIds() == null || rq.getEmployeeIds().isEmpty()) {
            log.warn("assignEmployees called with invalid employee list for group {}", id);
            throw new GroupException(MessageException.INVALID_EMPLOYEE_LIST, GroupException.Type.BAD_REQUEST);
        }
        // Normalizar y duplicar ids solicitados
        Set<UUID> requestedIds = rq.getEmployeeIds().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (requestedIds.isEmpty()) {
            log.warn("assignEmployees called with empty/only-null ids for group {}", id);
            throw new GroupException(MessageException.INVALID_EMPLOYEE_LIST, GroupException.Type.BAD_REQUEST);
        }

        try {
            Group existing = groupRepository.findById(id)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.GROUP_NOT_FOUND, id);
                        log.debug("Group not found for assignEmployees: {}", id);
                        return new GroupException(msg, GroupException.Type.NOT_FOUND);
                    });

            // Recuperar empleados por ids
            List<Employees> found = employeeRepository.findAllById(requestedIds);

            // Comprobar si hay ids faltantes
            Set<UUID> foundIds = found.stream()
                    .map(Employees::getId)
                    .collect(Collectors.toSet());

            Set<UUID> missing = new HashSet<>(requestedIds);
            missing.removeAll(foundIds);
            if (!missing.isEmpty()) {
                UUID missingId = missing.iterator().next();
                log.debug("Employee not found when assigning to group {}: {}", id, missingId);
                throw new GroupException(String.format(MessageException.EMPLOYEE_NOT_FOUND, missingId), GroupException.Type.BAD_REQUEST);
            }

            // Comprobar estado y pertenencia actual
            Set<UUID> existingEmployeeIds = existing.getEmployees() == null
                    ? Collections.emptySet()
                    : existing.getEmployees().stream()
                    .map(Employees::getId)
                    .collect(Collectors.toSet());

            for (Employees e : found) {
                // Validar estado (asumimos que status.toString() contiene algo como "ACTIVE")
                String status = String.valueOf(e.getStatus());
                if (status == null || !"ACTIVE".equalsIgnoreCase(status)) {
                    log.warn("Attempt to assign inactive employee {} to group {}", e.getId(), id);
                    throw new GroupException(String.format(MessageException.EMPLOYEE_NOT_ACTIVE, e.getId()), GroupException.Type.BAD_REQUEST);
                }

                // Validar si ya pertenece
                if (existingEmployeeIds.contains(e.getId())) {
                    log.warn("Attempt to assign employee {} who is already in group {}", e.getId(), id);
                    throw new GroupException(String.format(MessageException.EMPLOYEE_ALREADY_IN_GROUP, e.getId()), GroupException.Type.BAD_REQUEST);
                }
            }

            // Asignar todos los empleados (añadir al Set)
            if (existing.getEmployees() == null) {
                existing.setEmployees(new HashSet<>(found));
            } else {
                existing.getEmployees().addAll(found);
            }

            Group saved = groupRepository.save(existing);
            return toRs(saved);

        } catch (DataAccessException dae) {
            log.error("Error assigning employees to Group id {} with payload {}", id, rq, dae);
            throw new GroupException(MessageException.DATABASE_ERROR, GroupException.Type.INTERNAL_SERVER);
        }
    }

    /**
     * Remueve un empleado del grupo.
     *
     * @param groupId    identificador del grupo
     * @param employeeId identificador del empleado a remover
     * @throws RuntimeException si el grupo o el empleado no existen o el empleado no pertenece al grupo
     */
    @Override
    public void removeEmployee(UUID groupId, UUID employeeId) {

        if (groupId == null) {
            log.warn("removeEmployee called with null groupId");
            throw new GroupException(String.format(MessageException.INVALID_UUID, "null"), GroupException.Type.BAD_REQUEST);
        }
        if (employeeId == null) {
            log.warn("removeEmployee called with null employeeId for group {}", groupId);
            throw new GroupException(String.format(MessageException.INVALID_UUID, "null"), GroupException.Type.BAD_REQUEST);
        }

        try {
            Group existing = groupRepository.findById(groupId)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.GROUP_NOT_FOUND, groupId);
                        log.debug("Group not found for removeEmployee: {}", groupId);
                        return new GroupException(msg, GroupException.Type.NOT_FOUND);
                    });

            Employees employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> {
                        String msg = String.format(MessageException.EMPLOYEE_NOT_FOUND, employeeId);
                        log.debug("Employee not found for removeEmployee: {}", employeeId);
                        return new GroupException(msg, GroupException.Type.BAD_REQUEST);
                    });

            // Comprobar pertenencia
            boolean belongs = existing.getEmployees() != null &&
                    existing.getEmployees().stream()
                            .anyMatch(e -> Objects.equals(e.getId(), employeeId));

            if (!belongs) {
                log.warn("Attempt to remove employee {} who is not in group {}", employeeId, groupId);
                String msg = MessageException.EMPLOYEE_NOT_IN_GROUP != null
                        ? String.format(MessageException.EMPLOYEE_NOT_IN_GROUP, employeeId)
                        : String.format("Employee %s is not a member of group %s", employeeId, groupId);
                throw new GroupException(msg, GroupException.Type.BAD_REQUEST);
            }

            // Remover la relación y persistir
            existing.getEmployees().removeIf(e -> Objects.equals(e.getId(), employeeId));
            groupRepository.save(existing);
            log.info("Employee {} removed from group {}", employeeId, groupId);

        } catch (DataAccessException dae) {
            log.error("Error removing employee {} from group {}", employeeId, groupId, dae);
            throw new GroupException(MessageException.DATABASE_ERROR, GroupException.Type.INTERNAL_SERVER);
        }

    }

    /**
     * Mapea una entidad Group a su DTO GroupRs.
     *
     * @param g entidad Group
     * @return GroupRs mapeado
     */
    private GroupRs toRs(Group g) {
        GroupRs r = new GroupRs();
        r.setId(g.getId());
        r.setName(g.getName());
        r.setAddress(g.getAddress());
        r.setCreatedAt(g.getCreatedAt());
        r.setUpdatedAt(g.getUpdatedAt());

        if (g.getEmployees() != null && !g.getEmployees().isEmpty()) {
            Set<EmployeeSummaryDto> ems = g.getEmployees().stream()
                    .map(this::toEmployeeSummary)
                    .collect(Collectors.toSet());
            r.setEmployees(ems);
        } else {
            r.setEmployees(Collections.emptySet());
        }

        return r;
    }

    /**
     * Mapea una entidad Employees a EmployeeSummaryDto.
     *
     * @param e entidad Employees
     * @return EmployeeSummaryDto con datos resumidos del empleado
     */
    private EmployeeSummaryDto toEmployeeSummary(Employees e) {
        return EmployeeSummaryDto.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .email(e.getEmail())
                .status(e.getStatus())
                .build();
    }
}
