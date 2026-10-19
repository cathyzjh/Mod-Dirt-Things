package donny.dirtthings.armor;

import donny.dirtthings.DirtThings;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DirtThings.MODID)
public class ArmorRegistryHandler {
    public static final Item DIRT_HELMET = new ItemDirtArmor("dirt_helmet", EntityEquipmentSlot.HEAD);
    public static final Item DIRT_CHESTPLATE = new ItemDirtArmor("dirt_chestplate", EntityEquipmentSlot.CHEST);
    public static final Item DIRT_LEGGINGS = new ItemDirtArmor("dirt_leggings", EntityEquipmentSlot.LEGS);
    public static final Item DIRT_BOOTS = new ItemDirtArmor("dirt_boots", EntityEquipmentSlot.FEET);

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(DIRT_HELMET, DIRT_CHESTPLATE, DIRT_LEGGINGS, DIRT_BOOTS);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        registerModel(DIRT_HELMET);
        registerModel(DIRT_CHESTPLATE);
        registerModel(DIRT_LEGGINGS);
        registerModel(DIRT_BOOTS);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
