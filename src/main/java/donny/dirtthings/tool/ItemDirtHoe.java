package donny.dirtthings.tool;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemDirtHoe extends ItemHoe {
    public ItemDirtHoe() {
        super(DirtToolMaterial.DIRT);
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack tool, ItemStack material) {
        return !material.isEmpty()
                && material.getItem() == Item.getItemFromBlock(Blocks.DIRT)
                && material.getMetadata() == 0;
    }
}
