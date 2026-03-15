package com.example.sea_island_lottery.repository;

import com.example.sea_island_lottery.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    //JpaRepository を継承（extends）してる為、基本的なデータベース操作（CRUD操作）メソッドが自動で全て定義される
    //findById(ID id)
    //findAll()
    //save(S entity)
    //deleteById(ID id)
    //よって、EventService内で eventRepository.findById(id); が使えてる仕組み

    // 例: `status` でイベントを検索するメソッド
    List<Event> findByStatus(String status);
    //List<T> => Typeにはモデルが入る、モデルにないカラムはエラーになる
    //           Listは複数件の結果を格納するためのコレクション
    //           JPAでは0件以上の結果をListに格納して返す

    Optional<Event> findById(Long eventId);
}
