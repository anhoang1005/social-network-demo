package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUsersByEmail(String email);
//    Optional<Users> findUsersById(Long id);
    Optional<Users> findUsersByUserCode(String userCode);

    @Query("SELECT u FROM Users u WHERE u.id <> :id " +
            "AND u.status = :status")
    Page<Users> getAllByIdAndStatus(@Param("id") Long id,
                                    @Param("status") Users.Status status,
                                    Pageable pageable);
    @Query("SELECT u FROM Users u " +
            "WHERE u.fullName LIKE %:fullName% " +
            "AND u.status = :status")
    Page<Users> findUsersByFullNameLikeAndStatus(@Param("fullName") String fullName,
                                                 @Param("status") Users.Status status,
                                                 Pageable pageable);
}
