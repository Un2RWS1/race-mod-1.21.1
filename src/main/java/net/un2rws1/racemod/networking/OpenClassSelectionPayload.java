package net.un2rws1.racemod.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenClassSelectionPayload() implements CustomPayload {

    public static final CustomPayload.Id<OpenClassSelectionPayload> ID =
            new CustomPayload.Id<>(Identifier.of("race-mod", "open_class_selection"));

    public static final PacketCodec<RegistryByteBuf, OpenClassSelectionPayload> CODEC =
            PacketCodec.unit(new OpenClassSelectionPayload());


    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
