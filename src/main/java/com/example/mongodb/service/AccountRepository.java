package com.example.mongodb.service;

import com.example.mongodb.model.Account;
import org.springframework.data.mongodb.core.BulkOperations;

import java.util.List;

/**
 * 账户接口
 */
public interface AccountRepository {
    void batchInsert(List<Account> accountList);
    void insert(Account account);
    void deleteByUsername(String username);
    void createIndex();
    List<Account> findAll();
    Account findByUsername(String username);
    List<Account> aggregationByUsername(String username);
}
