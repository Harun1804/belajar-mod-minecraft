package com.example.item.custom;

import com.example.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class ChiselItem extends Item {
    private static final Map<Block, Block> CHISELABLE_BLOCKS = Map.of(
        // Add your chiselable blocks here, e.g.:
        Blocks.STONE, Blocks.STONE_BRICKS,
        Blocks.END_STONE, Blocks.END_STONE_BRICKS,
        Blocks.DIRT, Blocks.DIAMOND_BLOCK,
        Blocks.OAK_LOG, ModBlocks.PINK_GARNET_BLOCK
    );

    public ChiselItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if (CHISELABLE_BLOCKS.containsKey(clickedBlock)) {
            if (!world.isClient) {
                world.setBlockState(context.getBlockPos(), CHISELABLE_BLOCKS.get(clickedBlock).getDefaultState());

                context.getStack().damage(1, ((ServerWorld) world), ((ServerPlayerEntity) context.getPlayer()),
                        item -> {
                            assert context.getPlayer() != null;
                            context.getPlayer().sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND);
                        });

                world.playSound(null, context.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS);
            }
        }

        return ActionResult.SUCCESS;
    }

    private boolean chiselOn = false;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            chiselOn = !chiselOn;
            user.sendMessage(
                    Text.literal("Chisel " + (chiselOn ? "ON" : "OFF")),
                    true
            );
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClient && chiselOn && entity instanceof PlayerEntity player) {
            attractItemsWithAnimation(world, player);
        }
    }

    public void attractItemsWithAnimation(World world, PlayerEntity user) {
        double range = 7.0;
        List<ItemEntity> items = world.getEntitiesByClass(
                ItemEntity.class,
                user.getBoundingBox().expand(range),
                item -> item.isAlive() && item.getStack() != null
        );
        for (ItemEntity item : items) {
            double dx = user.getX() - item.getX();
            double dy = user.getY() + 1.0 - item.getY();
            double dz = user.getZ() - item.getZ();
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double speed = 0.45; // Increase speed for smoother movement
            if (dist > 0.1) {
                // Set velocity directly towards the player for smooth movement
                item.setVelocity(
                        (dx / dist) * speed,
                        (dy / dist) * speed,
                        (dz / dist) * speed
                );
            }
            if (dist < 1.5) {
                if (user.getInventory().insertStack(item.getStack().copy())) {
                    item.discard();
                    world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS);
                }
            }
        }
    }
}
