package com.volvo.congestiontax.model;

import java.util.List;

public record TollFee(Vehicle vehicle, int fee, List<String> dates) {
}
