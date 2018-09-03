package com.example.mongodb.service.Impl;

import com.example.mongodb.model.Account;
import com.example.mongodb.service.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户接口实现类
 */
@Service
public class AccountRepositoryImpl implements AccountRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void batchInsert(List<Account> accountList) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED,Account.class);
        bulkOperations.insert(accountList);
        bulkOperations.execute();
    }

    @Override
    public void insert(Account account) {
        mongoTemplate.insert(account,"account");
    }

    @Override
    public void deleteByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        mongoTemplate.remove(query,"acount");
    }

    @Override
    public void createIndex() {
        Index index = new Index("age",Sort.Direction.DESC);
        IndexOperations indexOperations = mongoTemplate.indexOps("account");
        indexOperations.ensureIndex(index);
    }

    @Override
    public List<Account> findAll() {
        return mongoTemplate.findAll(Account.class);
    }

    @Override
    public Account findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query,Account.class,"account");
    }

    @Override
    public List<Account> aggregationByUsername(String username){
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("username").is(username)),Aggregation.project("username","sex","tel","organization.name"));
        AggregationResults<Account> accountAggregationResults = mongoTemplate.aggregate(aggregation,"account",Account.class);
        return accountAggregationResults.getMappedResults();
    }


}
