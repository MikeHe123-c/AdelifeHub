package com.adlifehub.adlife.mapper;

import com.adlifehub.adlife.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from users where username=#{username}")
    User findByUsername(String username);

    @Select("select * from users where email=#{email}")
    User findByEmail(String email);

    @Insert("insert into users(username,email,password_hash,nickname,avatar_url,phone,roles_json) values(#{username},#{email},#{passwordHash},#{nickname},#{avatarUrl},#{phone},#{rolesJson})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User u);

    @Select("select * from users where id=#{id}")
    User findById(Long id);

    @Update("update users set nickname=#{nickname}, avatar_url=#{avatarUrl}, phone=#{phone} where id=#{id}")
    void updateProfile(User u);
}
