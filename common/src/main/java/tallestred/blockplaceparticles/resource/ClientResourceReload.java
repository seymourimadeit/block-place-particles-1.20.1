package tallestred.blockplaceparticles.resource;

import tallestred.blockplaceparticles.ParticleInteractionsLogging;
import tallestred.blockplaceparticles.util.ColourUtil;
import net.minecraft.server.packs.resources.ResourceManager;

public class ClientResourceReload {
    public static void onReload(ResourceManager resourceManager) {
        ColourUtil.invalidateCaches();
        ParticleInteractionsLogging.message("Cleared average block colour cache");
    }
}
