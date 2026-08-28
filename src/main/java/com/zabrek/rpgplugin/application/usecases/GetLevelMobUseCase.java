package com.zabrek.rpgplugin.application.usecases;

import java.util.concurrent.ThreadLocalRandom;

public class GetLevelMobUseCase {

    public int execute(double x, double z) {
        double distance = Math.max(Math.abs(x), Math.abs(z));
        int zoneLevel = (int) Math.round(distance / 500.0) + 1;

        double variation = ThreadLocalRandom.current().nextGaussian() * 1.5;

        int level = (int) Math.round(zoneLevel + variation);
        return Math.clamp(level, 1, 200);
    }
}
