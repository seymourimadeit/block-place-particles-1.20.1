package games.enchanted.blockplaceparticles.mixin.blocks;

import games.enchanted.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.level.block.TntBlock.class)
public abstract class TntBlock {
    @Inject(
        at = @At("RETURN"),
        method = "use"
    )
    protected void useItemOn(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
       ItemStack itemStack = player.getItemInHand(hand);
        if(level.isClientSide() && cir.getReturnValue() == InteractionResult.SUCCESS) {
            if(itemStack.getItem() instanceof FlintAndSteelItem) {
                SpawnParticles.spawnFlintAndSteelSparkParticle(level, pos);
            } else if(itemStack.getItem() instanceof FireChargeItem) {
                SpawnParticles.spawnFireChargeSmokeParticle(level, pos);
            }
        }
    }
}
