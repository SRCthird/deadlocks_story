package me.frigidambiance.deadlocksstory.mixin;

import com.mojang.logging.LogUtils;
import io.ejekta.bountiful.content.board.BoardBlockEntity;
import me.frigidambiance.deadlocksstory.Config;
import me.frigidambiance.deadlocksstory.stats.DeadlocksStoryStats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BoardBlockEntity.class, remap = false)
public abstract class BoardBlockEntityMixin {
    @Unique
    private static final Logger deadlocksstory$LOGGER = LogUtils.getLogger();

    @Inject(
            method = "updateCompletedBounties",
            at = @At("RETURN"),
            require = 1
    )
    private void deadlocksstory$awardBountyCompletedStat(
            Player player,
            CallbackInfo ci
    ) {
        if (player instanceof ServerPlayer serverPlayer) {
            DeadlocksStoryStats.awardBountyCompleted(serverPlayer);

            if (Config.debugLogging()) {
                deadlocksstory$LOGGER.info(
                        "[DeadlocksStory] Bountiful bounty completion counted for {}",
                        serverPlayer.getGameProfile().getName()
                );
            }
        }
    }
}
