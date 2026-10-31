package donny.dirtthings.tool;

import net.minecraft.item.ItemAxe;

public class ItemDirtAxe extends ItemAxe {
    public ItemDirtAxe() {
        // Explicit wooden-axe values avoid indexing vanilla arrays with a custom material.
        super(DirtToolMaterial.DIRT, 6.0F, -3.2F);
    }
}
