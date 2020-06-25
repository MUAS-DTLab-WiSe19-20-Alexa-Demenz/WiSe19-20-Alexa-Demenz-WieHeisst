package com.familytree.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.familytree.alexa.PhrasesAndConstants;
import com.familytree.model.Person;

public class DummyDataSource implements IDataSource {
    private List<Person> plist;

    public DummyDataSource() {
        List<Person> list = new ArrayList<Person>();

        for (int i = 0; i < 10; ++i) {
            Person pa = new Person();
            pa.setUserId("user_id");
            pa.setPersonId("" + i);
            pa.setBirthDate("1969-02-02");
            if (i % 2 == 0) {
                pa.setFirstName("Hans" + i);
                pa.setRelationToUser("BROTHER");
            } else {
                pa.setFirstName("Rosi" + i);
                pa.setRelationToUser("SISTER");
            }
            // pa.setHobbies(hobbies));
            // pa.setLastName(lastName);
            // pa.setPhoneNumber(phoneNumber);
            //
            // pa.setWork(work);
            list.add(pa);
        }
        Person pa = new Person();
        pa.setUserId("user_id");
        pa.setBirthDate("1969-02-02");
        pa.setLastName("Musterfrau");
        pa.setPersonId("" + 11);
        pa.setFirstName("RosiM");
        pa.setWork("Architektin");
        pa.setRelationToUser("MOTHER");
        list.add(pa);

        Person pa2 = new Person();
        pa2.setUserId("user_id");
        pa2.setBirthDate("1979-12-05");
        pa2.setLastName("August");
        pa2.setPhoneNumber("93920199");
        pa2.setPersonId("" + 12);
        pa2.setFirstName("Johann");
        pa2.setWork("Bierbrauer");
        pa2.setRelationToUser("M_COUSIN");
        pa2.setHobbies(new String[] { "schwimmen" });
        list.add(pa2);

        Person pa3 = new Person();
        pa3.setUserId("user_id");
        pa3.setBirthDate("1988-04-20");
        pa3.setLastName("Ackermann");
        pa3.setPersonId("" + 13);
        pa3.setFirstName("Melinda");
        pa3.setWork("Magierin");
        pa3.setRelationToUser("F_COUSIN");
        list.add(pa3);
        
       // System.out.println(pa3.getFirstName() +" "+ pa3.getLastName() + " "+pa3.getRelationToUser());
        
        Person pa4 = new Person();
        pa4.setUserId("user_id");
        pa4.setBirthDate("1988-04-21");
        pa4.setLastName("Duck");
        pa4.setPersonId("" + 14);
        pa4.setFirstName("Dagobert");
        pa4.setWork("Ente");
        pa4.setRelationToUser("UNCLE");
        list.add(pa4);

//        for (Person p: list) {
//        	System.out.println(p.getFirstName() +" "+ p.getLastName() + " "+p.getRelationToUser());
//        }
        plist = list;
    }

    @Override
    public List<Person> getPersonsByHobby(String hobby) {
        List<Person> list = new ArrayList<>();
        for (Person p : plist) {
            List<String> hobbies = Arrays.asList(p.getHobbies());
            for (String h : hobbies) {
                if (h.equalsIgnoreCase(hobby)) {
                    list.add(p);
                }
            }
        }
        return list;
    }

    @Override
    public List<Person> getPersonsByRelationship(String relationship) {
        List<Person> list = new ArrayList<Person>();
        for (PhrasesAndConstants.RELATIONS r : PhrasesAndConstants.RELATIONS.values()) {
            if (r.getRelation().equalsIgnoreCase(relationship)) {
                for (Person p : plist) {
                	//System.out.println(p.getRelationToUser() + "|" + r.toString());
                	
                	if (!(p.getRelationToUser() == null)) {
                		if (p.getRelationToUser().equalsIgnoreCase(r.toString())) {
                			list.add(p);
                		}
                	}
                }
            }
        }

        return list;
    }

    @Override
    public List<Person> getPersonsByAge(int age) {
        List<Person> list = new ArrayList<>();
        for (Person p : plist) {
            if (Integer.parseInt(p.calculateAge()) == age)
                list.add(p);
        }
        return list;
    }

    @Override
    public List<Person> getPersonsByName(String name) {
        List<Person> list = new ArrayList<Person>();
        for (Person p : plist) {
            if (p.getFirstName().equals(name)) {
                list.add(p);
            }
        }
        return list;
    }

    @Override
    public void setUserId(String userId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void save(Person person) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public Person getPersonById(String userId, String personId) {
        for (Person p : plist) {
            if (p.getUserId().equals(userId) && p.getPersonId().equals(personId))
                return p;
        }
        return null;
    }

    @Override
    public void delete(Person person) {
        plist.remove(person);
    }

    @Override
    public List<Person> getPersonByUserId(String userId) {
        // TODO Auto-generated method stub
        return null;
    }

}
