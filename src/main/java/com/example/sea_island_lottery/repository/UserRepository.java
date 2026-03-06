package com.example.sea_island_lottery.repository;

import com.example.sea_island_lottery.entity.User;
//entity.User => <User, UUID> でUserエンティティを使えるようにする
import org.springframework.data.jpa.repository.JpaRepository;
//JpaRepository => JPAのJpaRepository(CRUDを自動生成機能)インターフェースを使えるようにする
import org.springframework.stereotype.Repository;
//Repository => このファイルをSpringにリポジトリ層として認識させる

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Spring Data JPAがメソッド名から自動的にクエリ生成
    // 例: `name` でユーザーを検索するメソッド
    Optional<User> findByName(String name);
    //Optional<T> => Typeにはモデルが入る、モデルにないカラムはエラーになる
    //               Optionalは返り値が有り無しか不明パターンで使う
    //               JPAリポジトリで返却型にすると、1件検索の結果が Optional にラップされる
}
