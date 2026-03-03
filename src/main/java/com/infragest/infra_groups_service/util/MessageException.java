package com.infragest.infra_groups_service.util;

/**
 * Mensajes de error reutilizables.
 * Algunos mensajes incluyen placeholders (%s) para usar con String.format.
 */
public abstract class MessageException {

    private MessageException() {}

    public static final String GROUP_NOT_FOUND = "Group not found: %s";
    public static final String GROUP_ALREADY_EXISTS = "Group already exists: %s";
    public static final String GROUP_DELETE_NOT_ALLOWED = "Group cannot be deleted: %s";

    public static final String EMPLOYEE_NOT_FOUND = "Employee not found: %s";
    public static final String EMPLOYEE_NOT_ACTIVE = "Employee %s is not active";
    public static final String EMPLOYEE_ALREADY_IN_GROUP = "Employee %s already belongs to the group";
    public static final String EMPLOYEE_NOT_IN_GROUP = "Employee %s does not belong to the group";
    public static final String EMPLOYEE_ALREADY_EXISTS = "Employee already exists: %s";
    public static final String EMPLOYEE_NOT_FOUND_IN_GROUP = "No employees found in the group: %s";
    public static final String EMPLOYEE_NO_VALID_EMAILS_IN_GROUP = "No valid emails associated with the group: %s";
    public static final String EMPLOYEE_CANNOT_BE_REMOVED_FROM_GROUP = "The employee %s cannot be deleted because they are associated with one or more groups.";

    public static final String INVALID_EMPLOYEE_LIST = "Invalid employee list";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String INVALID_UUID = "Invalid identifier: %s";

    public static final String OPERATION_NOT_ALLOWED = "Operation not allowed: %s";
    public static final String DATABASE_ERROR = "Database error";
    public static final String INTERNAL_ERROR = "Internal server error";
    public static final String NO_VALID_EMPLOYEES_TO_ASSIGN = "There aren't any valid employees to assign to the group";

}
