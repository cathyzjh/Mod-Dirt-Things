package donny.dirtthings.machine;

import donny.dirtthings.machine.recipe.DirtMakerRecipe;
import donny.dirtthings.machine.recipe.DirtMakerRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public class TileEntityDirtMaker extends TileEntity implements IInventory, ITickable {
    public static final int PROCESS_TIME = 200;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    private int burnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int processingTime = PROCESS_TIME;

    @Override
    public void update() {
        boolean changed = false;
        if (burnTime > 0) {
            --burnTime;
        }
        if (!world.isRemote) {
            DirtMakerRecipe recipe = DirtMakerRecipes.find(inventory.get(0));
            processingTime = recipe == null ? PROCESS_TIME : recipe.getProcessingTime();
            if (burnTime == 0 && canProcess()) {
                consumeFuel();
                changed = burnTime > 0;
            }
            if (burnTime > 0 && canProcess()) {
                if (++cookTime >= processingTime) {
                    cookTime = 0;
                    processItem();
                    changed = true;
                }
            } else if (cookTime != 0) {
                cookTime = 0;
                changed = true;
            }
        }
        if (changed || !world.isRemote && (burnTime > 0 || cookTime > 0)) {
            markDirty();
        }
    }

    private void consumeFuel() {
        ItemStack fuel = inventory.get(1);
        currentItemBurnTime = burnTime = TileEntityFurnace.getItemBurnTime(fuel);
        if (burnTime > 0) {
            ItemStack container = fuel.getItem().getContainerItem(fuel);
            fuel.shrink(1);
            if (fuel.isEmpty()) {
                inventory.set(1, container);
            }
        }
    }

    private boolean canProcess() {
        DirtMakerRecipe recipe = DirtMakerRecipes.find(inventory.get(0));
        if (recipe == null) {
            return false;
        }
        ItemStack result = recipe.getResult();
        ItemStack output = inventory.get(2);
        return output.isEmpty() || ItemStack.areItemsEqual(output, result)
                && ItemStack.areItemStackTagsEqual(output, result)
                && output.getCount() + result.getCount() <= Math.min(getInventoryStackLimit(), output.getMaxStackSize());
    }

    private void processItem() {
        DirtMakerRecipe recipe = DirtMakerRecipes.find(inventory.get(0));
        if (recipe == null || !canProcess()) {
            return;
        }
        inventory.get(0).shrink(recipe.getInputCount());
        ItemStack result = recipe.getResult();
        ItemStack output = inventory.get(2);
        if (output.isEmpty()) {
            inventory.set(2, result);
        } else {
            output.grow(result.getCount());
        }
    }

    public int getBurnTimeRemainingScaled(int pixels) {
        return currentItemBurnTime == 0 ? 0 : burnTime * pixels / currentItemBurnTime;
    }

    public int getCookProgressScaled(int pixels) {
        return cookTime * pixels / Math.max(1, processingTime);
    }

    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack removed = ItemStackHelper.getAndSplit(inventory, index, count);
        if (!removed.isEmpty()) {
            markDirty();
        }
        return removed;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack removed = ItemStackHelper.getAndRemove(inventory, index);
        markDirty();
        return removed;
    }

    @Override
    public String getName() {
        return "container.dirtthings.dirt_maker";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public int getFieldCount() {
        return 4;
    }

    @Override
    public void clear() {
        inventory.clear();
        cookTime = 0;
        markDirty();
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack previous = inventory.get(index);
        if (index == 0 && (!ItemStack.areItemsEqual(previous, stack)
                || !ItemStack.areItemStackTagsEqual(previous, stack))) {
            cookTime = 0;
        }
        inventory.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(pos) == this && player.getDistanceSq(pos) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index == 0 ? DirtMakerRecipes.accepts(stack)
                : index == 1 && TileEntityFurnace.isItemFuel(stack);
    }

    @Override
    public int getField(int id) {
        return id == 0 ? burnTime : id == 1 ? currentItemBurnTime : id == 2 ? cookTime
                : id == 3 ? processingTime : 0;
    }

    @Override
    public void setField(int id, int value) {
        if (id == 0) {
            burnTime = value;
        } else if (id == 1) {
            currentItemBurnTime = value;
        } else if (id == 2) {
            cookTime = value;
        } else if (id == 3) {
            processingTime = value;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("BurnTime", burnTime);
        tag.setInteger("FuelTime", currentItemBurnTime);
        tag.setInteger("CookTime", cookTime);
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.get(i).isEmpty()) {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte) i);
                inventory.get(i).writeToNBT(item);
                items.appendTag(item);
            }
        }
        tag.setTag("Items", items);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        burnTime = tag.getInteger("BurnTime");
        currentItemBurnTime = tag.getInteger("FuelTime");
        cookTime = tag.getInteger("CookTime");
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
        NBTTagList items = tag.getTagList("Items", 10);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound item = items.getCompoundTagAt(i);
            int slot = item.getByte("Slot") & 255;
            if (slot < inventory.size()) {
                inventory.set(slot, new ItemStack(item));
            }
        }
    }
}
