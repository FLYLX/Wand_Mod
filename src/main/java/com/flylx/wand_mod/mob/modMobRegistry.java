package com.flylx.wand_mod.mob;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.item.ModItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class modMobRegistry {
    public static final EntityType<MagicGolemEntity> MAGIC_GOLEM_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(Wand_mod.ModID, "cube"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, MagicGolemEntity::new).dimensions(EntityDimensions.fixed(0.75f,
                    0.75f)).build()
    );

    public static void registryAttribute(){

        FabricDefaultAttributeRegistry.register(MAGIC_GOLEM_ENTITY,MagicGolemEntity.createMagicGolemEntity());
    }
}
