package com.example.helpify.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;

    private Boolean verified = false;

    private String otp; // for email verification

    private Double latitude;
    private Double longitude;


    public boolean isVerified() {
        if(verified == null) {
            return false;
        }
        return verified;
    }
}
