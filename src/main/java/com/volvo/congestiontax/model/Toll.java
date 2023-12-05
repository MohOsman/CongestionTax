package com.volvo.congestiontax.model;

import java.util.List;

public record Toll(String city, List<TollEntry> tollEntryList) {
}
