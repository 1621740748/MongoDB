package com.example.mongodb.service;

import java.util.List;

import com.example.mongodb.model.Account;

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
