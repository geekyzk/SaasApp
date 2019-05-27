package com.em248.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table
public class Book {
    @Id
    private String id = UUID.randomUUID().toString();

    private String name;

    private String author;

    private String publishDate;
}
