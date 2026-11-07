package donny.dirtthings.creativetab;

import donny.dirtthings.DirtThings;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/** Controls display order only; item registry IDs and saved inventories are unchanged. */
public final class DirtThingsItemOrder {
    private static final List<String> ORDER = Arrays.asList(
            "dirt_ball",
            "compressed_dirt", "dirt_maker",
            "dirt_sword", "dirt_pickaxe", "dirt_axe", "dirt_shovel", "dirt_hoe",
            "dirt_helmet", "dirt_chestplate", "dirt_leggings", "dirt_boots");

    private DirtThingsItemOrder() {
    }

    public static void sort(List<ItemStack> items) {
        // List.sort is stable: future items and variants retain their relative order.
        items.sort(Comparator.comparingInt(DirtThingsItemOrder::getPriority));
    }

    private static int getPriority(ItemStack stack) {
        ResourceLocation name = stack.getItem().getRegistryName();
        if (name != null) {
            for (int index = 0; index < ORDER.size(); index++) {
                if (name.equals(new ResourceLocation(DirtThings.MODID, ORDER.get(index)))) {
                    return index;
                }
            }
        }
        return Integer.MAX_VALUE;
    }
}
