package tallestred.blockplaceparticles.platform.services;

import com.mojang.serialization.Codec;
import tallestred.blockplaceparticles.particle.ModParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.function.Function;

public interface PlatformHelperInterface {
    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Creates and returns a new instance of net.minecraft.core.particles.SimpleParticleType
     */
    SimpleParticleType createNewSimpleParticle(boolean alwaysShow);

    /**
     * Registers a particle to a particle provider
     */
    <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> particleType, ModParticleTypes.SpriteProviderReg<T> particleProvider);

    /**
     * Returns the path where configuration files are stored within the .minecraft directory
     */
    Path getConfigPath();

    /*
     * Creates a SimpleParticleType via registry
     */
    SimpleParticleType registerParticleToRegistry(ResourceLocation particleID, SimpleParticleType value);

    /*
     * Creates a ParticleOptions via registry
     */
    <T extends ParticleOptions> ParticleType<T>  registerParticleOptions(ModParticleTypes.SpriteProviderReg<T> provider, ResourceLocation particleID, boolean alwaysShow, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory);

}