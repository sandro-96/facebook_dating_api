package com.fbd.audit;

import com.fbd.utils.UserUtils;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return UserUtils.getCurrentUserLogin();
    }
}