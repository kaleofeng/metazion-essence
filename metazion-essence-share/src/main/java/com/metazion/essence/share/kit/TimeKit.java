package com.metazion.essence.share.kit;

import java.time.Instant;

public class TimeKit {

    public static long nowSeconds() {
        return Instant.now().getEpochSecond();
    }
}
