package donny.dirtthings.tool;

import donny.dirtthings.DirtThings;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public final class DirtToolMaterial {
    public static final Item.ToolMaterial DIRT = createMaterial();

    private DirtToolMaterial() {
    }

    private static Item.ToolMaterial createMaterial() {
        Item.ToolMaterial wood = Item.ToolMaterial.WOOD;
        Item.ToolMaterial dirt = EnumHelper.addToolMaterial(
                DirtThings.MODID + "_tool",
                wood.getHarvestLevel(),
                wood.getMaxUses(),
                wood.getEfficiency(),
                wood.getAttackDamage(),
                wood.getEnchantability());
        dirt.setRepairItem(new ItemStack(Blocks.DIRT, 1, 0));
        return dirt;
    }
}
