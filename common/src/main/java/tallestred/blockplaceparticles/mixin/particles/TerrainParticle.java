package tallestred.blockplaceparticles.mixin.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tallestred.blockplaceparticles.config.ConfigHandler;
import tallestred.blockplaceparticles.util.MathHelpers;

@Mixin(net.minecraft.client.particle.TerrainParticle.class)
public abstract class TerrainParticle extends TextureSheetParticle {
    @Unique
    private static final float MIN_UV_EPSILON = 0.0000001f;
    @Unique
    private float block_place_particle$normalizedQuadSize = 1;

    @Mutable
    @Shadow
    @Final
    private float uo;
    @Mutable
    @Shadow
    @Final
    private float vo;

    protected TerrainParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Unique
    private void block_place_particle$recalculateNormalizedQuadSize() {
        this.block_place_particle$normalizedQuadSize =
                this.quadSize <= 0.04 ?
                        (float) 1 / this.sprite.contents().width() :
                        MathHelpers.ceilWithResolution(this.quadSize + 0.0625, this.sprite.contents().width());

        if (this.uo + this.block_place_particle$normalizedQuadSize > 1)
            this.uo = 1 - this.block_place_particle$normalizedQuadSize;
        if (this.vo + this.block_place_particle$normalizedQuadSize > 1)
            this.vo = 1 - this.block_place_particle$normalizedQuadSize;
    }


    @Inject(
            at = @At("TAIL"),
            method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V"
    )
    protected void terrainParticleInit(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, BlockState state, BlockPos pos, CallbackInfo ci) {
        if (ConfigHandler.general_pixelConsistentTerrainParticles) {
            float spriteWidth = this.sprite.contents().width();
            float spriteHeight = this.sprite.contents().height();
            float randomPixelU = (float) MathHelpers.randomBetween(0, (int) spriteWidth);
            float randomPixelV = (float) MathHelpers.randomBetween(0, (int) spriteHeight);
            this.uo = randomPixelU / (spriteWidth / 4.0F);
            this.vo = randomPixelV / (spriteHeight / 4.0F);
            this.block_place_particle$recalculateNormalizedQuadSize();
        }
    }


    @Redirect(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU(D)F"),
            method = "getU0"
    )
    protected float alignU0(TextureAtlasSprite instance, double pU) {
        if (!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return instance.getU(pU);
        }
        this.block_place_particle$recalculateNormalizedQuadSize();
        return this.sprite.getU(this.uo * this.sprite.contents().width()) + MIN_UV_EPSILON;
    }

    @Redirect(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU(D)F"),
            method = "getU1"
    )
    protected float alignU1(TextureAtlasSprite instance, double pU) {
        if (!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return instance.getU(pU);
        }
        this.block_place_particle$recalculateNormalizedQuadSize();
        return this.sprite.getU((this.uo + this.block_place_particle$normalizedQuadSize) * this.sprite.contents().width()) + MIN_UV_EPSILON;
    }

    @Redirect(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getV(D)F"),
            method = "getV0"
    )
    protected float alignV0(TextureAtlasSprite instance, double pV) {
        if (!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return instance.getV(pV);
        }
        this.block_place_particle$recalculateNormalizedQuadSize();
        return this.sprite.getV(this.vo * this.sprite.contents().height()) + MIN_UV_EPSILON;
    }

    @Redirect(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getV(D)F"),
            method = "getV1"
    )
    protected float alignV1(TextureAtlasSprite instance, double pV) {
        if (!ConfigHandler.general_pixelConsistentTerrainParticles) {
            return instance.getV(pV);
        }
        this.block_place_particle$recalculateNormalizedQuadSize();
        return this.sprite.getV((this.vo + this.block_place_particle$normalizedQuadSize) * this.sprite.contents().height()) + MIN_UV_EPSILON;
    }
}