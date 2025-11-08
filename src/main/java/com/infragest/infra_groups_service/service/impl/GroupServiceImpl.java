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

    private final GroupsRepository groupRepository;
    private final EmployeesRepository employeeRepository;

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
     * @throws RuntimeException si ocurre un error de persistencia
     */
    @Override
    public GroupRs createGroup(GroupRq rq) {
        try {
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
     * @throws RuntimeException si falla la lectura de datos
     */
    @Override
    public List<GroupRs> listGroups() {
        return List.of();
    }

    /**
     * Recupera un grupo por su id.
     *
     * @param id identificador del grupo
     * @return GroupRs si existe
     * @throws RuntimeException si el grupo no existe o falla la lectura
     */
    @Override
    public GroupRs getById(UUID id) {
        return null;
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
        return null;
    }

    /**
     * Elimina un grupo por id.
     *
     * @param id identificador del grupo a eliminar
     * @throws RuntimeException si no existe el grupo o falla la eliminación
     */
    @Override
    public void deleteGroup(UUID id) {

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
    public GroupRs assignEmployees(UUID id, AssignEmployeesRq rq) {
        return null;
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
