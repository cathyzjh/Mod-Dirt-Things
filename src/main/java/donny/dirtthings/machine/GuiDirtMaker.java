package donny.dirtthings.machine;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDirtMaker extends GuiContainer {
    private static final ResourceLocation FURNACE_GUI = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");
    private final InventoryPlayer playerInventory;
    private final TileEntityDirtMaker machine;
    public GuiDirtMaker(InventoryPlayer playerInventory, TileEntityDirtMaker machine) {
        super(new ContainerDirtMaker(playerInventory, machine));
        this.playerInventory = playerInventory;
        this.machine = machine;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = net.minecraft.client.resources.I18n.format(machine.getName());
        fontRenderer.drawString(title, (xSize - fontRenderer.getStringWidth(title)) / 2, 6, 4210752);
        fontRenderer.drawString(playerInventory.getDisplayName().getUnformattedText(), 8, ySize - 94, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(FURNACE_GUI);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, xSize, ySize);

        int burn = machine.getBurnTimeRemainingScaled(13);
        if (burn > 0) {
            drawTexturedModalRect(left + 56, top + 48 - burn, 176, 12 - burn, 14, burn + 1);
        }
        int cook = machine.getCookProgressScaled(24);
        drawTexturedModalRect(left + 79, top + 34, 176, 14, cook + 1, 16);
    }
}
