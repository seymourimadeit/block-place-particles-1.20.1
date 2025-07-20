package tallestred.blockplaceparticles.platform;

import com.mojang.serialization.Codec;
import tallestred.blockplaceparticles.ParticleInteractionsMod;
import tallestred.blockplaceparticles.particle.ModParticleTypes;
import tallestred.blockplaceparticles.platform.services.PlatformHelperInterface;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.function.Function;

public class FabricPlatformHelper implements PlatformHelperInterface {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public SimpleParticleType createNewSimpleParticle(boolean alwaysShow) {
        return FabricParticleTypes.simple(alwaysShow);
    }

    @Override
    public <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> particleType, ModParticleTypes.SpriteProviderReg<T> particleProvider) {
        ParticleFactoryRegistry.getInstance().register(particleType, particleProvider::create);
    }

    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
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
