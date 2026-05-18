package net.un2rws1.racemod.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;

public record SummonGolemPayload() implements CustomPayload {
    public static final Id<SummonGolemPayload> ID =
            new Id<>(Identifier.of(Racemod.MOD_ID, "summon_golem"));

    public static final PacketCodec<RegistryByteBuf, SummonGolemPayload> CODEC =
            PacketCodec.unit(new SummonGolemPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}