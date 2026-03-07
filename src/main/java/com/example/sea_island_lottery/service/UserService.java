package com.example.sea_island_lottery.service;

import com.example.sea_island_lottery.dto.UserDto;
import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    //自動でDI注入(new)するアノテーション
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //DTO使用パターンで作成が必要なメソッド＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
    // DTOを返すメソッド
    @Transactional(readOnly = true)
    public Optional<UserDto> findUserDtoById(UUID id) {
        return userRepository.findById(id).map(this::convertToDto);
    }

    // UserエンティティをUserDtoに変換するヘルパーメソッド
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }
    //＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

    //DBから1件ユーザーを取得するメソッド
    //DB操作がトランザクション内(処理成功時だけDBに反映される)で実行されるアノテーション
    @Transactional(readOnly = true)
    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    //ユーザー新規作成メソッド
    public User createUser(User user) {
        // ユーザー作成に関するビジネスロジック
        return userRepository.save(user);
    }

    //ユーザー更新メソッド
    public User updateUser(UUID id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(userDetails.getName());
        // 他に更新可能なプロフィール情報があれば書く

        return userRepository.save(user);
    }
}
