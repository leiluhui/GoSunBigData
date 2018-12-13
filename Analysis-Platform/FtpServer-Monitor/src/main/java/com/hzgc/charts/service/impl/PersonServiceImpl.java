package com.hzgc.charts.service.impl;

import com.hzgc.charts.dao.PersonRepository;
import com.hzgc.charts.domain.Person;
import com.hzgc.charts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<Person> findAll() {

        Iterable<Person> userIterable = personRepository.findAll();
        List<Person> list = new ArrayList<>();
        userIterable.forEach(single ->list.add(single));
        return list;
    }

    @Override
    public long findTotalNum() {
        return personRepository.count();
    }
}
