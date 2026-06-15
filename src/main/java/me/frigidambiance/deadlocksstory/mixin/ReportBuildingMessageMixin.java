package me.frigidambiance.deadlocksstory.mixin;

import com.mojang.logging.LogUtils;
import forge.net.mca.network.c2s.ReportBuildingMessage;
import forge.net.mca.server.world.data.Building;
import forge.net.mca.server.world.data.Village;
import forge.net.mca.server.world.data.VillageManager;
import me.frigidambiance.deadlocksstory.Config;
import me.frigidambiance.deadlocksstory.context.McaRoomRemovalContext;
import me.frigidambiance.deadlocksstory.stats.DeadlocksStoryStats;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ReportBuildingMessage.class, remap = false)
public abstract class ReportBuildingMessageMixin {
    @Unique
    private static final Logger deadlocksstory$LOGGER = LogUtils.getLogger();

    @Shadow
    @Final
    private ReportBuildingMessage.Action action;

    @Redirect(
        method = "receive",
        at = @At(
            value = "INVOKE",
            target = "Lforge/net/mca/server/world/data/VillageManager;processBuilding(Lnet/minecraft/core/BlockPos;ZZ)Lforge/net/mca/server/world/data/Building$validationResult;"
        ),
        require = 1
    )
    private Building.validationResult deadlocksstory$processBuildingAndAwardRoomStat(
        VillageManager villages,
        BlockPos pos,
        boolean enforce,
        boolean strictScan,
        ServerPlayer player
    ) {
        int roomsBefore = deadlocksstory$countMcaBuildings(villages);

        Building.validationResult result = villages.processBuilding(pos, enforce, strictScan);

        int roomsAfter = deadlocksstory$countMcaBuildings(villages);
        boolean roomCountIncreased = roomsAfter > roomsBefore;

        if (
            this.action == ReportBuildingMessage.Action.ADD_ROOM
                && result == Building.validationResult.SUCCESS
                && roomCountIncreased
        ) {
            DeadlocksStoryStats.awardMcaRoomCreated(player);

            if (Config.debugLogging()) {
                deadlocksstory$LOGGER.info(
                    "[DeadlocksStory] MCA room creation counted for {}; rooms before={}, rooms after={}",
                    player.getGameProfile().getName(),
                    roomsBefore,
                    roomsAfter
                );
            }
        } else if (Config.debugLogging()) {
            deadlocksstory$LOGGER.info(
                "[DeadlocksStory] MCA room creation was not counted for {}; action={}, result={}, rooms before={}, rooms after={}",
                player.getGameProfile().getName(),
                this.action,
                result,
                roomsBefore,
                roomsAfter
            );
        }

        return result;
    }

    @Unique
    private int deadlocksstory$countMcaBuildings(VillageManager villages) {
        int count = 0;

        for (Village village : villages) {
            count += village.getBuildings().size();
        }

        return count;
    }

    @Inject(
        method = "receive",
        at = @At("HEAD"),
        require = 1
    )
    private void deadlocksstory$startRoomRemovalContext(
        ServerPlayer player,
        CallbackInfo ci
    ) {
        if (this.action == ReportBuildingMessage.Action.REMOVE) {
            McaRoomRemovalContext.start(player);
        }
    }

    @Inject(
        method = "receive",
        at = @At("RETURN"),
        require = 1
    )
    private void deadlocksstory$clearRoomRemovalContext(
        ServerPlayer player,
        CallbackInfo ci
    ) {
        if (this.action == ReportBuildingMessage.Action.REMOVE) {
            McaRoomRemovalContext.clear();
        }
    }
}
