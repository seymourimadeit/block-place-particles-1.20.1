package tallestred.blockplaceparticles.mixin;

import tallestred.blockplaceparticles.ParticleInteractionsLogging;
import tallestred.blockplaceparticles.mixin.accessor.BucketItemAccessor;
import tallestred.blockplaceparticles.particle_spawning.SpawnParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCauldronBlock.class)
public abstract class AbstractCauldronBlockMixin {
    @Inject(
        at = @At("TAIL"),
        method = "use"
    )
    protected void spawnFluidOrBlockPlaceParticlesOnItemUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if(!level.isClientSide()) return;
        InteractionResult result = cir.getReturnValue();
        Item usedItem = player.getItemInHand(hand).getItem();
        if(result != InteractionResult.SUCCESS) return;
        if(usedItem instanceof BucketItem) {
            Fluid placedFluid = ((BucketItemAccessor) usedItem).block_place_particle$getContent();
            ParticleInteractionsLogging.debugInfo("Bucket of " + placedFluid.builtInRegistryHolder().key().location() + " placed in a cauldron at " + pos.toShortString());
            SpawnParticles.spawnFluidPlacedParticle(level, pos, placedFluid);
        } else if(usedItem instanceof BlockItem) {
            BlockState placedState = ((BlockItem) usedItem).getBlock().defaultBlockState();
            ParticleInteractionsLogging.debugInfo("Block '" + placedState.getBlock().builtInRegistryHolder().key().location() + "' placed in a cauldron at " + pos.toShortString());
            SpawnParticles.spawnBlockPlaceParticle((ClientLevel) level, pos, placedState);
        }
    }
}
