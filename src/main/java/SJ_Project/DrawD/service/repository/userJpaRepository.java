package SJ_Project.DrawD.service.repository;

import SJ_Project.DrawD.Entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface userJpaRepository extends JpaRepository<userEntity, String> {

    @Modifying
    @Transactional
    @Query("UPDATE userEntity u SET u.refreshToken = :refreshJwt WHERE u.email = :email")
    void updateRefreshTokenByEmail(@Param("email") String email, @Param("refreshJwt") String refreshJwt);

    boolean existsByEmailAndRefreshTokenEquals(String email, String refreshToken);

    @Modifying
    @Transactional
    @Query("UPDATE userEntity u SET u.refreshToken = NULL WHERE u.email = :email")
    void deleteRefreshTokenByEmail(@Param("email") String email);

    boolean existsByEmailAndProfileEquals(String email, String profile);

    @Modifying
    @Transactional
    @Query("UPDATE userEntity u SET u.profile = :profile WHERE u.email = :email AND u.profile <> :profile")
    void updateProfileByEmail(@Param("email") String email, @Param("profile") String profile);

    userProfile findProfileByEmail(String email);

}
