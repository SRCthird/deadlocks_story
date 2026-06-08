package me.frigidambiance.deadlocksstory.stats;

import com.mojang.logging.LogUtils;
import me.frigidambiance.deadlocksstory.Config;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import org.slf4j.Logger;

public final class DeadlocksStoryStats {
    private static final Logger LOGGER = LogUtils.getLogger();

    private DeadlocksStoryStats() {
    }

    public static void awardMcaGift(ServerPlayer player) {
        ResourceLocation statId = ModStats.MCA_GIFTS_GIVEN.get();
        Stat<ResourceLocation> stat = Stats.CUSTOM.get(statId);

        player.awardStat(stat, 1);

        if (Config.debugLogging()) {
            int value = player.getStats().getValue(stat);
            LOGGER.info(
                    "[DeadlocksStory] Awarded MCA gift stat to {}; new value = {}",
                    player.getGameProfile().getName(),
                    value
            );
        }
    }

    public static void awardBountyCompleted(ServerPlayer player) {
        ResourceLocation statId = ModStats.BOUNTIES_COMPLETED.get();
        Stat<ResourceLocation> stat = Stats.CUSTOM.get(statId);

        player.awardStat(stat, 1);

        if (Config.debugLogging()) {
            int value = player.getStats().getValue(stat);
            LOGGER.info(
                    "[DeadlocksStory] Awarded bounty completion stat to {}; new value = {}",
                    player.getGameProfile().getName(),
                    value
            );
        }
    }
}
