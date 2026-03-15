package com.example.sea_island_lottery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role; // e.g., "ROLE_USER", "ROLE_ADMIN"

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //==============アノテーションを使わずに書くと==============

//package com.example.sea_island_lottery.entity;
//
//import jakarta.persistence.*; // @PrePersist, @PreUpdate をインポート
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "users") // テーブル名を明示
//public class User {
//
//    @Id
//    @Column(columnDefinition = "UUID")
//    private UUID id;
//
//    @Column(nullable = false) // NOT NULL制約を明示
//    private String name;
//
//    @Column(name = "created_at", updatable = false) // カラム名を明示
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at") // カラム名を明示
//    private LocalDateTime updatedAt;
//
//    // コンストラクタ（オブジェクト化 new する準備）
//    // DI（Dependency Injection）はオブジェクト化 + 参照の注入を
//    // Springが自動実施する仕組み
//    // 指示は Controller / Service で書く
//    // Repository / Entity では書かない
//
//    public User() {}
//
//    public User(UUID id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
//        this.id = id;
//        this.name = name;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
//
//    // getter / setter
//    // CRUD(DB操作)とは関係なく、オブジェクト内に対して値を読み書きする
//
//    // 基本的には各カラム（フィールド）には getter と setter がセットで用意されることが多い
//    // DBに保存するフィールドは基本的に getter を持つ
//    // 書き換えが必要なフィールドだけ setter を持つ
//    // ID や自動生成カラムは setter を作らない（不変にする）
//
//    // setter → DBに保存したい値を入れるときに必要
//    // getter → オブジェクトの値を確認したいときに必要
//    // JPAではCRUDの際に内部でgetterが呼ばれることが多い
//
//    public UUID getId() {
//        return id; // getは取得だけなのでreturn
//    }
//
//    public void setId(UUID id) {
//        this.id = id; // setは更新なので引数が必要
//    }
//}

}
