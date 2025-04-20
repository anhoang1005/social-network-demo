package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.Notifications;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {


    @Query(value = "SELECT n from Notifications n " +
            "WHERE n.user.id = :id ")
    Page<Notifications> getNotificationOfUser(@Param("id") Long userId,
                                              Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Notifications n SET n.isRead = true " +
            "WHERE n.isRead = false AND n.user.id = :userId")
    void userTickReadAllNotification(@Param("userId") Long userId);

//    Long countOfUnreadNotification(Boolean isRead, Long userId);


}
