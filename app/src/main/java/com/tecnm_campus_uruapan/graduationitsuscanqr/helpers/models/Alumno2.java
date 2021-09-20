package com.tecnm_campus_uruapan.graduationitsuscanqr.helpers.models;

public class Alumno2 {
    private String nombre;
    private String carrera;
    private String grupo;
    private int status;
    private String correo;
    private String asiento;

    public Alumno2() {}

    public Alumno2(String nombre, String carrera, String grupo, int status, String correo, String asiento) {
        this.nombre = nombre;
        this.carrera = carrera;
        this.grupo = grupo;
        this.status = status;
        this.correo = correo;
        this.asiento = asiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }
}
