package me.frigidambiance.deadlocksstory;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue DEBUG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("deadlocksstory");

        DEBUG = builder
                .comment("Enable debug logging for Deadlocks Story.")
                .define("debug", false);

        builder.pop();

        SPEC = builder.build();
    }

    private Config() {
    }

    public static boolean debugLogging() {
        return DEBUG.get();
    }
}
