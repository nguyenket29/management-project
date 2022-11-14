package com.hau.ketnguyen.it.service.impl;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.entity.auth.ResetPasswordToken;
import com.hau.ketnguyen.it.repository.hau.ResetPasswordTokenReps;
import com.hau.ketnguyen.it.service.ResetPasswordTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ResetTokenPasswordServiceImpl implements ResetPasswordTokenService {
    private final ResetPasswordTokenReps resetPasswordTokenReps;

    @Override
    public boolean createTokenResetPassword(Integer userId, String token) {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setUserId(userId);
        resetPasswordToken.setToken(token);
        resetPasswordToken.setExpiryDate(calculateExpireDate(12 * 60));
        ResetPasswordToken save = resetPasswordTokenReps.save(resetPasswordToken);
        return save.getId() > 0;
    }

    @Override
    public String validatePasswordResetToken(String token) {
        ResetPasswordToken passToken = resetPasswordTokenReps.findByToken(token).orElseThrow(() ->
                APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy token password"));

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    @Override
    public Optional<ResetPasswordToken> findByToken(String token) {
        return resetPasswordTokenReps.findByToken(token);
    }

    private boolean isTokenFound(ResetPasswordToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(ResetPasswordToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    private Date calculateExpireDate(int expireTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expireTime);
        return calendar.getTime();
    }
}
