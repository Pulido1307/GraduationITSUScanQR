package com.example.graduationitsuscanqr.helpers.models;

public class Alumno {

    private String numeroControl;
    private String numeroSilla;
    private String nombre;
    private String carrera;
    private String grupo;
    private int status;

    public Alumno(String numeroControl,String numeroSilla, String nombre, String carrera, String grupo, int status) {
        this.numeroControl = numeroControl;
        this.numeroSilla=numeroSilla;
        this.nombre = nombre;
        this.carrera = carrera;
        this.grupo = grupo;
        this.status = status;
    }

    public String getNumeroSilla() {
        return numeroSilla;
    }

    public void setNumeroSilla(String numeroSilla) {
        this.numeroSilla = numeroSilla;
    }

    public String getNumeroControl() {
        return numeroControl;
    }

    public void setNumeroControl(String numeroControl) {
        this.numeroControl = numeroControl;
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
}
