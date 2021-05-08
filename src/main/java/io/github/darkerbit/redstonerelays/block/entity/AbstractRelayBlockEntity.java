package io.github.darkerbit.redstonerelays.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public abstract class AbstractRelayBlockEntity extends BlockEntity {
    public AbstractRelayBlockEntity(BlockEntityType<?> type) {
        super(type);
    }
}
