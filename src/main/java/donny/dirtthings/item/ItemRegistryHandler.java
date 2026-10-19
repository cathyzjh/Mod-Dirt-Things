package donny.dirtthings.item;

import donny.dirtthings.DirtThings;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import static donny.dirtthings.DirtThings.logger;

@Mod.EventBusSubscriber(modid = DirtThings.MODID)
public class ItemRegistryHandler {
    public static final ItemDirtBall DIRT_BALL = new ItemDirtBall();

    @SubscribeEvent
    public static void onRegistry(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(DIRT_BALL);
        logger.info("Registered Item");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(DIRT_BALL, 0,
                new ModelResourceLocation(DIRT_BALL.getRegistryName(), "inventory"));
    }
}
