package com.sam.minicinemaapi.repostiory;

import com.sam.minicinemaapi.entity.PasswordResetToken;
import com.sam.minicinemaapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);

    List<PasswordResetToken> findAllByUser(User user);
}
