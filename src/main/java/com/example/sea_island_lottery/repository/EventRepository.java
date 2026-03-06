package com.example.sea_island_lottery.repository;

import com.example.sea_island_lottery.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // 例: `status` でイベントを検索するメソッド
    List<Event> findByStatus(String status);
    //List<T> => Typeにはモデルが入る、モデルにないカラムはエラーになる
    //           Listは複数件の結果を格納するためのコレクション
    //           JPAでは0件以上の結果をListに格納して返す

}
