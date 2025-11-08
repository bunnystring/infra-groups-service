package com.infragest.infra_groups_service.exception;

/**
 * Excepción de negocio para el módulo Group.
 * unchecked para permitir rollback automático en transacciones gestionadas por Spring.
 * Contiene un {@link Type} que clasifica el error.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
public class GroupException extends RuntimeException {

    /**
     * Tipos de error usados por {@link GroupException}.
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

    /**
     * Crea una nueva {@code GroupException}.
     *
     * @param message mensaje descriptivo del error
     * @param type    tipo de excepción
     */
    public GroupException(String message, Type type) {
      super(message);
      this.type = type;
    }

    /**
     * Obtiene el tipo (clasificación) de la excepción.
     *
     * @return el {@link Type} asociado a esta excepción
     */
    public Type getType() {
    return type;
  }
}
