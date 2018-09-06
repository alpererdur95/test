package com.example.demo.repository;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User,Integer> {
    @Query(value = "SELECT * FROM chimneyuser WHERE is_deleted=0 and CONCAT(firstname,' ',lastname,' ',email) COLLATE UTF8_GENERAL_CI LIKE CONCAT('%',:search,'%') ORDER BY updated desc LIMIT :startAt,:rowCount ", nativeQuery = true)
    List<User> findSearchUser(@Param("search") String searchKey,@Param("startAt") Integer startAt,@Param("rowCount") Integer rowCount);

    @Query(value = "SELECT COUNT(*) FROM chimneyuser WHERE is_deleted=0 and CONCAT(firstname,' ',lastname,' ',email) COLLATE UTF8_GENERAL_CI LIKE CONCAT('%',:search,'%')", nativeQuery = true)
   Double getCount(@Param("search") String searchKey);

    @Modifying
    @Query(value = "UPDATE chimneyuser SET password=:pass  WHERE uid=:id",nativeQuery = true)
    @Transactional
    void updatePassword(@Param("pass") String password,@Param("id")Integer id);

}