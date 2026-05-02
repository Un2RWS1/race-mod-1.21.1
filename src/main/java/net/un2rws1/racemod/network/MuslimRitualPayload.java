package net.un2rws1.racemod.network;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MuslimRitualPayload(boolean start) implements CustomPayload {
    public static final Id<MuslimRitualPayload> ID =
            new Id<>(Identifier.of("racemod", "bomber_ritual"));

    public static final PacketCodec<PacketByteBuf, MuslimRitualPayload> CODEC =
            PacketCodec.of(
                    (value, buf) -> buf.writeBoolean(value.start()),
                    buf -> new MuslimRitualPayload(buf.readBoolean())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
