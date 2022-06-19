package com.osc.blog.business.abstracts;

import com.osc.blog.core.utilities.results.DataResult;
import com.osc.blog.core.utilities.results.Result;
import com.osc.blog.entities.concretes.ConfirmationToken;

public interface ConfirmationTokenService {

    Result save(ConfirmationToken confirmationToken);
    DataResult<ConfirmationToken> getByToken(String token);
    Result confirmToken(String token);

}
