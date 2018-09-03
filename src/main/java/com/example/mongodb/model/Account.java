package com.example.mongodb.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 实体类
 */
@Data
@Document
@ToString
public class Account {
    @Id
    private String username;
    private String password;
    private String sex;
    private String tel;
    @Indexed(unique = true,direction = IndexDirection.DESCENDING)
    private int age;
    private String []hobby;
    private Organization organization;
}
