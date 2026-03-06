package com.example.sea_island_lottery.entity;

import jakarta.persistence.*;
//persistence.* => @Entity, @Id, @GeneratedValue, @ManyToOne など JPAアノテーションを使う場合は必須

import lombok.AllArgsConstructor;
//AllArgsConstructor => 必須ではない すべてのフィールドを引数に持つコンストラクタを自動生成

import lombok.Data;
//Data => JPAエンティティでは基本的に必要

import lombok.NoArgsConstructor;
//NoArgsConstructor => JPAエンティティでは基本的に必要 JPAは引数なしコンストラクタが必須

//上記４つはエンティティでよく使う
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
//各Timestamp => 必須ではない @CreationTimestampを付けると作成日時を自動でDBに入れてくれる

//JPA => JPA（Java Persistence API）は、JavaのオブジェクトをDBテーブルに対応させて操作する仕組み
//       エンティティを作ってリポジトリ経由でCRUDやリレーション管理が可能
//JPA不使用 => 手動でSQLを書く必要がある

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    //ID
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id; // auth.users.id と同じ

    //名前：入力必須
    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

//==============アノテーションを使わずに書くと==============

//package com.example.sea_island_lottery.entity;
//
//import jakarta.persistence.*; // @PrePersist, @PreUpdate をインポート
//        import java.time.LocalDateTime;
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
//    // -------------------------------
//    // コンストラクタ(オブジェクト化(new)する準備)
//    //※DI（Dependency Injection）はオブジェクト化 + 参照の注入をSpringが自動実施する仕組みで
//    //指示はController,Serviceで書く、Repository,Entityでは書かない
//    public User() {}
//
//    public User(UUID id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
//        this.id = id;
//        this.name = name;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
//
//    // -------------------------------
//    // getter / setter
//    public UUID getId() {
//      return id;  //getは取得だけなのでreturnでよい
//    }
//    public void setId(UUID id) {
//      this.id = id;  //setは更新なので引数が必要 + returnは不要 + this使う
//    }
//    public String getName() {
//      return name;
//    }
//    public void setName(String name) {
//      this.name = name;
//    }
//    public LocalDateTime getCreatedAt() {
//      return createdAt;
//    }
//    public void setCreatedAt(LocalDateTime createdAt) {
//      this.createdAt = createdAt;
//    }
//    public LocalDateTime getUpdatedAt() {
//      return updatedAt;
//    }
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//      this.updatedAt = updatedAt;
//    }
//
//    // -------------------------------
//    // タイムスタンプを自動設定するためのコールバックメソッド
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
//}
