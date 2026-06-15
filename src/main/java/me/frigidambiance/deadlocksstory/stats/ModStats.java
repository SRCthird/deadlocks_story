package me.frigidambiance.deadlocksstory.stats;

import me.frigidambiance.deadlocksstory.DeadlocksStory;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModStats {
    public static final DeferredRegister<ResourceLocation> CUSTOM_STATS =
        DeferredRegister.create(Registries.CUSTOM_STAT, DeadlocksStory.MOD_ID);

    public static final RegistryObject<ResourceLocation> MCA_GIFTS_GIVEN =
        CUSTOM_STATS.register("mca_gifts_given",
            () -> ResourceLocation.fromNamespaceAndPath(DeadlocksStory.MOD_ID, "mca_gifts_given"));

    public static final RegistryObject<ResourceLocation> MCA_ROOMS_CREATED =
        CUSTOM_STATS.register("mca_rooms_created",
            () -> ResourceLocation.fromNamespaceAndPath(DeadlocksStory.MOD_ID, "mca_rooms_created"));

    public static final RegistryObject<ResourceLocation> BOUNTIES_COMPLETED =
        CUSTOM_STATS.register("bounties_completed",
            () -> ResourceLocation.fromNamespaceAndPath(DeadlocksStory.MOD_ID, "bounties_completed"));

    public static final RegistryObject<ResourceLocation> SPELLS_CAST =
        CUSTOM_STATS.register("spells_cast",
            () -> ResourceLocation.fromNamespaceAndPath(DeadlocksStory.MOD_ID, "spells_cast"));

    private ModStats() {
    }

    public static void register(IEventBus eventBus) {
        CUSTOM_STATS.register(eventBus);
    }
}
