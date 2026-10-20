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
}
