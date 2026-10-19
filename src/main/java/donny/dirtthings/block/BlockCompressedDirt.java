package donny.dirtthings.block;

import donny.dirtthings.DirtThings;
import donny.dirtthings.creativetab.DirtThingsCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCompressedDirt extends Block {
    public BlockCompressedDirt() {
        super(Material.GROUND);
        setUnlocalizedName(DirtThings.MODID + ".compressed_dirt");
        setRegistryName("compressed_dirt");
        setCreativeTab(DirtThingsCreativeTab.DIRT_THINGS);
        setHardness(0.6F);
        setSoundType(SoundType.GROUND);
    }
}
