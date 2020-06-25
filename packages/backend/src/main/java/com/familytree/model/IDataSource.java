package com.familytree.model;

import java.io.IOException;
import java.util.List;
import com.familytree.model.Person;

public interface IDataSource {

    void setUserId(String userId);

    List<Person> getPersonsByHobby(String hobby);

    List<Person> getPersonsByRelationship(String relationship);

    List<Person> getPersonsByAge(int age);

    List<Person> getPersonsByName(String name);

    List<Person> getPersonByUserId(String userId);

    Person getPersonById(String userId, String personId);

    void save(Person person) throws IOException;

    void delete(Person person);
}
