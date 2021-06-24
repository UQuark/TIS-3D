package li.cil.tis3d.api.detail;

import li.cil.tis3d.api.manual.RendererProvider;
import li.cil.tis3d.api.manual.ContentRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public interface ManualAPI {
    /**
     * Look up the documentation path for the specified item stack.
     *
     * @param stack the stack to find the documentation path for.
     * @return the path to the page, <tt>null</tt> if none is known.
     */
    @Nullable
    String pathFor(final ItemStack stack);

    /**
     * Look up the documentation for the specified block in the world.
     *
     * @param world the world containing the block.
     * @param pos   the position of the block.
     * @param side  the face of the block.
     * @return the path to the page, <tt>null</tt> if none is known.
     */
    @Nullable
    String pathFor(final World world, final BlockPos pos, final Direction side);

    /**
     * Get the content of the documentation page at the specified location.
     * <p>
     * The provided path may contain the special variable <tt>%LANGUAGE%</tt>,
     * which will be resolved to the currently set language, falling back to
     * <tt>en_US</tt>.
     *
     * @param path the path of the page to get the content of.
     * @return the content of the page, or <tt>null</tt> if none exists.
     */
    @Nullable
    Iterable<String> contentFor(final String path);

    /**
     * Get the image renderer for the specified image path.
     * <p>
     * This will look for {@link RendererProvider}s registered for a prefix in the
     * specified path. If there is no match, or the matched content provider
     * does not provide a renderer, this will return <tt>null</tt>.
     *
     * @param path the path to the image to get the renderer for.
     * @return the custom renderer for that path, or <tt>null</tt> if none exists.
     */
    @Nullable
    ContentRenderer imageFor(final String path);

    // ----------------------------------------------------------------------- //

    /**
     * Open the manual for the specified player.
     * <p>
     * If you wish to display a specific page, call {@link #navigate(String)}
     * after this function returns, with the path to the page to show.
     */
    void open();

    /**
     * Reset the history of the manual.
     */
    void reset();

    /**
     * Navigate to a page in the manual.
     *
     * @param path the path to navigate to.
     */
    void navigate(final String path);
}
