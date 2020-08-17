package net.pugsworth.multarumore.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pugsworth.multarumore.MultarumOreMod;

public class MultarumOreBlock extends Block {

    public static final BooleanProperty LIT;
    private static DustParticleEffect[] _dustParticles = new DustParticleEffect[5];
    private static final Random _random = new Random();

    public MultarumOreBlock(Settings settings)
    {
        super(settings);
        this.setDefaultState((BlockState) this.getDefaultState().with(LIT, false));

        _dustParticles[0] = new DustParticleEffect(0.902f, 0.447f, 0.098f, 1.0f); // Princeton Orange
        _dustParticles[1] = new DustParticleEffect(0.902f, 0.847f, 0.098f, 1.0f); // Citrine
        _dustParticles[2] = new DustParticleEffect(0.902f, 0.098f, 0.098f, 1.0f); // Red Pigment
        _dustParticles[3] = new DustParticleEffect(0.220f, 0.902f, 0.098f, 1.0f); // Neon Green
        _dustParticles[4] = new DustParticleEffect(0.098f, 0.192f, 0.902f, 1.0f); // Blue
        
    }


    @Override
    public RenderLayer getRenderLayer() {
        return RenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public int getLuminance(BlockState state){
        return (Boolean) state.get(LIT) ? super.getLuminance(state) : 0;
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos,
            final PlayerEntity player)
    {
        light(state, world, pos);
        super.onBlockBreakStart(state, world, pos, player);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        float rand = world.random.nextFloat();

        if (MultarumOreMod.CONFIG.tntEnabled && (!world.isClient() && !player.isCreative() && rand < MultarumOreMod.CONFIG.tntChance)) {
            world.removeBlock(pos, false);

            TntEntity tnt = new TntEntity(world, pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, player); // World world, double x, double y, double z, @Nullable LivingEntity igniter

            world.spawnEntity(tnt);
            world.playSound((PlayerEntity)null, tnt.x, tnt.y, tnt.z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);

            return;
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, Entity entity)
    {
        light(world.getBlockState(pos), world, pos);
        super.onSteppedOn(world, pos, entity);
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockHitResult hit)
    {
        light(state, world, pos);
        return super.activate(state, world, pos, player, hand, hit);
    }

    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random)
    {
        if ((Boolean) state.get(LIT)) {
            world.setBlockState(pos, (BlockState) state.with(LIT, false), 3);
        }
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
    {
        if ((Boolean) state.get(LIT)) {
            spawnParticles(world, pos);
        }
    }

    private static void light(BlockState state, World world, BlockPos pos)
    {
        spawnParticles(world, pos);
        if (!(Boolean) state.get(LIT)) {
            world.setBlockState(pos, (BlockState) state.with(LIT, true), 3);
            world.getBlockTickScheduler().schedule(pos, state.getBlock(), 20*15);
        }
    }

    private static DustParticleEffect getDustColor()
    {
        return _dustParticles[_random.nextInt(_dustParticles.length-1)];
    }

    private static void spawnParticles(World world, BlockPos pos)
    {
        final double d = 0.5625D;
        final Random random = world.random;
        final Direction[] directions = Direction.values();

        for (int i = 0; i < directions.length; ++i) {
            final Direction direction = directions[i];
            final BlockPos blockPos = pos.offset(direction);
            if (!world.getBlockState(blockPos).isFullOpaque(world, blockPos)) {
                final Direction.Axis axis = direction.getAxis();
                final double e = axis == Direction.Axis.X ? 0.5D + d * (double) direction.getOffsetX()
                        : (double) random.nextFloat();
                final double f = axis == Direction.Axis.Y ? 0.5D + d * (double) direction.getOffsetY()
                        : (double) random.nextFloat();
                final double g = axis == Direction.Axis.Z ? 0.5D + d * (double) direction.getOffsetZ()
                        : (double) random.nextFloat();

            DustParticleEffect dust = getDustColor();
            world.addParticle(dust, (double)pos.getX() + e, (double)pos.getY() + f, (double)pos.getZ() + g, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    static {
        LIT = RedstoneTorchBlock.LIT;
    }
    
}