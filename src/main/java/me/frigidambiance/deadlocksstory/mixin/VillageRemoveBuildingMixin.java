package me.frigidambiance.deadlocksstory.mixin;

import com.mojang.logging.LogUtils;
import forge.net.mca.server.world.data.Village;
import me.frigidambiance.deadlocksstory.Config;
import me.frigidambiance.deadlocksstory.context.McaRoomRemovalContext;
import me.frigidambiance.deadlocksstory.stats.DeadlocksStoryStats;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = Village.class, remap = false)
public abstract class VillageRemoveBuildingMixin {
    @Unique
    private static final Logger deadlocksstory$LOGGER = LogUtils.getLogger();

    @Unique
    private boolean deadlocksstory$hadBuildingBeforeRemoval;

    @Shadow
    public abstract Map getBuildings();

    @Inject(
            method = "removeBuilding",
            at = @At("HEAD"),
            require = 1
    )
    private void deadlocksstory$captureBuildingBeforeRemoval(
            int id,
            CallbackInfo ci
    ) {
        ServerPlayer player = McaRoomRemovalContext.getPlayer();

        this.deadlocksstory$hadBuildingBeforeRemoval =
                player != null && this.getBuildings().containsKey(id);
    }

    @Inject(
            method = "removeBuilding",
            at = @At("RETURN"),
            require = 1
    )
    private void deadlocksstory$removeRoomStatAfterBuildingRemoval(
            int id,
            CallbackInfo ci
    ) {
        ServerPlayer player = McaRoomRemovalContext.getPlayer();

        if (
                player != null
                        && this.deadlocksstory$hadBuildingBeforeRemoval
                        && !this.getBuildings().containsKey(id)
        ) {
            DeadlocksStoryStats.forfeitMcaRoomCreated(player);

            if (Config.debugLogging()) {
                deadlocksstory$LOGGER.info(
                        "[DeadlocksStory] MCA room removal counted for {}; removed building id={}",
                        player.getGameProfile().getName(),
                        id
                );
            }
        } else if (Config.debugLogging() && player != null) {
            deadlocksstory$LOGGER.info(
                    "[DeadlocksStory] MCA room removal was not counted for {}; building id={}",
                    player.getGameProfile().getName(),
                    id
            );
        }
    }
}
