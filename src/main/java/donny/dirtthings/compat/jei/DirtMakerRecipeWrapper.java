package donny.dirtthings.compat.jei;

import donny.dirtthings.machine.recipe.DirtMakerRecipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public final class DirtMakerRecipeWrapper implements IRecipeWrapper {
    private final DirtMakerRecipe recipe;

    public DirtMakerRecipeWrapper(DirtMakerRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> inputs = new ArrayList<>();
        for (ItemStack matching : recipe.getIngredient().getMatchingStacks()) {
            ItemStack input = matching.copy();
            input.setCount(recipe.getInputCount());
            inputs.add(input);
        }
        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(inputs));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int width, int height, int mouseX, int mouseY) {
        String time = I18n.format("jei.dirtthings.dirt_maker.time", recipe.getProcessingTime() / 20.0F);
        minecraft.fontRenderer.drawString(time, 0, 60, 0x555555);
    }
}
