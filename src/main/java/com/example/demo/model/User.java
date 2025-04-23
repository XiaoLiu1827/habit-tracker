package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "account_user")
public class User {
    @Id @GeneratedValue
    private Long id;

    private String username;

    // 認証情報などを今後拡張
}