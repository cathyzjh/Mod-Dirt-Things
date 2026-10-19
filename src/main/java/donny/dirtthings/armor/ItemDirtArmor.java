package donny.dirtthings.armor;

import donny.dirtthings.DirtThings;
import donny.dirtthings.creativetab.DirtThingsCreativeTab;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemDirtArmor extends ItemArmor {
    public ItemDirtArmor(String registryName, EntityEquipmentSlot equipmentSlot) {
        super(DirtArmorMaterial.DIRT, 0, equipmentSlot);
        setRegistryName(registryName);
        setUnlocalizedName(DirtThings.MODID + "." + registryName);
        setCreativeTab(DirtThingsCreativeTab.DIRT_THINGS);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int layer = slot == EntityEquipmentSlot.LEGS ? 2 : 1;
        String overlay = type == null ? "" : "_" + type;
        return DirtThings.MODID + ":textures/models/armor/dirt_layer_" + layer + overlay + ".png";
    }

    @Override
    public int getColor(ItemStack stack) {
        return DirtArmorMaterial.DIRT_COLOR;
    }
}
