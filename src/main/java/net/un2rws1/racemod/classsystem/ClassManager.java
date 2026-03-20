package net.un2rws1.racemod.classsystem;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.GameMode;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.networking.OpenClassSelectionPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;



public final class ClassManager {
    private static final Identifier HEALTH_MODIFIER_ID = Identifier.of("race-mod", "class_health_bonus");
    private static final Identifier SPEED_MODIFIER_ID = Identifier.of("race-mod", "class_speed_bonus");
    private static final Identifier ASIAN_DAMAGE_MODIFIER_ID = Identifier.of("race-mod", "chinese_damage_reduction");

    private ClassManager() {
    }


    public static ClassState getState(ServerPlayerEntity player) {
        return player.getAttachedOrCreate(ClassAttachmentTypes.PLAYER_CLASS);
    }


    public static boolean hasClass(ServerPlayerEntity player) {
        return getState(player).hasChosenClass();
    }

    @Nullable
    public static PlayerClass getPlayerClass(ServerPlayerEntity player) {
        ClassState state = getState(player);

        if (!state.hasChosenClass()) {
            return null;
        }

        String classId = state.getSelectedClassId();
        if (classId == null || classId.isBlank()) {
            return null;
        }

        Optional<PlayerClass> resolved = PlayerClass.byId(classId);
        return resolved.orElse(null);
    }
    public static PlayerClass getPlayerClass(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return null;
        }

        return getPlayerClass(serverPlayer);
    }

    //buffs and all that
    //==========================kippah must be always worn=================================
    public static ItemStack createKippah() {
        ItemStack stack = new ItemStack(ModItems.KIPPAH);
        stack.set(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(false));
        return stack;
    }
    public static boolean isJew(ServerPlayerEntity player) {
        ClassState state = getState(player);
        return state != null && "jew".equals(state.getSelectedClassId());
    }
    private static void ensureKippah(ServerPlayerEntity player) {
        PlayerClass playerClass = getPlayerClass(player);
        ItemStack headStack = player.getEquippedStack(EquipmentSlot.HEAD);
        boolean isJew = playerClass == PlayerClass.JEW;

        if (isJew) {
            for (int i = 0; i < 36; i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.isOf(ModItems.KIPPAH)) {
                    player.getInventory().setStack(i, ItemStack.EMPTY);
                }
            }

            if (player.getOffHandStack().isOf(ModItems.KIPPAH)) {
                player.getInventory().offHand.set(0, ItemStack.EMPTY);
            }

            if (!headStack.isOf(ModItems.KIPPAH)) {
                player.equipStack(EquipmentSlot.HEAD, createKippah());
            } else {
                if (!headStack.contains(DataComponentTypes.UNBREAKABLE)) {
                    headStack.set(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(false));
                }
            }
        } else {
            if (headStack.isOf(ModItems.KIPPAH)) {
                player.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
            }

            for (int i = 0; i < 36; i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.isOf(ModItems.KIPPAH)) {
                    player.getInventory().setStack(i, ItemStack.EMPTY);
                }
            }

            if (player.getOffHandStack().isOf(ModItems.KIPPAH)) {
                player.getInventory().offHand.set(0, ItemStack.EMPTY);
            }
        }

        player.getInventory().markDirty();
        player.currentScreenHandler.sendContentUpdates();
    }


