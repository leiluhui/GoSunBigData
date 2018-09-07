package com.hzgc.service.people.model;

public class Car {
    private Long id;

    private Long carid;

    private String car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCarid() {
        return carid;
    }

    public void setCarid(Long carid) {
        this.carid = carid;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car == null ? null : car.trim();
    }
}