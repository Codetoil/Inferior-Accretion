package inferioraccretionteam.inferioraccretion.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import inferioraccretionteam.inferioraccretion.world.level.levelgen.feature.configurations.SimpleBlockWithBambooConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BambooBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SimpleBlockWithBambooFeature extends Feature<SimpleBlockWithBambooConfiguration>
{
	private static final BlockState BAMBOO_TRUNK = Blocks.BAMBOO.defaultBlockState().setValue(BambooBlock.AGE, 1).setValue(BambooBlock.LEAVES, BambooLeaves.NONE).setValue(BambooBlock.STAGE, 0);
	private static final BlockState BAMBOO_FINAL_LARGE = BAMBOO_TRUNK.setValue(BambooBlock.LEAVES, BambooLeaves.LARGE).setValue(BambooBlock.STAGE, 1);
	private static final BlockState BAMBOO_TOP_LARGE = BAMBOO_TRUNK.setValue(BambooBlock.LEAVES, BambooLeaves.LARGE);
	private static final BlockState BAMBOO_TOP_SMALL = BAMBOO_TRUNK.setValue(BambooBlock.LEAVES, BambooLeaves.SMALL);

	public SimpleBlockWithBambooFeature(Codec<SimpleBlockWithBambooConfiguration> p_65137_) {
		super(p_65137_);
	}

	/**
	 * Places the given feature at the given location.
	 * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
	 * that they can safely generate into.
	 * @param pContext A context object with a reference to the level and the position the feature is being placed at
	 */
	public boolean place(FeaturePlaceContext<SimpleBlockWithBambooConfiguration> pContext) {
		return placeBamboo(pContext);
	}

	public boolean placeBamboo(FeaturePlaceContext<SimpleBlockWithBambooConfiguration> pContext) {
		int i = 0;
		BlockPos blockpos = pContext.origin();
		WorldGenLevel worldgenlevel = pContext.level();
		RandomSource randomsource = pContext.random();
		SimpleBlockWithBambooConfiguration probabilityfeatureconfiguration = pContext.config();
		BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos.mutable();
		BlockPos.MutableBlockPos blockpos$mutableblockpos1 = blockpos.mutable();
		if (worldgenlevel.isEmptyBlock(blockpos$mutableblockpos)) {
			if (Blocks.BAMBOO.defaultBlockState().canSurvive(worldgenlevel, blockpos$mutableblockpos)) {
				int j = randomsource.nextInt(12) + 5;
				if (randomsource.nextFloat() < probabilityfeatureconfiguration.bambooProbability()) {
					int k = randomsource.nextInt(4) + 1;

					for(int l = blockpos.getX() - k; l <= blockpos.getX() + k; ++l) {
						for(int i1 = blockpos.getZ() - k; i1 <= blockpos.getZ() + k; ++i1) {
							int j1 = l - blockpos.getX();
							int k1 = i1 - blockpos.getZ();
							if (j1 * j1 + k1 * k1 <= k * k) {
								blockpos$mutableblockpos1.set(l, worldgenlevel.getHeight(Heightmap.Types.WORLD_SURFACE, l, i1) - 1, i1);
								if (isDirt(worldgenlevel.getBlockState(blockpos$mutableblockpos1))) {
									worldgenlevel.setBlock(blockpos$mutableblockpos1, Blocks.MOSS_BLOCK.defaultBlockState(), 2);
								}
							}
						}
					}
				}

				for(int l1 = 0; l1 < j && worldgenlevel.isEmptyBlock(blockpos$mutableblockpos); ++l1) {
					worldgenlevel.setBlock(blockpos$mutableblockpos, BAMBOO_TRUNK, 2);
					blockpos$mutableblockpos.move(Direction.UP, 1);
				}

				if (blockpos$mutableblockpos.getY() - blockpos.getY() >= 3) {
					worldgenlevel.setBlock(blockpos$mutableblockpos, BAMBOO_FINAL_LARGE, 2);
					worldgenlevel.setBlock(blockpos$mutableblockpos.move(Direction.DOWN, 1), BAMBOO_TOP_LARGE, 2);
					worldgenlevel.setBlock(blockpos$mutableblockpos.move(Direction.DOWN, 1), BAMBOO_TOP_SMALL, 2);
				}
			}

			++i;
		}

		return i > 0;
	}
}