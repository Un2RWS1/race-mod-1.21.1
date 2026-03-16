package net.un2rws1.racemod.classsystem;

import java.util.Arrays;
import java.util.Optional;

public enum PlayerClass {
    WHITE("white", "White", ""),
    BLACK("black", "Black", ""),
    JEW("jew", "Jew", ""),
    INDIAN("indian", "Indian", ""),
    CHINESE("chinese", "Chinese", ""),
    MEXICAN("mexican", "Mexican", "");

    private final String id;
    private final String displayName;
    private final String description;

    PlayerClass(String id, String displayName, String description) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }

    public static Optional<PlayerClass> byId(String id) {
        return Arrays.stream(values())
                .filter(c -> c.id.equals(id))
                .findFirst();
    }}
