package com.infragest.infra_groups_service.exception;

public class EmployeeException extends RuntimeException {

    /**
     * Tipos de error usados por {@link EmployeeException}.
     */
    public enum Type {
        NOT_FOUND,
        BAD_REQUEST,
        INTERNAL_SERVER
    }

    /**
     * Tipo de la excepción (clasificación).
     */
    private final Type type;

    public EmployeeException(String message, Type type) {
        super(message);
        this.type = type;
    }

    /**
     * Obtiene el tipo (clasificación) de la excepción.
     *
     * @return el {@link EmployeeException.Type} asociado a esta excepción
     */
    public EmployeeException.Type getType() {
        return type;
    }
}
