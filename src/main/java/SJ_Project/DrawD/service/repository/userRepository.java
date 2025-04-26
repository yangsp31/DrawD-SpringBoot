package SJ_Project.DrawD.service.repository;

import SJ_Project.DrawD.Entity.userEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class userRepository {
    private final userJpaRepository userJpaRepository;

    public userRepository(userJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Transactional(readOnly = true)
    public boolean existUser(String email) {
        return userJpaRepository.existsById(email);
    }

    @Transactional
    public void saveUser(userEntity user) {
        userJpaRepository.save(user);
    }

    @Transactional
    public void updateRefreshToken(String email, String refreshJwt) {
        userJpaRepository.updateRefreshTokenByEmail(email, refreshJwt);
    }

    @Transactional(readOnly = true)
    public boolean verifyRefreshToken(String email, String refreshToken) {
        return userJpaRepository.existsByEmailAndRefreshTokenEquals(email, refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(String email) {
        userJpaRepository.deleteRefreshTokenByEmail(email);
    }

    @Transactional
    public void updateProfile(String email, String profile) {
        if(!userJpaRepository.existsByEmailAndProfileEquals(email, profile)) {
            userJpaRepository.updateProfileByEmail(email, profile);
        }
    }

    @Transactional
    public String getProfile(String email) {
        return userJpaRepository.findProfileByEmail(email).getProfile();
    }

    @Transactional
    public userEntity getUser(String email) {
        return userJpaRepository.findById(email).orElseThrow(() -> new RuntimeException("Not Found Email"));
    }
}
