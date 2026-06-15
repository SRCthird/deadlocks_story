package me.frigidambiance.deadlocksstory.events;

import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import me.frigidambiance.deadlocksstory.Config;
import me.frigidambiance.deadlocksstory.DeadlocksStory;
import me.frigidambiance.deadlocksstory.stats.DeadlocksStoryStats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(
    modid = DeadlocksStory.MOD_ID,
    bus = Mod.EventBusSubscriber.Bus.FORGE
)
public final class IronsSpellbooksEvents {
    public static final Logger LOGGER = LogUtils.getLogger();

    private IronsSpellbooksEvents() {
    }

    @SubscribeEvent
    public static void onSpellCast(SpellOnCastEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            DeadlocksStoryStats.awardSpellCast(serverPlayer, event.getSpellId());

            if (Config.debugLogging()) {
                LOGGER.info(
                        "[DeadlocksStory] Iron's spell cast counted for {}; spell={}; source={}; level={}",
                        serverPlayer.getGameProfile().getName(),
                        event.getSpellId(),
                        event.getCastSource(),
                        event.getSpellLevel()
                );
            }
        }
    }
}
