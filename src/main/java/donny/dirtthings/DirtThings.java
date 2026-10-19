package donny.dirtthings;

import donny.dirtthings.machine.DirtMakerGuiHandler;
import donny.dirtthings.machine.DirtMakerTileEntityRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = DirtThings.MODID, name = DirtThings.NAME, version = DirtThings.VERSION)
public class DirtThings
{
    public static final String MODID = "dirtthings";
    public static final String NAME = "Dirt Things";
    public static final String VERSION = "1.0";

    public static Logger logger;

    @Mod.Instance(MODID)
    public static DirtThings instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        DirtMakerTileEntityRegistry.register();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new DirtMakerGuiHandler());
    }

}
