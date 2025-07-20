package tallestred.blockplaceparticles.platform;

import com.mojang.serialization.Codec;
import tallestred.blockplaceparticles.ParticleInteractionsMod;
import tallestred.blockplaceparticles.particle.ModParticleTypes;
import tallestred.blockplaceparticles.particle.RegParticleProvidersForge;
import tallestred.blockplaceparticles.platform.services.PlatformHelperInterface;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.function.Function;

public class ForgePlatformHelper implements PlatformHelperInterface {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public SimpleParticleType createNewSimpleParticle(boolean alwaysShow) {
        return new SimpleParticleType(alwaysShow);
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> particleType, ModParticleTypes.SpriteProviderReg<T> particleProvider) {
        RegParticleProvidersForge.registerProviderWhenReady(particleType, particleProvider);
    }

    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public SimpleParticleType registerParticleToRegistry(ResourceLocation particleID, SimpleParticleType value) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, particleID, value);
    }

    @Override
    public <T extends ParticleOptions> ParticleType<T> registerParticleOptions(ModParticleTypes.SpriteProviderReg<T> provider, ResourceLocation particleID, boolean alwaysShow, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory) {
        ParticleType<T> registeredParticleType = ParticleInteractionsMod.register(Registries.PARTICLE_TYPE, () -> new ParticleType<T>(alwaysShow, pDeserializer) {
            public Codec<T> codec() {
                return pCodecFactory.apply(this);
            }
        }, particleID);
        Services.PLATFORM.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }
}