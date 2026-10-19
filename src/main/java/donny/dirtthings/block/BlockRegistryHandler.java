package donny.dirtthings.block;

import donny.dirtthings.DirtThings;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DirtThings.MODID)
public class BlockRegistryHandler {
    public static final BlockCompressedDirt COMPRESSED_DIRT = new BlockCompressedDirt();
    public static final BlockDirtMaker DIRT_MAKER = new BlockDirtMaker();

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(COMPRESSED_DIRT);
        event.getRegistry().register(DIRT_MAKER);
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(COMPRESSED_DIRT)
                .setRegistryName(COMPRESSED_DIRT.getRegistryName()));
        event.getRegistry().register(new ItemBlock(DIRT_MAKER)
                .setRegistryName(DIRT_MAKER.getRegistryName()));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        Item item = Item.getItemFromBlock(COMPRESSED_DIRT);
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(COMPRESSED_DIRT.getRegistryName(), "inventory"));
        Item dirtMakerItem = Item.getItemFromBlock(DIRT_MAKER);
        ModelLoader.setCustomModelResourceLocation(dirtMakerItem, 0,
                new ModelResourceLocation(DIRT_MAKER.getRegistryName(), "inventory"));
    }
}
