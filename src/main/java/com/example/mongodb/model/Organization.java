package com.example.mongodb.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Organization {
    @Id
    private String id;
    private String name;
    private String director;
}
