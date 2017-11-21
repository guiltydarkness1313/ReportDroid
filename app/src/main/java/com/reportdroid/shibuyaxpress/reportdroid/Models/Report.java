package com.reportdroid.shibuyaxpress.reportdroid.Models;

/**
 * Created by paulf on 13-Nov-17.
 */

public class Report {
    private int id;
    private String title;
    private String descripcion;
    private String address;
    private Double latitud;
    private Double longitud;
    private String image;
    private Integer users_id;

    public Report() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer  getUser_id() {
        return users_id;
    }

    public void setUser_id(Integer user_id) {
        this.users_id = user_id;
    }
}
