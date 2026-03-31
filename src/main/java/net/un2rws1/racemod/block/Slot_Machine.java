package net.un2rws1.racemod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.un2rws1.racemod.item.ModItems;

public class Slot_Machine extends Block {

    private static final int MAX_BET = 16;

    public Slot_Machine(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos,
                                 PlayerEntity player, BlockHitResult hit) {

        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);

        if (!stack.isOf(ModItems.GOLDEN_COINS)) {
            if (!world.isClient) {
                player.sendMessage(Text.literal("Hold Golden Coins in your main hand to play."), true);
            }
            return ActionResult.PASS;
        }

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        int coinsInHand = stack.getCount();
        int bet = Math.min(coinsInHand, MAX_BET);

        if (!player.isCreative()) {
            stack.decrement(bet);
        }

        ServerWorld serverWorld = (ServerWorld) world;
        playRollEffects(serverWorld, pos);

        SlotResult result = rollResult(serverWorld.random, bet);

        if (result.jackpot()) {
            giveCoins(player, result.payout());
            player.sendMessage(
                    Text.literal("JACKPOT! You bet " + bet + " and won " + result.payout() + " Golden Coins!"),
                    false
            );
            playJackpotEffects(serverWorld, pos);
            return ActionResult.CONSUME;
        }

        if (result.payout() <= 0) {
            player.sendMessage(
                    Text.literal("You lost " + bet + " coin" + (bet == 1 ? "" : "s") + "."),
                    false
            );
            playLoseEffects(serverWorld, pos);
            return ActionResult.CONSUME;
        }

        giveCoins(player, result.payout());
        player.sendMessage(
                Text.literal("You bet " + bet + " and won " + result.payout() + " Golden Coins!"),
                false
        );
        playWinEffects(serverWorld, pos, result.payout() >= bet * 3);

        return ActionResult.CONSUME;
    }

    private SlotResult rollResult(Random random, int bet) {
        int roll = random.nextInt(1000);

        if (roll < 500) {
            return new SlotResult(0, false);
        } else if (roll < 700) {
            int payout = Math.max(1, Math.round(bet * randomBetween(random, 0.5f, 2.0f)));
            return new SlotResult(payout, false);
        } else if (roll < 870) {
            int payout = Math.max(1, Math.round(bet * randomBetween(random, 2.0f, 2.75f)));
            return new SlotResult(payout, false);
        } else if (roll < 995) {
            int payout = Math.max(1, Math.round(bet * randomBetween(random, 2.75f, 4.5f)));
            return new SlotResult(payout, false);
        } else {
            int payout = Math.max(1, Math.round(bet * randomBetween(random, 10.0f, 50.0f)));
            return new SlotResult(payout, true);
        }
    }

    private float randomBetween(Random random, float min, float max) {
        return min + random.nextFloat() * (max - min);
    }

    private void giveCoins(PlayerEntity player, int amount) {
        ItemStack payout = new ItemStack(ModItems.GOLDEN_COINS, amount);

        boolean inserted = player.getInventory().insertStack(payout);
        if (!inserted) {
            player.dropItem(payout, false);
        }
    }

    private void playRollEffects(ServerWorld world, BlockPos pos) {
        world.playSound(
                null,
                pos,
                SoundEvents.BLOCK_NOTE_BLOCK_HAT.value(),
                SoundCategory.BLOCKS,
                1.0f,
                1.0f
        );

        world.spawnParticles(
                ParticleTypes.SMOKE,
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                6,
                0.2, 0.2, 0.2,
                0.01
        );
    }

    private void playLoseEffects(ServerWorld world, BlockPos pos) {
        world.playSound(
                null,
                pos,
                SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(),
                SoundCategory.BLOCKS,
                1.0f,
                0.7f
        );

        world.spawnParticles(
                ParticleTypes.POOF,
                pos.getX() + 0.5,
                pos.getY() + 1.0,
                pos.getZ() + 0.5,
                8,
                0.25, 0.25, 0.25,
                0.02
        );
    }

    private void playWinEffects(ServerWorld world, BlockPos pos, boolean bigWin) {
        world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                SoundCategory.BLOCKS,
                1.0f,
                bigWin ? 1.3f : 1.0f
        );

        world.playSound(
                null,
                pos,
                SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                SoundCategory.BLOCKS,
                0.8f,
                bigWin ? 1.2f : 1.0f
        );

        world.spawnParticles(
                ParticleTypes.HAPPY_VILLAGER,
                pos.getX() + 0.5,
                pos.getY() + 1.1,
                pos.getZ() + 0.5,
                bigWin ? 18 : 10,
                0.3, 0.3, 0.3,
                0.05
        );
    }

    private void playJackpotEffects(ServerWorld world, BlockPos pos) {
        world.playSound(
                null,
                pos,
                SoundEvents.UI_TOAST_CHALLENGE_COMPLETE,
                SoundCategory.BLOCKS,
                1.0f,
                1.0f
        );

        world.playSound(
                null,
                pos,
                SoundEvents.ENTITY_PLAYER_LEVELUP,
                SoundCategory.BLOCKS,
                1.0f,
                1.1f
        );

        world.spawnParticles(
                ParticleTypes.FIREWORK,
                pos.getX() + 0.5,
                pos.getY() + 1.2,
                pos.getZ() + 0.5,
                30,
                0.35, 0.4, 0.35,
                0.03
        );

        world.spawnParticles(
                ParticleTypes.HAPPY_VILLAGER,
                pos.getX() + 0.5,
                pos.getY() + 1.2,
                pos.getZ() + 0.5,
                25,
                0.4, 0.4, 0.4,
                0.08
        );
    }

    private record SlotResult(int payout, boolean jackpot) {}
}