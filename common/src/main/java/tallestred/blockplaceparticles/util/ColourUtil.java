package tallestred.blockplaceparticles.util;

import net.minecraft.world.level.block.Blocks;
import tallestred.blockplaceparticles.mixin.accessor.SpriteContentsAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class ColourUtil {
    private static final HashMap<ResourceLocation, Integer> colourCache = new HashMap<>();

    /**
     * Calculates and caches the average colour from a {@link BlockState}'s pixel texture
     *
     * @param blockState the block state
     * @return the average colour in an array of a, r, g, b
     */
    public static int[] getAverageBlockColour(BlockState blockState) {
        TextureAtlasSprite particleSprite = blockState.getBlock() == Blocks.GRASS ? TextureHelpers.getSpriteFromBlockAtlas(new ResourceLocation("block/grass_block_top")) : Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).getParticleIcon();
        ResourceLocation particleSpriteLocation = particleSprite.contents().name();
        if (colourCache.containsKey(particleSpriteLocation)) {
            return ARGBint_to_ARGB(colourCache.get(particleSpriteLocation));
        }
        int average = calculateAverageSpriteColour(particleSprite);
        colourCache.put(particleSpriteLocation, average);
        return ARGBint_to_ARGB(average);
    }

    /**
     * Converts an int in rgb decimal format to an array of a, r, g, b
     */
    public static int[] RGBint_to_ARGB(int rgb) {
        int[] rgbArray = RGBint_to_RGB(rgb);
        return new int[]{255, rgbArray[0], rgbArray[1], rgbArray[2]};
    }

    /**
     * Calculates the average from a {@link TextureAtlasSprite}
     *
     * @param sprite a sprite to calculate the average colour of
     * @return the average colour as an argb int
     */
    public static int calculateAverageSpriteColour(TextureAtlasSprite sprite) {
        if (sprite == null) return -1;
        SpriteContents spriteContents = sprite.contents();
        if (spriteContents.getUniqueFrames().count() == 0) return -1;
        float total = 0, red = 0, blue = 0, green = 0, alpha = 0;
        for (int x = 0; x < spriteContents.width(); x++) {
            for (int y = 0; y < spriteContents.height(); y++) {
                int color = ((SpriteContentsAccessor) spriteContents).block_place_particle$getOriginalImage().getPixelRGBA(x, y);
                int[] argb = ABGRint_to_RGBA(color);
                int pixelAlpha = argb[0];
                if (pixelAlpha <= 10) continue;
                total++;
                alpha += pixelAlpha;
                red += argb[1];
                green += argb[2];
                blue += argb[3];
            }
        }

        float[] hsb = Color.RGBtoHSB((int) (red / total), (int) (green / total), (int) (blue / total), null);
        hsb[2] *= 1.05f;
        int[] rgb = RGBint_to_RGB(
                Color.HSBtoRGB(
                        Mth.clamp(hsb[0], 0, 1),
                        Mth.clamp(hsb[1], 0, 1),
                        Mth.clamp(hsb[2], 0, 1)
                )
        );

        return ARGB_to_ARGBint((int) (alpha / total), rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Gets a random pixel's colour from a {@link BlockState}'s pixel texture
     *
     * @param blockState the block state to get a random colour from
     * @return the colour in an array of a, r, g, b
     */
    public static int[] getRandomBlockColour(BlockState blockState) {
        TextureAtlasSprite particleSprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).getParticleIcon();
        SpriteContents spriteContents = particleSprite.contents();

        int x = MathHelpers.randomBetween(0, spriteContents.width() - 1);
        int y = MathHelpers.randomBetween(0, spriteContents.height() - 1);

        int sampledColour = ((SpriteContentsAccessor) spriteContents).block_place_particle$getOriginalImage().getPixelRGBA(x, y);

        return ABGRint_to_RGBA(sampledColour);
    }


    /**
     * Clears all calculated average colours
     */
    public static void invalidateCaches() {
        ColourUtil.colourCache.clear();
    }

    /**
     * Converts argb to an int in argb decimal format
     */
    public static int ARGB_to_ARGBint(int a, int r, int g, int b) {
        int alpha = MathHelpers.clampInt(a, 0, 255);
        int red = MathHelpers.clampInt(r, 0, 255);
        int green = MathHelpers.clampInt(g, 0, 255);
        int blue = MathHelpers.clampInt(b, 0, 255);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * Converts an int in argb decimal format to an array of a, r, g, b
     */
    public static int[] ARGBint_to_ARGB(int argb) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        return new int[]{a, r, g, b};
    }

    /**
     * Converts an int in abgr decimal format to an array of a, r, g, b
     */
    public static int[] ABGRint_to_RGBA(int abgr) {
        int a = (abgr >> 24) & 0xFF;
        int b = (abgr >> 16) & 0xFF;
        int g = (abgr >> 8) & 0xFF;
        int r = abgr & 0xFF;
        return new int[]{a, r, g, b};
    }

    /**
     * Converts rgb to an int in rgb decimal format
     */
    public static int RGB_to_RGBint(int r, int g, int b) {
        int red = MathHelpers.clampInt(r, 0, 255);
        int green = MathHelpers.clampInt(g, 0, 255);
        int blue = MathHelpers.clampInt(b, 0, 255);
        return (red << 16) | (green << 8) | blue;
    }

    /**
     * Converts an int in rgb decimal format to an array of r, g, b
     */
    public static int[] RGBint_to_RGB(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return new int[]{r, g, b};
    }

    /**
     * Multiply two colours together. The inputs can be either ARGB or RGB, but they must both be the same format
     *
     * @return the colour in ARGB or RGB
     */
    public static int[] multiplyColours(int[] colour1, int[] colour2) {
        if (colour1.length != colour2.length) {
            throw new IllegalArgumentException(ColourUtil.class.getName() + "#multiplyColours: colour1 and colour2 must both be either ARGB or RGB arrays. colour1: " + Arrays.toString(colour1) + ", colour2: " + Arrays.toString(colour2));
        }
        if (!(colour1.length == 4 || colour1.length == 3)) {
            throw new IllegalArgumentException(ColourUtil.class.getName() + "#multiplyColours: both colours must have 4 or 3 values. colour1: " + Arrays.toString(colour1) + ", colour2: " + Arrays.toString(colour2));
        }
        int[] multipliedColour = new int[colour1.length];
        for (int i = 0; i < colour1.length; i++) {
            multipliedColour[i] = (int) (Mth.clamp((colour1[i] / 255f) * (colour2[i] / 255f), 0f, 255f) * 255);
        }
        return multipliedColour;
    }

}
