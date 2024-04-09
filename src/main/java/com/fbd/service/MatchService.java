package com.fbd.service;

import com.fbd.model.FilterOption;
import com.fbd.model.Match;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MatchService {
    Optional<FilterOption> getFilter(String userId);
    int getCountLiked(String userId);
}