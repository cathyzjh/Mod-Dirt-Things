package donny.dirtthings.compat.jei;

import donny.dirtthings.block.BlockRegistryHandler;
import donny.dirtthings.machine.recipe.DirtMakerRecipe;
import donny.dirtthings.machine.recipe.DirtMakerRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

/** JEI discovers this class only when JEI is installed. */
@JEIPlugin
public final class DirtMakerJeiPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new DirtMakerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.handleRecipes(DirtMakerRecipe.class, DirtMakerRecipeWrapper::new, DirtMakerRecipeCategory.UID);
        registry.addRecipes(DirtMakerRecipes.getAll(), DirtMakerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(BlockRegistryHandler.DIRT_MAKER), DirtMakerRecipeCategory.UID);
    }
}
