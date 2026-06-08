package me.frigidambiance.deadlocksstory;

import me.frigidambiance.deadlocksstory.stats.ModStats;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DeadlocksStory.MOD_ID)
public class DeadlocksStory {
    public static final String MOD_ID = "deadlocksstory";

    public DeadlocksStory(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModStats.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        context.registerConfig(
              ModConfig.Type.COMMON,
              Config.SPEC
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Stats.CUSTOM.get(ModStats.MCA_GIFTS_GIVEN.get(), StatFormatter.DEFAULT);
            Stats.CUSTOM.get(ModStats.BOUNTIES_COMPLETED.get(), StatFormatter.DEFAULT);
        });
    }
}
