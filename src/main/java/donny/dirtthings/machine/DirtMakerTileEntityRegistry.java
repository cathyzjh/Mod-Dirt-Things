package donny.dirtthings.machine;

import donny.dirtthings.DirtThings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class DirtMakerTileEntityRegistry {
    private DirtMakerTileEntityRegistry() {
    }

    public static void register() {
        GameRegistry.registerTileEntity(TileEntityDirtMaker.class,
                new ResourceLocation(DirtThings.MODID, "dirt_maker"));
    }
}
