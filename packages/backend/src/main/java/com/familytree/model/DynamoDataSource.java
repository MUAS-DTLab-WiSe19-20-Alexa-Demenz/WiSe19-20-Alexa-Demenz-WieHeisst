package com.familytree.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.familytree.alexa.PhrasesAndConstants;

public class DynamoDataSource implements IDataSource {
    private String userId;
    private PersonMapper m;

    public DynamoDataSource() {
        m = new PersonMapper();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public List<Person> getPersonsByHobby(String hobby) {
        return m.getPersonsByAttribute(userId, "hobby", hobby);
    }

    @Override
    public List<Person> getPersonsByRelationship(String relationship) {
        for (PhrasesAndConstants.RELATIONS r : PhrasesAndConstants.RELATIONS.values()) {
            if (r.getRelation().equalsIgnoreCase(relationship)) {
                return m.getPersonsByAttribute(userId, "relationToUser", r.toString());
            }
        }
        return new ArrayList<Person>();
    }

    @Override
    public List<Person> getPersonsByAge(int age) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Person> getPersonsByName(String name) {
        return m.getPersonsByAttribute(userId, "firstName", name);
    }

    @Override
    public void save(Person person) throws IOException {
        m.save(person);
    }

    @Override
    public Person getPersonById(String userId, String personId) {
        return m.getPersonById(userId, personId);
    }

    @Override
    public void delete(Person person) {
        m.delete(person);
    }

    @Override
    public List<Person> getPersonByUserId(String userId) {
        return m.getPersonsByUserId(userId);
    }

}