package donny.dirtthings.item;

import donny.dirtthings.DirtThings;
import donny.dirtthings.creativetab.DirtThingsCreativeTab;
import net.minecraft.item.Item;

public class ItemDirtBall extends Item {
    public ItemDirtBall() {
        this.setUnlocalizedName(DirtThings.MODID + ".dirt_ball");
        this.setCreativeTab(DirtThingsCreativeTab.DIRT_THINGS);
        this.setRegistryName("dirt_ball");
        this.maxStackSize = 16;
    }
}
