package com.osc.blog.business.concretes;

import com.osc.blog.business.abstracts.ConfirmationTokenService;
import com.osc.blog.core.utilities.results.*;
import com.osc.blog.dal.abstracts.ConfirmationTokenDao;
import com.osc.blog.entities.concretes.ConfirmationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenManager implements ConfirmationTokenService {

    private final ConfirmationTokenDao confirmationTokenDao;

    @Override
    public Result save(ConfirmationToken confirmationToken) {
        confirmationTokenDao.save(confirmationToken);
        return new SuccessResult("Confirmation Token saved.");
    }

    @Override
    public DataResult<ConfirmationToken> getByToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenDao.findByToken(token);
        if(confirmationToken == null) {
            return new ErrorDataResult<>(null, "Confirmation Token not found!");
        }
        return new SuccessDataResult<>(confirmationToken);
    }

    @Override
    public Result confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenDao.findByToken(token);
        if(confirmationToken == null) {
            return new ErrorDataResult<>(null, "Confirmation Token not found!");
        }
        confirmationTokenDao.setConfirmedDate(token);
        return new SuccessResult("Confirmation Token confirmed.");
    }

}
