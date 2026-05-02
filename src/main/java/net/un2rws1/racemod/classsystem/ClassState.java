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
        nbt.putInt("bomberPrayerDay", muslimPrayerDay);
        nbt.putInt("bomberWindowIndex", muslimWindowIndex);
        nbt.putLong("bomberWindowEndTick", muslimWindowEndTick);
        nbt.putBoolean("bomberRitualCompleted", muslimRitualCompleted);
        nbt.putBoolean("bomberChanneling", muslimChanneling);
        nbt.putInt("bomberStillTicks", muslimStillTicks);
        nbt.putDouble("bomberStartX", muslimStartX);
        nbt.putDouble("bomberStartY", muslimStartY);
        nbt.putDouble("bomberStartZ", muslimStartZ);
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
        muslimPrayerDay = nbt.getInt("muslimPrayerDay");
        muslimWindowIndex = nbt.getInt("muslimWindowIndex");
        muslimWindowEndTick = nbt.getLong("muslimWindowEndTick");
        muslimRitualCompleted = nbt.getBoolean("muslimRitualCompleted");
        muslimChanneling = nbt.getBoolean("muslimChanneling");
        muslimStillTicks = nbt.getInt("muslimStillTicks");
        muslimStartX = nbt.getDouble("muslimStartX");
        muslimStartY = nbt.getDouble("muslimStartY");
        muslimStartZ = nbt.getDouble("muslimStartZ");

    }
    //==========================pray==================
    private int muslimPrayerDay = -1;          // last in-game day we processed
    private int muslimWindowIndex = -1;        // which of the 5 windows is active
    private long muslimWindowEndTick = -1;     // absolute world tick when the 1-minute window ends
    private boolean muslimRitualCompleted = false;

    private boolean muslimChanneling = false;  // currently holding P ritual
    private int muslimStillTicks = 0;          // how long they have stood still
    private double muslimStartX;
    private double muslimStartY;
    private double muslimStartZ;

    public int getMuslimPrayerDay() {
        return muslimPrayerDay;
    }

    public void setMuslimPrayerDay(int bomberPrayerDay) {
        this.muslimPrayerDay = bomberPrayerDay;
    }

    public int getMuslimWindowIndex() {
        return muslimWindowIndex;
    }

    public void setMuslimWindowIndex(int bomberWindowIndex) {
        this.muslimWindowIndex = bomberWindowIndex;
    }

    public long getMuslimWindowEndTick() {
        return muslimWindowEndTick;
    }

    public void setMuslimWindowEndTick(long bomberWindowEndTick) {
        this.muslimWindowEndTick = bomberWindowEndTick;
    }

    public boolean isMuslimRitualCompleted() {
        return muslimRitualCompleted;
    }

    public void setMuslimRitualCompleted(boolean bomberRitualCompleted) {
        this.muslimRitualCompleted = bomberRitualCompleted;
    }

    public boolean isMuslimChanneling() {
        return muslimChanneling;
    }

    public void setMuslimChanneling(boolean bomberChanneling) {
        this.muslimChanneling = bomberChanneling;
    }

    public int getMuslimStillTicks() {
        return muslimStillTicks;
    }

    public void setMuslimStillTicks(int bomberStillTicks) {
        this.muslimStillTicks = bomberStillTicks;
    }

    public double getMuslimStartX() {
        return muslimStartX;
    }

    public void setMuslimStartX(double bomberStartX) {
        this.muslimStartX = bomberStartX;
    }

    public double getMuslimStartY() {
        return muslimStartY;
    }

    public void setMuslimStartY(double bomberStartY) {
        this.muslimStartY = bomberStartY;
    }

    public double getMuslimStartZ() {
        return muslimStartZ;
    }

    public void setMuslimStartZ(double bomberStartZ) {
        this.muslimStartZ = bomberStartZ;
    }

    // =============================muslims=
    private int muslimDeathTimer = 0;

    public int getMuslimDeathTimer() {
        return muslimDeathTimer;
    }

    public void setMuslimDeathTimer(int bomberDeathTimer) {
        this.muslimDeathTimer = bomberDeathTimer;
    }
    //================================overheal chinese====================================
    public long lastChineseOverhealDecayTime = 0L;

}

