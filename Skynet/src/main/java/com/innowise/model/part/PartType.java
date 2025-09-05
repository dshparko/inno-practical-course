package com.innowise.model.part;

import java.util.concurrent.ThreadLocalRandom;

public enum PartType {
    HEAD, TORSO, HAND, FEET;

    private static final PartType[] VALUES = values();

    public static PartType getRandomPart() {
        return VALUES[ThreadLocalRandom.current().nextInt(VALUES.length)];
    }
}
