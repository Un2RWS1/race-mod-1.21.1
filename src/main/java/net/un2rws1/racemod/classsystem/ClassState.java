package net.un2rws1.racemod.classsystem;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

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

}

