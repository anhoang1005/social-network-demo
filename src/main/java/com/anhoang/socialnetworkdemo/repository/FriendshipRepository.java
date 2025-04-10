package com.anhoang.socialnetworkdemo.repository;

import com.anhoang.socialnetworkdemo.entity.Friendship;
import com.anhoang.socialnetworkdemo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query(value = "SELECT f.friend FROM Friendship f " +
            "where f.user.id = :userId " +
            "AND f.status = :status")
    Page<Users> getFriendRequestSendByMe(@Param("userId") Long userId,
                                              Pageable pageable);

    @Query(value = "SELECT f FROM Friendship f " +
            "where f.friend.id = :userId " +
            "AND f.status = :status")
    Page<Friendship> getFriendRequestInviteMe(@Param("userId") Long userId,
                                              Pageable pageable);

    @Query(value = "SELECT COUNT(f) FROM Friendship f " +
            "WHERE f.user.id = :userId1 " +
            "AND f.friend.id = :userId2 " +
            "AND f.status = :status")
    Long checkExistedFriend(@Param("userId1") Long userId1,
                            @Param("userId2") Long userId2,
                            @Param("status") Friendship.FriendshipStatus status);

    Boolean existsByUserAndFriendAndStatus(Users users, Users friend, Friendship.FriendshipStatus status);

    Optional<Friendship> findByUserAndFriendAndStatus(Users user, Users friend, Friendship.FriendshipStatus status);

    //Lay danh sach ban be cua 1 user
    @Query("""
                SELECT f.friend FROM Friendship f 
                WHERE f.user.id = :userId AND f.status = 'ACCEPTED'
            """)
    Page<Users> findAllFriends(@Param("userId") Long userId,
                            Pageable pageable);

    @Query("""
                SELECT COUNT(f) FROM Friendship f 
                WHERE f.user.id = :userId AND f.status = 'ACCEPTED'
            """)
    Long countFriendOfUser(@Param("userId") Long userId);

    //Lay danh sach loi moi ket ban cua 1 user
    @Query("""
                SELECT f FROM Friendship f 
                WHERE f.user.id = :userId AND f.status = 'PENDING'
            """)
    Page<Friendship> findIncomingRequests(@Param("userId") Long userId,
                                          Pageable pageable);

    //Lay danh sahc user da chan
    @Query("""
                SELECT f FROM Friendship f 
                WHERE f.user.id = :userId AND f.status = 'BLOCKED'
            """)
    Page<Friendship> findWhoBlockedUser(@Param("userId") Long userId,
                                        Pageable pageable);

    //Lay danh sach ban chung cua 2 user
    @Query("""
                SELECT f1.friend FROM Friendship f1 
                JOIN Friendship f2 ON f1.friend.id = f2.friend.id 
                WHERE f1.user.id = :user1Id AND f2.user.id = :user2Id 
                AND f1.status = 'ACCEPTED' AND f2.status = 'ACCEPTED'
            """)
    Page<Users> findMutualFriendsByPage(@Param("user1Id") Long user1Id,
                                        @Param("user2Id") Long user2Id,
                                        Pageable pageable);

    //Dem so luong ban chung cua 2 user
    @Query("""
                SELECT COUNT(f1.friend) FROM Friendship f1 
                JOIN Friendship f2 ON f1.friend.id = f2.friend.id 
                WHERE f1.user.id = :user1Id AND f2.user.id = :user2Id
                AND f1.status = 'ACCEPTED' AND f2.status = 'ACCEPTED'
            """)
    Long countMutualFriends(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);


}
