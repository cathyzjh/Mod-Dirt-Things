package donny.dirtthings.tool;

import donny.dirtthings.block.BlockRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemDirtShovel extends ItemSpade {
    private static final float DIRT_SPEED_MULTIPLIER = 20.0F;

    public ItemDirtShovel() {
        super(DirtToolMaterial.DIRT);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.DIRT || block == Blocks.GRASS || block == BlockRegistryHandler.COMPRESSED_DIRT) {
            // Treat compressed dirt as a shovel target, even though vanilla does not list it.
            return DirtToolMaterial.DIRT.getEfficiency() * DIRT_SPEED_MULTIPLIER;
        }
        return super.getDestroySpeed(stack, state);
    }
}
