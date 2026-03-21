package net.un2rws1.racemod.block;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.FallingBlockEntity;

public class Brick_Poop_Block extends FallingBlock {
    public static final MapCodec<Brick_Poop_Block> CODEC = createCodec(Brick_Poop_Block::new);
    public Brick_Poop_Block(Settings settings) {
        super(settings);
    }
    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return CODEC;
    }
    @Override
    protected void configureFallingBlockEntity(FallingBlockEntity entity) {
        entity.setHurtEntities(2.0f, 80);
    }
}
