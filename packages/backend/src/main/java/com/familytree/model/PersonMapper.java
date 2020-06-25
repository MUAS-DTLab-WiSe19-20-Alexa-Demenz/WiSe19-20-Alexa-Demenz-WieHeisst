package com.familytree.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class PersonMapper {
    private static final String PERSON_TABLE_NAME = System.getenv("personTableName");

    private Logger logger = Logger.getLogger(this.getClass());

    private DynamoDBAdapter db_adapter;
    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;

    public PersonMapper() {
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(PERSON_TABLE_NAME)).build();
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }

    public boolean ifTableExists() {
        return this.client.describeTable(PERSON_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }

    public void save(Person person) throws IOException {
        logger.info("FamilyTree - save(): " + person.toString());
        this.mapper.save(person);
    }

    public void delete(Person person) {
        logger.info("FamilyTree - delete():" + person.toString());
        this.mapper.delete(person);
    }

    public Person getPersonById(String userId, String personId) {
        return this.mapper.load(Person.class, personId, userId);
    }

    public List<Person> getPersonsByAttribute(String userId, String attribute, String attributeValue) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(userId));
        eav.put(":val2", new AttributeValue().withS(attributeValue));
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#w", "work");

        DynamoDBScanExpression scanExp = new DynamoDBScanExpression()
                .withProjectionExpression(
                        "userId, farbe, personId, firstName, lastName, phoneNumber, #w, birthDate, relationToUser")
                .withExpressionAttributeNames(nameMap)
                .withFilterExpression("userId = :val1 AND " + attribute + " = :val2")
                .withExpressionAttributeValues(eav);
        List<Person> results = this.mapper.scan(Person.class, scanExp);
        for (Person p : results) {
            logger.info("Person - list(): " + p.toString());
        }
        return results;
    }

    public List<Person> getPersonsByUserId(String userId) {
        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS(userId));
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#w", "work");

        DynamoDBScanExpression scanExp = new DynamoDBScanExpression()
                .withProjectionExpression(
                        "userId, farbe, personId, firstName, lastName, phoneNumber, #w, birthDate, relationToUser")
                .withExpressionAttributeNames(nameMap).withFilterExpression("userId = :val1")
                .withExpressionAttributeValues(eav);
        List<Person> results = this.mapper.scan(Person.class, scanExp);
        for (Person p : results) {
            logger.info("Person - list(): " + p.toString());
        }
        return results;
    }
}