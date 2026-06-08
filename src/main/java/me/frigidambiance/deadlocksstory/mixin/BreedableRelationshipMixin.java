package me.frigidambiance.deadlocksstory.mixin;

import com.mojang.logging.LogUtils;
import forge.net.mca.entity.ai.Memories;
import forge.net.mca.entity.ai.BreedableRelationship;
import me.frigidambiance.deadlocksstory.Config;
import me.frigidambiance.deadlocksstory.stats.DeadlocksStoryStats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BreedableRelationship.class, remap = false)
public abstract class BreedableRelationshipMixin {
    @Unique
    private static final Logger deadlocksstory$LOGGER = LogUtils.getLogger();

    @Unique
    private Item deadlocksstory$giftItemBefore;

    @Unique
    private int deadlocksstory$giftCountBefore;

    @Inject(
            method = "giveGift",
            at = @At("HEAD"),
            require = 1
    )
    private void deadlocksstory$captureGiftBefore(
            ServerPlayer player,
            Memories memory,
            CallbackInfo ci
    ) {
        ItemStack stack = player.getMainHandItem();

        this.deadlocksstory$giftItemBefore = stack.getItem();
        this.deadlocksstory$giftCountBefore = stack.getCount();

        if (Config.debugLogging()) {
            deadlocksstory$LOGGER.info(
                    "[DeadlocksStory] MCA giveGift started for {} with {} x{}",
                    player.getGameProfile().getName(),
                    stack.getItem(),
                    stack.getCount()
            );
        }
    }

    @Inject(
            method = "giveGift",
            at = @At("RETURN"),
            require = 1
    )
    private void deadlocksstory$awardGiftAfter(
            ServerPlayer player,
            Memories memory,
            CallbackInfo ci
    ) {
        ItemStack stack = player.getMainHandItem();

        boolean sameItem = !stack.isEmpty() && stack.getItem() == this.deadlocksstory$giftItemBefore;
        boolean stackWasConsumed = stack.isEmpty() && this.deadlocksstory$giftCountBefore > 0;
        boolean stackCountWentDown = sameItem && stack.getCount() < this.deadlocksstory$giftCountBefore;

        if (stackWasConsumed || stackCountWentDown) {
            DeadlocksStoryStats.awardMcaGift(player);

            if (Config.debugLogging()) {
                deadlocksstory$LOGGER.info(
                        "[DeadlocksStory] MCA gift counted for {}",
                        player.getGameProfile().getName()
                );
            }
        } else if (Config.debugLogging()) {
            deadlocksstory$LOGGER.info(
                    "[DeadlocksStory] MCA gift was not counted for {}; before={} after={}",
                    player.getGameProfile().getName(),
                    this.deadlocksstory$giftCountBefore,
                    stack.getCount()
            );
        }
    }
}
