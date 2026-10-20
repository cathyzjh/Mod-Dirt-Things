package donny.dirtthings.compat.jei;

import donny.dirtthings.DirtThings;
import donny.dirtthings.block.BlockRegistryHandler;
import donny.dirtthings.machine.recipe.DirtMakerRecipe;
import donny.dirtthings.machine.recipe.DirtMakerRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

/** JEI discovers this class only when JEI is installed. */
@JEIPlugin
public final class DirtMakerJeiPlugin implements IModPlugin {
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new DirtMakerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        List<DirtMakerRecipeWrapper> wrappers = new ArrayList<>();
        for (DirtMakerRecipe recipe : DirtMakerRecipes.getAll()) {
            wrappers.add(new DirtMakerRecipeWrapper(recipe));
        }
        registry.addRecipes(wrappers, DirtMakerRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(BlockRegistryHandler.DIRT_MAKER), DirtMakerRecipeCategory.UID);
        DirtThings.logger.info("Added {} dirt maker recipes to JEI", wrappers.size());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        IRecipeRegistry registry = runtime.getRecipeRegistry();
        IRecipeCategory<?> category = registry.getRecipeCategory(DirtMakerRecipeCategory.UID);
        int count = category == null ? 0 : registry.getRecipeWrappers(category).size();
        DirtThings.logger.info("JEI dirt maker category contains {} recipe displays", count);
        if (count == 0) {
            DirtThings.logger.error("JEI dirt maker category has no recipes; check the recipe registry and JSON loading");
        }
    }
}
