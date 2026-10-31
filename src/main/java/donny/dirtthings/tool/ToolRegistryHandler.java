package donny.dirtthings.tool;

import donny.dirtthings.DirtThings;
import donny.dirtthings.creativetab.DirtThingsCreativeTab;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DirtThings.MODID)
public final class ToolRegistryHandler {
    public static final Item DIRT_SWORD = configure(new ItemDirtSword(), "dirt_sword");
    public static final Item DIRT_PICKAXE = configure(new ItemDirtPickaxe(), "dirt_pickaxe");
    public static final Item DIRT_AXE = configure(new ItemDirtAxe(), "dirt_axe");
    public static final Item DIRT_HOE = configure(new ItemDirtHoe(), "dirt_hoe");

    private ToolRegistryHandler() {
    }

    private static Item configure(Item item, String name) {
        item.setRegistryName(DirtThings.MODID, name);
        item.setUnlocalizedName(DirtThings.MODID + "." + name);
        item.setCreativeTab(DirtThingsCreativeTab.DIRT_THINGS);
        return item;
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(DIRT_SWORD, DIRT_PICKAXE, DIRT_AXE, DIRT_HOE);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        registerModel(DIRT_SWORD);
        registerModel(DIRT_PICKAXE);
        registerModel(DIRT_AXE);
        registerModel(DIRT_HOE);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
