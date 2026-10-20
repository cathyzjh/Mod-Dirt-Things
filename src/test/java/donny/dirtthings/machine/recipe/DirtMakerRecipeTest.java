package donny.dirtthings.machine.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import net.minecraft.init.Blocks;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import org.junit.BeforeClass;
import org.junit.Test;
import donny.dirtthings.compat.jei.DirtMakerRecipeWrapper;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static org.junit.Assert.*;

public class DirtMakerRecipeTest {
    @BeforeClass
    public static void bootstrap() {
        Bootstrap.register();
        CraftingHelper.init();
    }

    private JsonObject recipeJson() throws Exception {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(
                "/assets/dirtthings/recipes/dirtmaker/dirt_to_dirt_ball.json"), StandardCharsets.UTF_8)) {
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
            // Use a vanilla item so these tests do not need a running FML mod loader.
            json.getAsJsonObject("result").addProperty("item", "minecraft:clay_ball");
            return json;
        }
    }

    private DirtMakerRecipe parse(JsonObject json) {
        return (DirtMakerRecipe) new DirtMakerRecipeFactory().parse(new JsonContext("dirtthings"), json);
    }

    @Test
    public void bundledRecipeAcceptsOnlyOrdinaryDirt() throws Exception {
        DirtMakerRecipe recipe = parse(recipeJson());
        assertTrue(recipe.matchesInput(new ItemStack(Blocks.DIRT, 1, 0)));
        assertFalse(recipe.matchesInput(new ItemStack(Blocks.DIRT, 1, 1)));
        assertFalse(recipe.matchesInput(new ItemStack(Blocks.DIRT, 1, 2)));
        assertFalse(recipe.matchesInput(ItemStack.EMPTY));
        assertEquals(2, recipe.getResult().getCount());
        assertEquals(200, recipe.getProcessingTime());
    }

    @Test
    public void inputCountAndTimeComeFromJson() throws Exception {
        JsonObject json = recipeJson();
        json.getAsJsonObject("ingredient").addProperty("count", 3);
        json.addProperty("processing_time", 80);
        DirtMakerRecipe recipe = parse(json);
        assertTrue(recipe.accepts(new ItemStack(Blocks.DIRT, 1)));
        assertFalse(recipe.matchesInput(new ItemStack(Blocks.DIRT, 2)));
        assertTrue(recipe.matchesInput(new ItemStack(Blocks.DIRT, 3)));
        assertEquals(80, recipe.getProcessingTime());
    }

    @Test
    public void machineRecipeCannotBeCraftedAndReturnsDefensiveCopies() throws Exception {
        DirtMakerRecipe recipe = parse(recipeJson());
        assertFalse(recipe.matches(null, null));
        assertFalse(recipe.canFit(3, 3));
        assertTrue(recipe.getCraftingResult(null).isEmpty());
        assertTrue(recipe.getRecipeOutput().isEmpty());
        assertTrue(recipe.isDynamic());
        recipe.getResult().shrink(1);
        assertEquals(2, recipe.getResult().getCount());
    }

    @Test(expected = JsonSyntaxException.class)
    public void rejectsNonpositiveTime() throws Exception {
        JsonObject json = recipeJson();
        json.addProperty("processing_time", 0);
        parse(json);
    }

    @Test(expected = JsonSyntaxException.class)
    public void rejectsOversizedOutput() throws Exception {
        JsonObject json = recipeJson();
        json.getAsJsonObject("result").addProperty("count", 65);
        parse(json);
    }

    @Test
    public void ordinaryCraftingRecipesLoadWithMetadata() throws Exception {
        for (String name : new String[]{"compressed_dirt1", "compressed_dirt_to_dirt"}) {
            try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(
                    "/assets/dirtthings/recipes/" + name + ".json"), StandardCharsets.UTF_8)) {
                String text = new JsonParser().parse(reader).toString()
                        .replace("dirtthings:compressed_dirt", "minecraft:clay");
                JsonObject json = new JsonParser().parse(text).getAsJsonObject();
                assertFalse(CraftingHelper.getRecipe(json, new JsonContext("dirtthings"))
                        .getRecipeOutput().isEmpty());
            }
        }
    }

    @Test
    public void recipeCacheRefreshesWhenRecipesArriveAfterFirstRead() throws Exception {
        int previousSize = DirtMakerRecipes.getAll().size();
        DirtMakerRecipe recipe = parse(recipeJson());
        recipe.setRegistryName("dirtthings", "test_late_recipe");
        ForgeRegistries.RECIPES.register(recipe);
        assertEquals(previousSize + 1, DirtMakerRecipes.getAll().size());
        assertTrue(DirtMakerRecipes.getAll().contains(recipe));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void jeiWrapperPublishesCountedInputAndOutput() throws Exception {
        AtomicReference<List<List<ItemStack>>> inputs = new AtomicReference<>();
        AtomicReference<ItemStack> output = new AtomicReference<>();
        IIngredients ingredients = (IIngredients) Proxy.newProxyInstance(
                IIngredients.class.getClassLoader(), new Class<?>[]{IIngredients.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("setInputLists")) {
                        inputs.set((List<List<ItemStack>>) args[1]);
                    } else if (method.getName().equals("setOutput")) {
                        output.set((ItemStack) args[1]);
                    }
                    return null;
                });
        JsonObject json = recipeJson();
        json.getAsJsonObject("ingredient").addProperty("count", 3);
        new DirtMakerRecipeWrapper(parse(json)).getIngredients(ingredients);
        assertEquals(1, inputs.get().size());
        assertEquals(3, inputs.get().get(0).get(0).getCount());
        assertEquals(0, inputs.get().get(0).get(0).getMetadata());
        assertEquals(2, output.get().getCount());
    }

    @Test
    public void dirtMakerCraftingRequiresDirtPistonAndFurnaceInOrder() throws Exception {
        try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(
                "/assets/dirtthings/recipes/dirt_maker.json"), StandardCharsets.UTF_8)) {
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
            assertEquals("dirtthings:dirt_maker", json.getAsJsonObject("result").get("item").getAsString());
            // Substitute only the output item to test without a running mod loader.
            json.getAsJsonObject("result").addProperty("item", "minecraft:furnace");
            IRecipe recipe = CraftingHelper.getRecipe(json, new JsonContext("dirtthings"));
            InventoryCrafting grid = new InventoryCrafting(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer player) {
                    return true;
                }
            }, 3, 3);
            net.minecraft.block.Block[] blocks = {
                    Blocks.DIRT, Blocks.DIRT, Blocks.DIRT,
                    Blocks.COBBLESTONE, Blocks.PISTON, Blocks.COBBLESTONE,
                    Blocks.COBBLESTONE, Blocks.FURNACE, Blocks.COBBLESTONE
            };
            for (int slot = 0; slot < blocks.length; slot++) {
                grid.setInventorySlotContents(slot, new ItemStack(blocks[slot]));
            }
            assertTrue(recipe.matches(grid, null));
            assertEquals(1, recipe.getCraftingResult(grid).getCount());
            grid.setInventorySlotContents(4, new ItemStack(Blocks.FURNACE));
            grid.setInventorySlotContents(7, new ItemStack(Blocks.PISTON));
            assertFalse("Swapped piston and furnace must not match", recipe.matches(grid, null));
            grid.setInventorySlotContents(4, new ItemStack(Blocks.PISTON));
            grid.setInventorySlotContents(7, new ItemStack(Blocks.FURNACE));
            grid.setInventorySlotContents(0, new ItemStack(Blocks.DIRT, 1, 1));
            assertFalse("Coarse dirt must not replace ordinary dirt", recipe.matches(grid, null));
        }
    }

    @Test
    public void allArmorRecipesMatchVanillaArmorShapesUsingOrdinaryDirt() throws Exception {
        String[] names = {"dirt_helmet", "dirt_chestplate", "dirt_leggings", "dirt_boots"};
        String[][] patterns = {
                {"DDD", "D D", "   "},
                {"D D", "DDD", "DDD"},
                {"DDD", "D D", "D D"},
                {"D D", "D D", "   "}
        };
        for (int i = 0; i < names.length; i++) {
            try (Reader reader = new InputStreamReader(getClass().getResourceAsStream(
                    "/assets/dirtthings/recipes/" + names[i] + ".json"), StandardCharsets.UTF_8)) {
                JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
                assertEquals("dirtthings:" + names[i], json.getAsJsonObject("result").get("item").getAsString());
                json.getAsJsonObject("result").addProperty("item", "minecraft:clay_ball");
                IRecipe recipe = CraftingHelper.getRecipe(json, new JsonContext("dirtthings"));
                InventoryCrafting grid = new InventoryCrafting(new Container() {
                    @Override
                    public boolean canInteractWith(EntityPlayer player) {
                        return true;
                    }
                }, 3, 3);
                for (int slot = 0; slot < 9; slot++) {
                    if (patterns[i][slot / 3].charAt(slot % 3) == 'D') {
                        grid.setInventorySlotContents(slot, new ItemStack(Blocks.DIRT, 1, 0));
                    }
                }
                assertTrue(names[i], recipe.matches(grid, null));
                assertEquals(1, recipe.getCraftingResult(grid).getCount());
                grid.setInventorySlotContents(0, new ItemStack(Blocks.DIRT, 1, 1));
                assertFalse("Coarse dirt must not craft " + names[i], recipe.matches(grid, null));
            }
        }
    }
}
