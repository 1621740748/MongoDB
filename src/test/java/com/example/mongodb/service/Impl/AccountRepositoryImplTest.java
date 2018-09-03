package com.example.mongodb.service.Impl;

import com.example.mongodb.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
public class AccountRepositoryImplTest {

    @InjectMocks
    private AccountRepositoryImpl accountRepositoryImpl;
    @Mock
    private MongoTemplate mongoTemplate;
    @Before
    public void setup(){
        initMocks(this);
    }

    @Test
    public void findAll() {
        when(mongoTemplate.findAll(any())).thenReturn(java.util.Arrays.asList(new Account(),new Account(),new Account()));
        List<Account> list = accountRepositoryImpl.findAll();
        Assert.assertEquals(3,list.size());
    }

    @Test
    public void findByUsername() {
        when(mongoTemplate.findOne(any(),any(),anyString())).thenReturn(new Account());
        Account account = accountRepositoryImpl.findByUsername("xxx");
        Assert.assertNotNull(account);
    }
}