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
        award(player, ModStats.MCA_GIFTS_GIVEN.get(), "MCA gift");
    }

    public static void awardBountyCompleted(ServerPlayer player) {
        award(player, ModStats.BOUNTIES_COMPLETED.get(), "bounty completion");
    }

    public static void awardMcaRoomCreated(ServerPlayer player) {
        award(player, ModStats.MCA_ROOMS_CREATED.get(), "MCA room created");
    }

    public static void forfeitMcaRoomCreated(ServerPlayer player) {
        forfeit(player, ModStats.MCA_ROOMS_CREATED.get(), "MCA room removed");
    }

    public static void awardSpellCast(ServerPlayer player, String spellId) {
        award(player, ModStats.SPELLS_CAST.get(), "spell cast: " + spellId);
    }

    private static void award(ServerPlayer player, ResourceLocation statId, String debugName) {
        Stat<ResourceLocation> stat = Stats.CUSTOM.get(statId);

        player.awardStat(stat, 1);

        if (Config.debugLogging()) {
            int value = player.getStats().getValue(stat);
            LOGGER.info(
                "[DeadlocksStory] Awarded {} stat to {}; new value = {}",
                debugName,
                player.getGameProfile().getName(),
                value
            );
        }
    }

    private static void forfeit(ServerPlayer player, ResourceLocation statId, String debugName) {
        Stat<ResourceLocation> stat = Stats.CUSTOM.get(statId);

        int currentValue = player.getStats().getValue(stat);

        if (currentValue <= 0) {
            if (Config.debugLogging()) {
                LOGGER.info(
                        "[DeadlocksStory] Tried to remove {} stat from {}, but value was already 0",
                        debugName,
                        player.getGameProfile().getName()
                );
            }

            return;
        }

        player.awardStat(stat, -1);

        if (Config.debugLogging()) {
            int newValue = player.getStats().getValue(stat);
            LOGGER.info(
                    "[DeadlocksStory] Removed {} stat from {}; old value = {}, new value = {}",
                    debugName,
                    player.getGameProfile().getName(),
                    currentValue,
                    newValue
            );
        }
    }
}
