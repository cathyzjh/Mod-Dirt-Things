package donny.dirtthings.creativetab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirtThingsItemOrderTest {
    @BeforeClass
    public static void bootstrap() {
        Bootstrap.register();
    }

    private ItemStack stack(String name) {
        return new ItemStack(new Item().setRegistryName("dirtthings", name));
    }

    @Test
    public void itemsAreGroupedRegardlessOfRegistryInsertionOrder() {
        List<String> expected = Arrays.asList("dirt_ball", "compressed_dirt", "dirt_maker",
                "dirt_sword", "dirt_pickaxe", "dirt_axe", "dirt_shovel", "dirt_hoe",
                "dirt_helmet", "dirt_chestplate", "dirt_leggings", "dirt_boots");
        List<ItemStack> items = new ArrayList<>();
        for (String name : expected) {
            items.add(stack(name));
        }
        Collections.reverse(items);
        DirtThingsItemOrder.sort(items);
        assertEquals(expected.size(), items.size());
        for (int index = 0; index < expected.size(); index++) {
            assertEquals("dirtthings:" + expected.get(index), items.get(index).getItem().getRegistryName().toString());
        }
    }

    @Test
    public void unknownItemsRemainVisibleAndKeepTheirOrder() {
        ItemStack first = stack("future_item_a");
        ItemStack second = stack("future_item_b");
        ItemStack material = stack("dirt_ball");
        List<ItemStack> items = new ArrayList<>(Arrays.asList(first, material, second));
        DirtThingsItemOrder.sort(items);
        assertEquals(Arrays.asList(material, first, second), items);
    }

    @Test
    public void sortingPreservesStackDataAndVariantOrder() {
        Item item = new Item().setRegistryName("dirtthings", "dirt_helmet");
        ItemStack first = new ItemStack(item, 1, 8);
        ItemStack second = new ItemStack(item, 1, 3);
        ItemStack tool = stack("dirt_axe");
        List<ItemStack> items = new ArrayList<>(Arrays.asList(first, tool, second));
        DirtThingsItemOrder.sort(items);
        assertSame(tool, items.get(0));
        assertSame(first, items.get(1));
        assertSame(second, items.get(2));
        assertEquals(8, first.getMetadata());
        assertEquals(3, second.getMetadata());
    }
}
