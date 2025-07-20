package tallestred.blockplaceparticles.mixin.particles;

import tallestred.blockplaceparticles.particle.StretchyBouncyShapeParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StretchyBouncyShapeParticle.class)
public abstract class StretchyBouncyQuadParticleNeoForge extends TextureSheetParticle {
    protected StretchyBouncyQuadParticleNeoForge(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Override
    public AABB getBoundingBox() {
        double diffX = this.x - this.xo;
        double diffY = this.y - this.yo;
        double diffZ = this.z - this.zo;
        return super.getBoundingBox().move(-diffX / 2, -diffY / 2, -diffZ / 2).inflate( Math.abs(new Vec3(this.x, this.y, this.z).distanceTo(new Vec3(this.xo, this.yo, this.zo)) / 2 ));
    }
}
