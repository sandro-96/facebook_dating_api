package com.fbd.service;

import com.fbd.model.FilterOption;
import java.util.Optional;

public interface MatchService {
    Optional<FilterOption> getFilter(String userId);
    int matchedCount(String userId);
}