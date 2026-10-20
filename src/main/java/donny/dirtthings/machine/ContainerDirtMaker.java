package donny.dirtthings.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerDirtMaker extends Container {
    private final TileEntityDirtMaker machine;
    private int burnTime;
    private int fuelTime;
    private int cookTime;
    private int processingTime;

    public ContainerDirtMaker(InventoryPlayer playerInventory, TileEntityDirtMaker machine) {
        this.machine = machine;

        addSlotToContainer(new Slot(machine, 0, 56, 17) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return machine.isItemValidForSlot(0, stack);
            }
        });
        addSlotToContainer(new Slot(machine, 1, 56, 53) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack);
            }
        });
        addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, machine, 2, 116, 35));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlotToContainer(new Slot(playerInventory, column + row * 9 + 9,
                        8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlotToContainer(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, machine.getField(0));
        listener.sendWindowProperty(this, 1, machine.getField(1));
        listener.sendWindowProperty(this, 2, machine.getField(2));
        listener.sendWindowProperty(this, 3, machine.getField(3));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : listeners) {
            if (burnTime != machine.getField(0)) {
                listener.sendWindowProperty(this, 0, machine.getField(0));
            }
            if (fuelTime != machine.getField(1)) {
                listener.sendWindowProperty(this, 1, machine.getField(1));
            }
            if (cookTime != machine.getField(2)) {
                listener.sendWindowProperty(this, 2, machine.getField(2));
            }
            if (processingTime != machine.getField(3)) {
                listener.sendWindowProperty(this, 3, machine.getField(3));
            }
        }
        burnTime = machine.getField(0);
        fuelTime = machine.getField(1);
        cookTime = machine.getField(2);
        processingTime = machine.getField(3);
    }

    @Override
    public void updateProgressBar(int id, int value) {
        machine.setField(id, value);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return machine.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        if (slot == null || !slot.getHasStack()) {
            return empty;
        }

        ItemStack stack = slot.getStack();
        ItemStack original = stack.copy();
        if (index == 2) {
            if (!mergeItemStack(stack, 3, 39, true)) {
                return empty;
            }
            slot.onSlotChange(stack, original);
        } else if (index != 0 && index != 1) {
            if (machine.isItemValidForSlot(0, stack)) {
                if (!mergeItemStack(stack, 0, 1, false)) {
                    return empty;
                }
            } else if (machine.isItemValidForSlot(1, stack)) {
                if (!mergeItemStack(stack, 1, 2, false)) {
                    return empty;
                }
            } else if (index < 30) {
                if (!mergeItemStack(stack, 30, 39, false)) {
                    return empty;
                }
            } else if (!mergeItemStack(stack, 3, 30, false)) {
                return empty;
            }
        } else if (!mergeItemStack(stack, 3, 39, false)) {
            return empty;
        }

        if (stack.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return empty;
        }
        slot.onTake(player, stack);
        return original;
    }
}
