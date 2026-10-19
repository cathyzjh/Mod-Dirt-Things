package donny.dirtthings.creativetab;

import donny.dirtthings.DirtThings;
import donny.dirtthings.item.ItemRegistryHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class DirtThingsCreativeTab {
    public static final CreativeTabs DIRT_THINGS = new CreativeTabs(DirtThings.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemRegistryHandler.DIRT_BALL);
        }
    };

    private DirtThingsCreativeTab() {
    }
}
