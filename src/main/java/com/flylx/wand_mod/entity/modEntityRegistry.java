package com.flylx.wand_mod.entity;

import com.flylx.wand_mod.Wand_mod;
import com.flylx.wand_mod.block.modBlockRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.example.registry.EntityRegistryBuilder;


public class modEntityRegistry {

    public static EntityType<BasicMagic> BASIC_MAGIC = buildEntity(BasicMagic::new, BasicMagic.class, 0.5F,
            0.5F, SpawnGroup.MISC);

     public static BlockEntityType WAND_TABLE = Registry.register(Registry.BLOCK_ENTITY_TYPE,
             new Identifier(Wand_mod.ModID, "wand_table_entity"),
                FabricBlockEntityTypeBuilder.create(WandTableEntity::new,modBlockRegistry.WAND_TABLE).build());


     public static final EntityType<MagicAreaCloud> MAGIC_AREA = buildEntity(MagicAreaCloud::new,MagicAreaCloud.class
             ,6.0f, 0.5f,SpawnGroup.MISC);

    public static <T extends Entity> EntityType<T> buildEntity(EntityType.EntityFactory<T> entity, Class<T> entityClass,
                                                               float width, float height, SpawnGroup group) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            String name = entityClass.getSimpleName().toLowerCase();
            return EntityRegistryBuilder.<T>createBuilder(new Identifier(Wand_mod.ModID, name)).entity(entity)
                    .category(group).dimensions(EntityDimensions.changing(width, height)).build();
        }
        return null;
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return Registry.register(Registry.ENTITY_TYPE, id, type.build(id));
    }
}
