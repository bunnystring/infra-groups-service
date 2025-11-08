package com.infragest.infra_groups_service.util;

/**
 * Mensajes de error reutilizables.
 * Algunos mensajes incluyen placeholders (%s) para usar con String.format.
 */
public abstract class MessageException {

    private MessageException() {}

    public static final String GROUP_NOT_FOUND = "Group no encontrado: %s";
    public static final String GROUP_ALREADY_EXISTS = "Group ya existe: %s";
    public static final String GROUP_DELETE_NOT_ALLOWED = "No se puede eliminar el grupo: %s";

    public static final String EMPLOYEE_NOT_FOUND = "Employee no encontrado: %s";
    public static final String EMPLOYEE_NOT_ACTIVE = "Employee %s no está activo";
    public static final String EMPLOYEE_ALREADY_IN_GROUP = "Employee %s ya pertenece al grupo";
    public static final String EMPLOYEE_NOT_IN_GROUP = "Employee %s no pertenece al grupo";

    public static final String INVALID_EMPLOYEE_LIST = "Lista de empleados inválida";
    public static final String INVALID_REQUEST = "Request inválido";
    public static final String INVALID_UUID = "Identificador inválido: %s";

    public static final String OPERATION_NOT_ALLOWED = "Operación no permitida: %s";
    public static final String DATABASE_ERROR = "Error en la base de datos";
    public static final String INTERNAL_ERROR = "Error interno del servidor";


}
