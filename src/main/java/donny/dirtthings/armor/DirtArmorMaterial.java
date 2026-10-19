package donny.dirtthings.armor;

import net.minecraft.item.ItemArmor;

public final class DirtArmorMaterial {
    // Leather provides the intended durability multiplier (5) and protection values.
    public static final ItemArmor.ArmorMaterial DIRT = ItemArmor.ArmorMaterial.LEATHER;

    /**
     * A muted, dark earth brown close to Minecraft's dirt palette.  Leather
     * armor applies this only to the base texture layer; the overlay remains
     * uncoloured so its original pixel details stay visible.
     */
    public static final int DIRT_COLOR = 0x765238;

    private DirtArmorMaterial() {
    }
}
