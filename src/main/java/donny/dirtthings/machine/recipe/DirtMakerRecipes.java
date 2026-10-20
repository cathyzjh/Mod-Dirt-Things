package donny.dirtthings.machine.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import donny.dirtthings.DirtThings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DirtThings.MODID)
public final class DirtMakerRecipes {
    private static List<DirtMakerRecipe> cachedRecipes;

    private DirtMakerRecipes() {
    }

    public static List<DirtMakerRecipe> getAll() {
        if (cachedRecipes != null) {
            return cachedRecipes;
        }
        List<DirtMakerRecipe> recipes = new ArrayList<>();
        for (IRecipe recipe : ForgeRegistries.RECIPES) {
            if (recipe instanceof DirtMakerRecipe) {
                recipes.add((DirtMakerRecipe) recipe);
            }
        }
        cachedRecipes = Collections.unmodifiableList(recipes);
        return cachedRecipes;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRecipeRegistry(RegistryEvent.Register<IRecipe> event) {
        cachedRecipes = null;
    }

    public static DirtMakerRecipe find(ItemStack input) {
        for (DirtMakerRecipe recipe : getAll()) {
            if (recipe.matchesInput(input)) {
                return recipe;
            }
        }
        return null;
    }

    public static boolean accepts(ItemStack input) {
        for (DirtMakerRecipe recipe : getAll()) {
            if (recipe.accepts(input)) {
                return true;
            }
        }
        return false;
    }
}
