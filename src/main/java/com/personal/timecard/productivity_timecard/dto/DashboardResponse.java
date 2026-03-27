package com.personal.timecard.productivity_timecard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class DashboardResponse {

    private boolean todaySubmitted;

    private Map<String, Integer> activeStreaks;

    private Map<String, Integer> longestStreaks;
}