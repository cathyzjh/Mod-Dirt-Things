package donny.dirtthings.compat.jei;

import donny.dirtthings.DirtThings;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public final class DirtMakerRecipeCategory implements IRecipeCategory<DirtMakerRecipeWrapper> {
    public static final String UID = DirtThings.MODID + ":dirt_maker";
    private final IDrawable background;
    private final IDrawable slots;

    public DirtMakerRecipeCategory(IGuiHelper helper) {
        background = helper.createBlankDrawable(150, 72);
        slots = helper.createDrawable(new ResourceLocation("minecraft", "textures/gui/container/furnace.png"),
                55, 16, 82, 54);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return I18n.format("container.dirtthings.dirt_maker");
    }

    @Override
    public String getModName() {
        return DirtThings.NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        slots.draw(minecraft, 0, 0);
        minecraft.fontRenderer.drawString(I18n.format("jei.dirtthings.dirt_maker.fuel"), 90, 38, 0x555555);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, DirtMakerRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup stacks = layout.getItemStacks();
        stacks.init(0, true, 0, 0);
        stacks.init(1, false, 60, 18);
        stacks.set(ingredients);
    }
}