//==================================tickPlayer method===================================
    public static void tickPlayer(PlayerEntity player) {
        PlayerClass playerClass = getPlayerClass((ServerPlayerEntity) player);
        //==============================================effects==================================================
        if (getPlayerClass((ServerPlayerEntity) player) == PlayerClass.MEXICAN) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 220, 0, true, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 220, 0, true, false));
        }
        if (getPlayerClass((ServerPlayerEntity) player) == PlayerClass.CHINESE){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 220, 1, false, false));
        }
        if (getPlayerClass((ServerPlayerEntity) player) == PlayerClass.JEW){
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, 220, 0, false, false));
        }

        //======================================Chinese damage ============================================================
        if (playerClass == PlayerClass.CHINESE) {
            var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if (attribute != null && attribute.getModifier(ASIAN_DAMAGE_MODIFIER_ID) == null) {

                attribute.addPersistentModifier(
                        new EntityAttributeModifier(
                                ASIAN_DAMAGE_MODIFIER_ID,
                                -0.15,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        )
                );
            }
        }

        //=====================================Other abilities (testing)==========================
        // =================================================water effects for blacks and indians==========================
        if (getPlayerClass((ServerPlayerEntity) player) == PlayerClass.BLACK){
            if(player.isTouchingWaterOrRain()){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 220, 2, false, false, false));
            }
        }
        if (getPlayerClass((ServerPlayerEntity) player) == PlayerClass.INDIAN){
            if(player.isTouchingWaterOrRain()){
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 220, 3, false, false, false));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 220, 0, false, false, false));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 220, 0, false, false, false));
            }
        }
        // ================================================Poop item dropping====================================
        if (playerClass == PlayerClass.INDIAN) {
            ClassState state = getState((ServerPlayerEntity) player);
            int timer = state.getPoopTickTimer() + 1;

            if (timer >= 6000) {
                ItemStack stack = new ItemStack(ModItems.POOP, 1);

                if (!player.getInventory().insertStack(stack)) {
                    player.dropItem(new ItemStack(Items.IRON_INGOT, 1), false);
                    }
                player.sendMessage(Text.literal("You just pooped"), true);

                timer = 0; // reset
            }

            state.setPoopTickTimer(timer);
        }

        // ====================================effects immunity==========================================
        if (getPlayerClass((ServerPlayerEntity) player) == PlayerClass.INDIAN) {
            player.removeStatusEffect(StatusEffects.POISON);
        }

        //========================================FIXES BUGS OF RELOG AND CHANGING CLASS=======================
        var attribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        if (attribute != null) {

            boolean isChinese = getPlayerClass((ServerPlayerEntity) player) == PlayerClass.CHINESE;
            boolean hasModifier = attribute.getModifier(ASIAN_DAMAGE_MODIFIER_ID) != null;

            if (isChinese && !hasModifier) {
                attribute.addPersistentModifier(
                        new EntityAttributeModifier(
                                ASIAN_DAMAGE_MODIFIER_ID,
                                -0.15,
                                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        )
                );
            }

            if (!isChinese && hasModifier) {
                attribute.removeModifier(ASIAN_DAMAGE_MODIFIER_ID);
            }
        }
        //==============================================Always wearing Kippah======================================
        ensureKippah((ServerPlayerEntity) player);


    }





    public static boolean canChooseClass(ServerPlayerEntity player) {
        return !hasClass(player);
    }

    public static boolean trySelectClass(ServerPlayerEntity player, PlayerClass chosenClass) {
        if (!canChooseClass(player)) {
            player.sendMessage(Text.literal("You have already selected a Race."), false);
            player.setHealth(player.getMaxHealth());
            return false;

        }

        ClassState state = getState(player);
        state.setSelectedClassId(chosenClass.getId());

        removeClassEffects(player);
        if (chosenClass.getId().equalsIgnoreCase("jew")) {
            ensureKippah(player);
            ItemStack stack = new ItemStack(ModItems.HAVA_NAGILA_MUSIC_DISC, 1);

            if (!player.getInventory().insertStack(stack)) {
                player.dropItem(stack, false);
            }
        }
        applyClassEffects(player, chosenClass);


        player.sendMessage(Text.literal("You are now " + chosenClass.getDisplayName() ), false);
        return true;
    }

    public static void forceSetClass(ServerPlayerEntity player, PlayerClass newClass) {
        ClassState state = getState(player);
        state.setSelectedClassId(newClass.getId());

        removeClassEffects(player);
        if (newClass.getId().equalsIgnoreCase("jew")) {
            ItemStack stack = new ItemStack(ModItems.HAVA_NAGILA_MUSIC_DISC, 1);
            ensureKippah(player);
            if (!player.getInventory().insertStack(stack)) {
                player.dropItem(stack, false);
            }
        }
        applyClassEffects(player, newClass);

        player.sendMessage(Text.literal("Your race has been set to " + newClass.getDisplayName() + "."), false);
    }

    public static void clearClass(ServerPlayerEntity player) {
        ClassState state = getState(player);
        state.clear();

        removeClassEffects(player);

        player.sendMessage(Text.literal("You are now White."), false);
    }

    public static void resetAndOpenSelection(ServerPlayerEntity player) {
        clearClass(player);
        openSelection(player);
    }

    public static void openSelection(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new OpenClassSelectionPayload());
        System.out.println("[RaceMod] Sending Race selection screen to " + player.getName().getString());
        ServerPlayNetworking.send(player, new OpenClassSelectionPayload());
    }


    public static boolean isLockedUntilClassChosen(ServerPlayerEntity player) {
        return !hasClass(player);
    }


    public static void refreshCurrentClassEffects(ServerPlayerEntity player) {
        PlayerClass playerClass = getPlayerClass(player);

        float oldMaxHealth = player.getMaxHealth();
        float oldHealth = player.getHealth();
        float healthPercent = oldMaxHealth > 0.0f ? oldHealth / oldMaxHealth : 1.0f;

        removeClassEffects(player);

        if (playerClass != null) {
            applyClassEffects(player, playerClass);
        }

        float newMaxHealth = player.getMaxHealth();
        player.setHealth(Math.max(1.0f, Math.min(newMaxHealth, newMaxHealth * healthPercent)));
    }


    public static void applyClassEffects(ServerPlayerEntity player, PlayerClass playerClass) {
        switch (playerClass) {
            case WHITE -> {
                // No bonuses yet
            }

            case BLACK -> {
                EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                EntityAttributeInstance movementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

                if (maxHealth != null && maxHealth.getModifier(HEALTH_MODIFIER_ID) == null) {
                    maxHealth.addPersistentModifier(new EntityAttributeModifier(
                            HEALTH_MODIFIER_ID,
                            4,

                            EntityAttributeModifier.Operation.ADD_VALUE
                    ));
                }

                if (movementSpeed != null && movementSpeed.getModifier(SPEED_MODIFIER_ID) == null) {
                    movementSpeed.addPersistentModifier(new EntityAttributeModifier(
                            SPEED_MODIFIER_ID,
                            0.03,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    ));
                }

                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
                player.heal(4.0F);
            }

            case INDIAN -> {
                EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                EntityAttributeInstance movementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

                if (maxHealth != null && maxHealth.getModifier(HEALTH_MODIFIER_ID) == null) {
                    maxHealth.addPersistentModifier(new EntityAttributeModifier(
                            HEALTH_MODIFIER_ID,
                            -4.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    ));
                }


                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }

            case CHINESE -> {
                EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);

                if (maxHealth != null && maxHealth.getModifier(HEALTH_MODIFIER_ID) == null) {
                    maxHealth.addPersistentModifier(new EntityAttributeModifier(
                            HEALTH_MODIFIER_ID,
                            -2.0,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    ));
                }

                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
            }
            case MEXICAN -> {
                EntityAttributeInstance movementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);


                if (movementSpeed != null && movementSpeed.getModifier(SPEED_MODIFIER_ID) == null) {
                    movementSpeed.addPersistentModifier(new EntityAttributeModifier(
                            SPEED_MODIFIER_ID,
                            0.03,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    ));
                }

            }


            case JEW -> {

            }



        }
    }


    public static void removeClassEffects(ServerPlayerEntity player) {
        EntityAttributeInstance maxHealth = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        EntityAttributeInstance movementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);

        if (maxHealth != null) {
            maxHealth.removeModifier(HEALTH_MODIFIER_ID);
        }

        if (movementSpeed != null) {
            movementSpeed.removeModifier(SPEED_MODIFIER_ID);
        }

        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }




}