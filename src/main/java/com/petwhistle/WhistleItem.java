package com.petwhistle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class WhistleItem extends Item {
    public WhistleItem(Properties properties) { super(properties); }
    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.success(stack);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int mode = Math.floorMod(tag.getInt("PetWhistleMode"), 4);
        String[] names = {"All pets", "Wolves", "Cats", "Parrots"};
        if (player.isShiftKeyDown()) {
            mode = (mode + 1) % 4; tag.putInt("PetWhistleMode", mode);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            player.displayClientMessage(Component.literal("Pet Whistle: " + names[mode]), true);
            return InteractionResultHolder.success(stack);
        }
        int count = 0;
        for (TamableAnimal pet : level.getEntitiesOfClass(TamableAnimal.class, player.getBoundingBox().inflate(Config.range))) {
            if (!pet.isTame() || !player.getUUID().equals(pet.getOwnerUUID()) || pet.isPassenger() || pet.isLeashed()) continue;
            if (!(pet instanceof Wolf || pet instanceof Cat || pet instanceof Parrot)) continue;
            if (mode == 1 && !(pet instanceof Wolf) || mode == 2 && !(pet instanceof Cat) || mode == 3 && !(pet instanceof Parrot)) continue;
            boolean moved = false;
            BlockPos base = player.blockPosition();
            for (int x = -2; x <= 2 && !moved; x++) for (int z = -2; z <= 2 && !moved; z++) for (int y = -1; y <= 1 && !moved; y++) {
                BlockPos p = base.offset(x,y,z);
                if (!level.hasChunkAt(p) || !level.getBlockState(p.below()).isSolidRender(level, p.below()) || !level.getFluidState(p).isEmpty() || !level.getFluidState(p.below()).isEmpty()) continue;
                if (level.getBlockState(p.below()).is(Blocks.MAGMA_BLOCK) || level.getBlockState(p.below()).is(Blocks.CAMPFIRE) || level.getBlockState(p.below()).is(Blocks.SOUL_CAMPFIRE) || level.getBlockState(p.below()).is(Blocks.CACTUS) || level.getBlockState(p.below()).is(Blocks.SWEET_BERRY_BUSH) || level.getBlockState(p).is(Blocks.FIRE) || level.getBlockState(p).is(Blocks.SOUL_FIRE) || level.getBlockState(p).is(Blocks.CACTUS) || level.getBlockState(p).is(Blocks.SWEET_BERRY_BUSH) || level.getBlockState(p.above()).is(Blocks.FIRE)) continue;
                AABB box = pet.getBoundingBox().move(p.getX()+0.5-pet.getX(), p.getY()-pet.getY(), p.getZ()+0.5-pet.getZ());
                if (!level.noCollision(pet, box) || !level.getWorldBorder().isWithinBounds(p)) continue;
                pet.teleportTo(p.getX()+0.5,p.getY(),p.getZ()+0.5); pet.getNavigation().stop(); moved = true; count++;
            }
        }
        if (count > 0) { player.getCooldowns().addCooldown(this, Config.cooldownTicks); stack.hurtAndBreak(1, (ServerLevel)level, (ServerPlayer)player, item -> player.onEquippedItemBroken(item, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND)); }
        player.displayClientMessage(Component.literal(count == 0 ? "No eligible pets found or no safe landing spot." : "Recalled " + count + " pet(s)."), true);
        return InteractionResultHolder.success(stack);
    }
}
