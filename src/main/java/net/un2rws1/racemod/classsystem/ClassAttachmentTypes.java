package net.un2rws1.racemod.classsystem;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;

public class ClassAttachmentTypes {
    public static final AttachmentType<ClassState> PLAYER_CLASS =
            AttachmentRegistry.create(
                    Identifier.of("yourmodid", "player_class"),
                    builder -> builder
                            .initializer(ClassState::new)
                            .persistent(ClassState.CODEC)
            );

    private ClassAttachmentTypes() {
    }

    public static void init() {

    }
}
