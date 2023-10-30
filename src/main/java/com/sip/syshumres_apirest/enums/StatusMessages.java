package com.sip.syshumres_apirest.enums;

public enum StatusMessages {

    SUCCESS("Operación realizada con éxito"),
    NOT_FOUND("Elemento no encontrado"),
    DUPLICATE("El elemento ya existe"),
    MESSAGE_KEY("message"),
    URLFILE_KEY("urlFile"),
    SUCCESS_CREATE("Registro creado con éxito"),
    SUCCESS_UPDATE("Registro actualizado con éxito"),
	ERROR_CREATE("No se pudo crear el registro, valide la información"),
	ERROR_UPDATE("No se pudo actualizar el registro, valide la información"),
	SUCCESS_ADD("Los registros se agregaron al padre con éxito"),
	ERROR_ADD("No se agregaron los registros al padre, valide la información"),
	SUCCESS_REMOVE("El registro se elimino del padre con éxito"),
	ERROR_REMOVE("No se pudo eliminar el registro del padre, valide la información"),
	SUCCESS_UPLOAD("El archivo se subió con éxito"),
	ERROR_UPLOAD("No se pudo subir el archivo, valide con el admin"),
	SUCCESS_LOGOUT("Logout exitoso"),
	SUCCESS_CHANGE_PASSWORD("La contraseña fue actualizada con éxito");

    private final String message;

    StatusMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}