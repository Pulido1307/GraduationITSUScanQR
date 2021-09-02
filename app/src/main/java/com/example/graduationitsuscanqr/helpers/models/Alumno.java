package com.example.graduationitsuscanqr.helpers.models;

public class Alumno {

    private String numeroControl;
    private String numeroSilla;
    private String nombre;
    private String carrera;
    private String grupo;
    private int status;
    private String correo;

    public Alumno(){}

    public Alumno(String numeroControl,String numeroSilla, String nombre, String carrera, String grupo, int status) {
        this.numeroControl = numeroControl;
        this.numeroSilla=numeroSilla;
        this.nombre = nombre;
        this.carrera = carrera;
        this.grupo = grupo;
        this.status = status;
    }

    public Alumno(String numeroControl,String numeroSilla, String nombre, String carrera, String grupo, String correo) {
        this.numeroControl = numeroControl;
        this.numeroSilla=numeroSilla;
        this.nombre = nombre;
        this.carrera = carrera;
        this.grupo = grupo;
        this.status = 0;
        this.correo = correo;
    }

    public Alumno(String numeroControl,String numeroSilla, String nombre, String carrera, String grupo, int status, String correo) {
        this.numeroControl = numeroControl;
        this.numeroSilla=numeroSilla;
        this.nombre = nombre;
        this.carrera = carrera;
        this.grupo = grupo;
        this.status = status;
        this.correo = correo;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
