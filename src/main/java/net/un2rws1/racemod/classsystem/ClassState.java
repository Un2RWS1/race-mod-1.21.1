package net.un2rws1.racemod.classsystem;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ClassState
{
    //tick counter
    private int poopTickTimer = 0;
    public int getPoopTickTimer() {
        return poopTickTimer;
    }
    public void setPoopTickTimer(int value) {
        this.poopTickTimer = value;
    }
    private String selectedClassId; // null if none selected
    public boolean hasChosenClass() {
        return selectedClassId != null && !selectedClassId.isBlank();
    }
    public @Nullable String getSelectedClassId() {
        return selectedClassId;
    }
    public void setSelectedClassId(@Nullable String selectedClassId) {
        this.selectedClassId = selectedClassId;
    }
    public void clear() {
        this.selectedClassId = null;
    }
    public static final Codec<ClassState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("selectedClassId", "").forGetter(state ->
                    state.selectedClassId == null ? "" : state.selectedClassId
            )
    ).apply(instance, id -> {
        ClassState state = new ClassState();
        if (!id.isEmpty()) {
            state.setSelectedClassId(id);
        }
        return state;
    }));
    //=======================================stealing=============================
    private long lastStealTime = 0L;

    private UUID stealTargetUuid = null;
    private long stealStartTick = -1L;
    private BlockPos stealTargetStartPos = null;
    private int stealTargetEntityId = -1;

    public int getStealTargetEntityId() {
        return stealTargetEntityId;
    }

    public void setStealTargetEntityId(int stealTargetEntityId) {
        this.stealTargetEntityId = stealTargetEntityId;
    }

    public long getLastStealTime() {
        return lastStealTime;
    }

    public void setLastStealTime(long lastStealTime) {
        this.lastStealTime = lastStealTime;
    }

    public long getStealStartTick() {
        return stealStartTick;
    }

    public void setStealStartTick(long stealStartTick) {
        this.stealStartTick = stealStartTick;
    }

    public BlockPos getStealTargetStartPos() {
        return stealTargetStartPos;
    }

    public void setStealTargetStartPos(BlockPos stealTargetStartPos) {
        this.stealTargetStartPos = stealTargetStartPos;
    }

    public void clearStealAttempt() {
        this.stealTargetEntityId = -1;
        this.stealStartTick = -1L;
        this.stealTargetStartPos = null;
    }
    // ========================================== Interest rates==========================
    private long lastJewsInterestDay = -1L;
    public long getLastJewsInterestDay() {
        return lastJewsInterestDay;
    }
    public void setLastJewsIntersetDay(long day) {
        this.lastJewsInterestDay = day;
    }

    //NBTS
    public NbtCompound writeNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putLong("lastJewInterestDay", lastJewsInterestDay);
        nbt.putLong("LastChineseOverhealDecayTime", lastChineseOverhealDecayTime);
        //stealing
        nbt.putString("SelectedClassId", selectedClassId);
        nbt.putLong("LastStealTime", lastStealTime);
        if (stealTargetUuid != null) {
            nbt.putUuid("StealTargetUuid", stealTargetUuid);
        }
        nbt.putLong("StealStartTick", stealStartTick);
        if (stealTargetStartPos != null) {
            nbt.putInt("StealTargetX", stealTargetStartPos.getX());
            nbt.putInt("StealTargetY", stealTargetStartPos.getY());
            nbt.putInt("StealTargetZ", stealTargetStartPos.getZ());
        }
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("lastJewInterestDay")) {
            lastJewsInterestDay = nbt.getLong("lastJewInterestDay");
            lastChineseOverhealDecayTime = nbt.getLong("LastChineseOverhealDecayTime");
        }
        // stealing
        selectedClassId = nbt.getString("SelectedClassId");
        lastStealTime = nbt.getLong("LastStealTime");
        if (nbt.containsUuid("StealTargetUuid")) {
            stealTargetUuid = nbt.getUuid("StealTargetUuid");
        } else {
            stealTargetUuid = null;
        }
        stealStartTick = nbt.getLong("StealStartTick");
        if (nbt.contains("StealTargetX")) {
            stealTargetStartPos = new BlockPos(
                    nbt.getInt("StealTargetX"),
                    nbt.getInt("StealTargetY"),
                    nbt.getInt("StealTargetZ")
            );
        } else {
            stealTargetStartPos = null;
        }
    }
    //================================overheal chinese====================================
    public long lastChineseOverhealDecayTime = 0L;

}

