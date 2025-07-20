package games.enchanted.blockplaceparticles;

import games.enchanted.blockplaceparticles.config.ConfigScreen;
import games.enchanted.blockplaceparticles.particle.ModParticleTypes;
import games.enchanted.blockplaceparticles.particle.RegParticleProvidersNeoForge;
import games.enchanted.blockplaceparticles.resource.ClientResourceReload;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.antlr.v4.runtime.misc.NotNull;

@Mod(value = ParticleInteractionsMod.MOD_ID)
public class NeoForgeClientEntrypoint {

    public NeoForgeClientEntrypoint() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ParticleInteractionsMod.startOfModLoading();

        // register stuff
        bus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey().equals(Registries.PARTICLE_TYPE)) {
                ModParticleTypes.registerParticles();
            }
        });

        // register client resource reload listener
        bus.addListener((RegisterClientReloadListenersEvent event) -> {
            event.registerReloadListener(new SimplePreparableReloadListener<Void>() {
                @Override
                protected @NotNull Void prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
                    ClientResourceReload.onReload(resourceManager);
                    return null;
                }

                @Override
                protected void apply(@NotNull Void object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
                }
            });
        });

        // register particle providers
        bus.addListener(RegParticleProvidersNeoForge::registerParticleProviders);
        // register config screen
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> ConfigScreen.createConfigScreen(screen)));

        ParticleInteractionsMod.endOfModLoading();
    }
}