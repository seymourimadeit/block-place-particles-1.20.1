package games.enchanted.blockplaceparticles.particle;

import com.mojang.serialization.Codec;
import games.enchanted.blockplaceparticles.ParticleInteractionsMod;
import games.enchanted.blockplaceparticles.particle.bubble.UnderwaterRisingBubble;
import games.enchanted.blockplaceparticles.particle.dust.FloatingBrushDust;
import games.enchanted.blockplaceparticles.particle.dust.FloatingColouredDust;
import games.enchanted.blockplaceparticles.particle.option.ParticleEmitterOptions;
import games.enchanted.blockplaceparticles.particle.petal.FallingColouredPetal;
import games.enchanted.blockplaceparticles.particle.petal.FallingPetal;
import games.enchanted.blockplaceparticles.particle.spark.FlyingSpark;
import games.enchanted.blockplaceparticles.particle.spark.SparkEmitter;
import games.enchanted.blockplaceparticles.particle.spark.SparkFlash;
import games.enchanted.blockplaceparticles.particle.splash.BlockSplash;
import games.enchanted.blockplaceparticles.particle.splash.ColouredBucketSplash;
import games.enchanted.blockplaceparticles.particle.splash.LavaSplash;
import games.enchanted.blockplaceparticles.platform.Services;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ModParticleTypes {
    public static SimpleParticleType FALLING_CHERRY_PETAL;
    public static ParticleType<BlockParticleOption> FALLING_TINTED_LEAF;
    public static SimpleParticleType FALLING_AZALEA_LEAF;
    public static SimpleParticleType FALLING_FLOWERING_AZALEA_LEAF;
    public static ParticleType<BlockParticleOption> GRASS_BLADE;
    public static ParticleType<BlockParticleOption> HEAVY_GRASS_BLADE;
    public static SimpleParticleType MOSS_CLUMP;
    public static SimpleParticleType PALE_MOSS_CLUMP;
    public static SimpleParticleType BRUSH_DUST;
    public static SimpleParticleType BRUSH_DUST_SPECK;
    public static ParticleType<BlockParticleOption> TINTED_DUST;
    public static ParticleType<BlockParticleOption> TINTED_DUST_SPECK;

    public static ParticleType<BlockParticleOption> WATER_BUCKET_TINTED_SPLASH;
    public static SimpleParticleType LAVA_BUCKET_SPLASH;
    public static ParticleType<BlockParticleOption> GENERIC_FLUID_BUCKET_SPLASH;

    public static SimpleParticleType FLYING_SPARK;
    public static SimpleParticleType FLOATING_SPARK;
    public static SimpleParticleType FLYING_SOUL_SPARK;
    public static SimpleParticleType FLOATING_SOUL_SPARK;

    public static SimpleParticleType SPARK_FLASH;
    public static SimpleParticleType SOUL_SPARK_FLASH;

    public static SimpleParticleType UNDERWATER_RISING_BUBBLE;

    public static ParticleType<ParticleEmitterOptions> FLYING_SPARK_EMITTER;

    public static void registerParticles() {
        FALLING_CHERRY_PETAL = register((SpriteProviderReg) FallingPetal.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "falling_cherry_leaves"), false);
        FALLING_TINTED_LEAF = register((SpriteProviderReg) FallingColouredPetal.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "falling_tinted_leaves"), false, BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
        FALLING_AZALEA_LEAF = register((SpriteProviderReg) FallingPetal.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "falling_azalea_leaves"), false);
        FALLING_FLOWERING_AZALEA_LEAF = register((SpriteProviderReg) FallingPetal.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "falling_flowering_azalea_leaves"), false);
        GRASS_BLADE = register((SpriteProviderReg) FallingColouredPetal.LargerSpriteProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "grass_blade"), false, BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
        HEAVY_GRASS_BLADE = register((SpriteProviderReg) FallingColouredPetal.LargerSpriteMoreGravityProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "heavy_grass_blade"), false, BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
        MOSS_CLUMP = register((SpriteProviderReg) FallingPetal.RandomisedSizeMoreGravityProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "moss_clump"), false);
        PALE_MOSS_CLUMP = register((SpriteProviderReg) FallingPetal.RandomisedSizeMoreGravityProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "pale_moss_clump"), false);
        BRUSH_DUST = register((SpriteProviderReg) FloatingBrushDust.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "brush_dust"), false);
        BRUSH_DUST_SPECK = register((SpriteProviderReg) FloatingBrushDust.SpeckProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "brush_dust_speck"), false);
        TINTED_DUST = register((SpriteProviderReg) FloatingColouredDust.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "tinted_dust"), false, BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
        TINTED_DUST_SPECK = register((SpriteProviderReg) FloatingColouredDust.SpeckProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "tinted_dust_speck"), false, BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);

        WATER_BUCKET_TINTED_SPLASH = register((SpriteProviderReg) ColouredBucketSplash.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "water_bucket_tinted_splash"), false, BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
        LAVA_BUCKET_SPLASH = register((SpriteProviderReg) LavaSplash.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "lava_bucket_splash"), false);
        GENERIC_FLUID_BUCKET_SPLASH = register((SpriteProviderReg) BlockSplash.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "generic_fluid_bucket_splash"), false, BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);

        FLYING_SPARK = register((SpriteProviderReg) FlyingSpark.LongLifeSparkProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "flying_spark"), false);
        FLOATING_SPARK = register((SpriteProviderReg) FlyingSpark.ShortLifeSparkProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "floating_spark"), false);
        FLYING_SOUL_SPARK = register((SpriteProviderReg) FlyingSpark.LongLifeSoulSparkProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "flying_soul_spark"), false);
        FLOATING_SOUL_SPARK = register((SpriteProviderReg) FlyingSpark.ShortLifeSoulSparkProvider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "floating_soul_spark"), false);

        SPARK_FLASH = register((SpriteProviderReg) SparkFlash.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "spark_flash"), false);
        SOUL_SPARK_FLASH = register((SpriteProviderReg) SparkFlash.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "soul_spark_flash"), false);

        UNDERWATER_RISING_BUBBLE = register((SpriteProviderReg) UnderwaterRisingBubble.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "underwater_rising_bubble"), false);

        FLYING_SPARK_EMITTER = register((SpriteProviderReg) SparkEmitter.Provider::new, new ResourceLocation(ParticleInteractionsMod.MOD_ID, "flying_spark_emitter"), true, ParticleEmitterOptions.DESERIALIZER, ParticleEmitterOptions::codec);
    }

    private static SimpleParticleType register(SpriteProviderReg<SimpleParticleType> provider, ResourceLocation particleID, boolean alwaysShow) {
        SimpleParticleType registeredParticleType = Services.PLATFORM.registerParticleToRegistry(particleID, Services.PLATFORM.createNewSimpleParticle(alwaysShow));
        Services.PLATFORM.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }

    private static <T extends ParticleOptions> ParticleType<T> register(SpriteProviderReg<T> provider, ResourceLocation particleID, boolean alwaysShow, ParticleOptions.Deserializer<T> pDeserializer, final Function<ParticleType<T>, Codec<T>> pCodecFactory) {
        ParticleType<T> registeredParticleType = Services.PLATFORM.registerParticleOptions(provider, particleID, alwaysShow, pDeserializer, pCodecFactory);
        Services.PLATFORM.registerParticleProvider(registeredParticleType, provider);
        return registeredParticleType;
    }


    @FunctionalInterface
    public interface SpriteProviderReg<T extends ParticleOptions> {
        ParticleProvider<T> create(SpriteSet spriteSet);
    }
}
