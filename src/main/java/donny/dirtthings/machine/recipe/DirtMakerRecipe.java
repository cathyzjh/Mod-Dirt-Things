package donny.dirtthings.machine.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

/** Registered by Forge, but executable only by the dirt maker. */
public final class DirtMakerRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private final Ingredient ingredient;
    private final int inputCount;
    private final ItemStack result;
    private final int processingTime;

    public DirtMakerRecipe(Ingredient ingredient, int inputCount, ItemStack result, int processingTime) {
        this.ingredient = ingredient;
        this.inputCount = inputCount;
        this.result = result.copy();
        this.processingTime = processingTime;
    }

    public boolean accepts(ItemStack input) {
        return !input.isEmpty() && ingredient.apply(input);
    }

    public boolean matchesInput(ItemStack input) {
        return accepts(input) && input.getCount() >= inputCount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getInputCount() {
        return inputCount;
    }

    public ItemStack getResult() {
        return result.copy();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
