package com.example.helpify.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document(collection = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;

    private String title;
    private String location;
    private int reward;

    private String status; // POSTED, ACCEPTED, DELIVERED
    private Double lat = 0.0;
    private Double lng=0.0;
    private String postedBy;
    private String acceptedBy;
    private String acceptedByName;
    private Date createdAt = new Date();
}
