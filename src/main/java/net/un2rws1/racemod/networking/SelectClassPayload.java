package net.un2rws1.racemod.networking;


import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SelectClassPayload(String classId) implements CustomPayload {

    public static final CustomPayload.Id<SelectClassPayload> ID =
            new CustomPayload.Id<>(Identifier.of("race-mod", "select_class"));

    public static final PacketCodec<RegistryByteBuf, SelectClassPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.STRING,
                    SelectClassPayload::classId,
                    SelectClassPayload::new
            );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
