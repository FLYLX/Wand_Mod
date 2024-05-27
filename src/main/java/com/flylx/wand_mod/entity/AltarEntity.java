package com.flylx.wand_mod.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class AltarEntity extends BlockEntity {

    public ItemStack content = Items.AIR.getDefaultStack();

    public AltarEntity(BlockPos pos, BlockState state) {
        super(modEntityRegistry.ALTAR, pos, state);

    }


    public void setContent(ItemStack itemStack){
        this.content = itemStack;
    }

    public ItemStack getContent(){

        return this.content;
    }

    public boolean isEmpty(){
        if(this.content!=Items.AIR.getDefaultStack()){
            return false;
        }else{
            return true;
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!isEmpty()) {
            nbt.put("Item", this.content.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setContent(ItemStack.fromNbt(nbt.getCompound("Item")));
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }


}
