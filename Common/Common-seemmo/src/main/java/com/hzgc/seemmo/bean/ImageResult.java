package com.hzgc.seemmo.bean;

import com.hzgc.seemmo.bean.carbean.Vehicle;
import com.hzgc.seemmo.bean.personbean.Person;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ImageResult implements Serializable {

    private List<Vehicle> vehicleList;

    private List<Person> personList;

}
