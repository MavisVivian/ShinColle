package com.lulan.shincolle.item;

import com.lulan.shincolle.client.gui.inventory.ContainerRecipePaper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

/**
 * Recipe Paper - stores crafting recipes for the shipyard.
 */
public class RecipePaper extends BasicItem {

    public RecipePaper() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            int heldSlot = hand == InteractionHand.MAIN_HAND ? player.getInventory().selected : 40;
            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                    (containerId, inventory, p) -> new ContainerRecipePaper(containerId, inventory, heldSlot),
                    Component.translatable("item.shincolle.recipe_paper")),
                    buf -> buf.writeInt(heldSlot));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        if (stack.hasTag()) {
            CompoundTag nbt = stack.getTag();
            assert nbt != null;
            ListTag tagList = nbt.getList("Recipe", Tag.TAG_COMPOUND);

            if (tagList.size() > 0) {
                ItemStack[] stacks = new ItemStack[10];

                for (int i = 0; i < tagList.size(); i++) {
                    CompoundTag itemTags = tagList.getCompound(i);
                    int slot = itemTags.getInt("Slot");

                    if (slot >= 0 && slot < 10) {
                        stacks[slot] = ItemStack.of(itemTags);
                    }
                }

                // Show result stack
                if (stacks[9] != null && !stacks[9].isEmpty()) {
                    tooltip.add(Component.literal(ChatFormatting.YELLOW
                            + Component.translatable("gui.shincolle.recipepaper.result").getString()
                            + " " + ChatFormatting.WHITE + stacks[9].getHoverName().getString()));
                }

                tooltip.add(Component.literal(ChatFormatting.AQUA
                        + Component.translatable("gui.shincolle.recipepaper.material").getString()));

                // Show material stacks
                for (int i = 0; i < 9; i++) {
                    if (stacks[i] != null && !stacks[i].isEmpty()) {
                        tooltip.add(
                                Component.literal(ChatFormatting.GRAY + "  " + stacks[i].getHoverName().getString()));
                    }
                }
            }
        }
    }
}
