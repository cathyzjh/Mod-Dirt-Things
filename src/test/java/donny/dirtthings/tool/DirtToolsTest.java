package donny.dirtthings.tool;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import donny.dirtthings.creativetab.DirtThingsCreativeTab;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirtToolsTest {
    @BeforeClass
    public static void bootstrap() {
        Bootstrap.register();
        CraftingHelper.init();
    }

    private Item[] dirtTools() {
        return new Item[]{ToolRegistryHandler.DIRT_SWORD, ToolRegistryHandler.DIRT_PICKAXE,
                ToolRegistryHandler.DIRT_AXE, ToolRegistryHandler.DIRT_HOE};
    }

    private Item[] woodTools() {
        return new Item[]{Items.WOODEN_SWORD, Items.WOODEN_PICKAXE,
                Items.WOODEN_AXE, Items.WOODEN_HOE};
    }

    @Test
    public void toolPropertiesAndCombatAttributesMatchWood() {
        Item[] dirt = dirtTools();
        Item[] wood = woodTools();
        for (int i = 0; i < dirt.length; i++) {
            assertEquals(wood[i].getMaxDamage(), dirt[i].getMaxDamage());
            assertEquals(wood[i].getItemEnchantability(), dirt[i].getItemEnchantability());
            assertEquals(1, dirt[i].getItemStackLimit());
            assertSame(DirtThingsCreativeTab.DIRT_THINGS, dirt[i].getCreativeTab());
            for (String attribute : wood[i].getItemAttributeModifiers(EntityEquipmentSlot.MAINHAND).keySet()) {
                List<Double> expected = new ArrayList<>();
                List<Double> actual = new ArrayList<>();
                for (AttributeModifier modifier : wood[i].getItemAttributeModifiers(
                        EntityEquipmentSlot.MAINHAND).get(attribute)) {
                    expected.add(modifier.getAmount());
                }
                for (AttributeModifier modifier : dirt[i].getItemAttributeModifiers(
                        EntityEquipmentSlot.MAINHAND).get(attribute)) {
                    actual.add(modifier.getAmount());
                }
                assertEquals(attribute, expected, actual);
            }
        }
    }

    @Test
    public void miningBehaviorMatchesWoodForRepresentativeBlocks() {
        Item[] dirt = dirtTools();
        Item[] wood = woodTools();
        Block[] targets = {Blocks.STONE, Blocks.LOG, Blocks.DIRT, Blocks.WEB, Blocks.DIAMOND_ORE};
        for (int i = 0; i < dirt.length; i++) {
            for (Block target : targets) {
                assertEquals(wood[i].getDestroySpeed(new ItemStack(wood[i]), target.getDefaultState()),
                        dirt[i].getDestroySpeed(new ItemStack(dirt[i]), target.getDefaultState()), 0.0F);
                assertEquals(wood[i].canHarvestBlock(target.getDefaultState()),
                        dirt[i].canHarvestBlock(target.getDefaultState()));
            }
        }
    }

    @Test
    public void toolsRepairWithOrdinaryDirtInsteadOfWoodOrCoarseDirt() {
        for (Item tool : dirtTools()) {
            ItemStack damaged = new ItemStack(tool, 1, 10);
            assertTrue(tool.getIsRepairable(damaged, new ItemStack(Blocks.DIRT, 1, 0)));
            assertFalse(tool.getIsRepairable(damaged, new ItemStack(Blocks.DIRT, 1, 1)));
            assertFalse(tool.getIsRepairable(damaged, new ItemStack(Blocks.PLANKS)));
            assertFalse(tool.getIsRepairable(damaged, ItemStack.EMPTY));
        }
    }

    @Test
    public void allFourRecipesUseCorrectShapedDirtAndStickPatterns() throws Exception {
        String[] names = {"sword", "pickaxe", "axe", "hoe"};
        String[][] shapes = {
                {"D  ", "D  ", "S  "},
                {"DDD", " S ", " S "},
                {"DD ", "DS ", " S "},
                {"DD ", " S ", " S "}
        };
        Item[] wood = woodTools();
        for (int i = 0; i < names.length; i++) {
            JsonObject json = readJson("/assets/dirtthings/recipes/dirt_" + names[i] + ".json");
            assertEquals("dirtthings:dirt_" + names[i], json.getAsJsonObject("result").get("item").getAsString());
            json.getAsJsonObject("result").addProperty("item", wood[i].getRegistryName().toString());
            IRecipe recipe = CraftingHelper.getRecipe(json, new JsonContext("dirtthings"));
            InventoryCrafting grid = new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer player) {
                    return true;
                }
            }, 3, 3);
            for (int slot = 0; slot < 9; slot++) {
                char symbol = shapes[i][slot / 3].charAt(slot % 3);
                if (symbol == 'D') {
                    grid.setInventorySlotContents(slot, new ItemStack(Blocks.DIRT, 1, 0));
                } else if (symbol == 'S') {
                    grid.setInventorySlotContents(slot, new ItemStack(Items.STICK));
                }
            }
            assertTrue(names[i], recipe.matches(grid, null));
            assertEquals(1, recipe.getCraftingResult(grid).getCount());
            grid.setInventorySlotContents(0, new ItemStack(Blocks.DIRT, 1, 1));
            assertFalse(names[i] + " must reject coarse dirt", recipe.matches(grid, null));
            grid.setInventorySlotContents(0, ItemStack.EMPTY);
            assertFalse(names[i] + " must reject missing material", recipe.matches(grid, null));
        }
    }

    @Test
    public void modelsReuseVanillaWoodenToolsAndNoShovelIsAdded() throws Exception {
        for (String name : new String[]{"sword", "pickaxe", "axe", "hoe"}) {
            JsonObject model = readJson("/assets/dirtthings/models/item/dirt_" + name + ".json");
            assertEquals("minecraft:item/wooden_" + name, model.get("parent").getAsString());
        }
        assertNull(getClass().getResource("/assets/dirtthings/recipes/dirt_shovel.json"));
        assertNull(getClass().getResource("/assets/dirtthings/models/item/dirt_shovel.json"));
    }

    private JsonObject readJson(String path) throws Exception {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(path), StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }
}
