package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StandardProceduresTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StandardProcedures getStandardProceduresSample1() {
        return new StandardProcedures().id(1L).userUUID("userUUID1");
    }

    public static StandardProcedures getStandardProceduresSample2() {
        return new StandardProcedures().id(2L).userUUID("userUUID2");
    }

    public static StandardProcedures getStandardProceduresRandomSampleGenerator() {
        return new StandardProcedures().id(longCount.incrementAndGet()).userUUID(UUID.randomUUID().toString());
    }
}
