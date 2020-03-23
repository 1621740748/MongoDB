package com.example.mongodb;

import com.example.mongodb.model.Account;
import com.example.mongodb.model.Organization;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class MongoTemplateTest {
    @Autowired
    private MongoTemplate mongoTemplate;
    Account account;
    Organization organization;
    @Test
    public void insert(){
        String hobby[] = {"篮球","乒乓球","羽毛球"};
        account = new Account();
        organization = new Organization();
        account.setAge(23);
        account.setPassword("1234");
        account.setSex("男");
        account.setTel("13609893206");
        account.setUsername("熊峰");
        organization.setDirector("xf");
        organization.setId("43");
        organization.setName("asd");
        account.setHobby(hobby);
        account.setOrganization(organization);
        mongoTemplate.save(organization);
        mongoTemplate.save(account);
    }

    @Test
    public void batchInsert(){
        List<Account> accountList = new ArrayList<Account>();
        String hobby[] = {"篮球","乒乓球","羽毛球"};
        for(int i=0;i<10;i++){
            Account account = new Account();
            account.setSex("男");
            account.setTel("1360989320"+i);
            account.setUsername("xf"+i);
            account.setPassword("qjkj"+i);
            account.setAge(i+1);
            Organization organization = new Organization();
            organization.setId(""+i);
            organization.setName("QJKJ"+i);
            organization.setDirector("m"+i);
            account.setOrganization(organization);
            account.setHobby(hobby);
            accountList.add(account);
        }
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,"account");
        bulkOperations.insert(accountList).execute();
    }

    @Test
    public void findAll(){
        Query query = new Query();
        List<Account> accountList = mongoTemplate.find(query,Account.class);
        System.out.println(accountList);
    }

    @Test
    public void findByUsername(){
        Query query = new Query(Criteria.where("_id").is("熊峰"));
        Account account = mongoTemplate.findOne(query,Account.class);
        System.out.println("**********org:"+account.getOrganization().getName());
        Assert.assertEquals("熊峰",account.getUsername());
    }

    @Test
    public void deleteByUsername(){
        Query query = new Query(Criteria.where("_id").is("熊峰"));
        DeleteResult deleteResult = mongoTemplate.remove(query,Account.class);
        Assert.assertTrue(deleteResult.wasAcknowledged());
    }

    @Test
    public void update(){
        Query query = new Query(Criteria.where("_id").is("xf0"));
        Update update = new Update();
        update.set("organization.name","biubiubiu");
        UpdateResult updateResult = mongoTemplate.updateFirst(query,update,Account.class);
        Assert.assertTrue(updateResult.wasAcknowledged());
    }

    @Test
    public void distinct(){
        DistinctIterable<String> distinctIterable = mongoTemplate.getDb().getCollection("account").distinct("sex",String.class);
        MongoCursor<String> iterator = distinctIterable.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

    @Test
    public void count(){
        Query query = new Query();
        Long count = mongoTemplate.count(query,"account");
        System.out.println(count);
    }

    @Test
    public void avg(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("sex").avg("age").as("平均数"));
        AggregationResults<String> aggregationResults = mongoTemplate.aggregate(aggregation,"account",String.class);
        List<String>list = aggregationResults.getMappedResults();
        System.out.println(list.size());
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }

    @Test
    public void max(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("sex").max("age").as("最大年龄"));
        AggregationResults<String> aggregationResults = mongoTemplate.aggregate(aggregation,"account",String.class);
        List<String> list = aggregationResults.getMappedResults();
        System.out.println(list);
    }

    @Test
    public void having(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("hobby"),Aggregation.match(Criteria.where("_id").ne("熊峰")),Aggregation.group("hobby").count().as("人数"));
        AggregationResults<String> aggregationResults = mongoTemplate.aggregate(aggregation,"account",String.class);
        List<String>list = aggregationResults.getMappedResults();
        for(String s:list)
        System.out.println(s);
    }

    @Test
    public void pagerList(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.project("sex","password","age"),Aggregation.skip(1L),Aggregation.limit(3L),Aggregation.sort(Sort.Direction.DESC,"age"));
        AggregationResults<Account> accountAggregationResults = mongoTemplate.aggregate(aggregation,"account",Account.class);
        List<Account> list = accountAggregationResults.getMappedResults();
        System.out.println(list);
    }

    @Test
    public void sum(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("sex").sum("age").as("sum(age)"));
        AggregationResults<String> accountAggregationResults = mongoTemplate.aggregate(aggregation,"account",String.class);
        List<String>list = accountAggregationResults.getMappedResults();
        for(String s:list){
            System.out.println(s);
        }
    }

    @Test
    public void groupby(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("sex").count().as("人数"));
        AggregationResults<String> stringAggregationResults = mongoTemplate.aggregate(aggregation,"account",String.class);
        List<String>list  = stringAggregationResults.getMappedResults();
        for(String s:list){
            System.out.println(s);
        }
    }

    @Test
    public void createIndex(){
        Index index = new Index("age",Sort.Direction.DESC);
        IndexOperations indexOperations = mongoTemplate.indexOps("account");
        indexOperations.ensureIndex(index);
    }

    @Test
    public void patternregex(){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("_id").regex("^熊")),Aggregation.project("_id","sex","age"));
        AggregationResults<Account> aggregationResults = mongoTemplate.aggregate(aggregation,"account",Account.class);
        List<Account>list = aggregationResults.getMappedResults();
        for (Account account:list){
            System.out.println(account);
        }
        //STARTING_WITH:  ^%s   ENDING_WITH:   %s$  CONTAINING: .*%s.  EXACT:  ^%s$  default: prepareAndEscapeStringBeforeApplyingLikeRegex
        //MongoRegexCreator类 源码
    }
}