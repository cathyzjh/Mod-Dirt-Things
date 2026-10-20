package donny.dirtthings.machine.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public final class DirtMakerRecipeFactory implements IRecipeFactory {
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        JsonObject input = JsonUtils.getJsonObject(json, "ingredient");
        Ingredient ingredient = CraftingHelper.getIngredient(input, context);
        int count = JsonUtils.getInt(input, "count", 1);
        ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
        int time = JsonUtils.getInt(json, "processing_time", 200);
        if (count < 1 || count > 64 || time < 1 || time > 32767) {
            throw new JsonSyntaxException("Dirt maker requires input count 1..64 and processing_time 1..32767");
        }
        if (result.isEmpty() || result.getCount() > result.getMaxStackSize()) {
            throw new JsonSyntaxException("Dirt maker result must fit in one nonempty item stack");
        }
        return new DirtMakerRecipe(ingredient, count, result, time);
    }
}
