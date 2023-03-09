package com.flylx.wand_mod.event;

import com.flylx.wand_mod.entity.BasicMagic;
import com.flylx.wand_mod.util.IEntityDataSaver;
import net.fabricmc.loader.impl.util.log.Log;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class SwitchMagic {

    public float degree;
    private BasicMagic basicMagic;
    private LivingEntity shooter;
    public SwitchMagic(BasicMagic basicMagic,LivingEntity shooter){
        this.degree = basicMagic.degree;
        this.basicMagic = basicMagic;
        this.shooter = shooter;
    } 
    
    public void theMagic(){
        if(degree>0&&degree<60) {
            LogManager.getLogger().info("switch:" + degree);
            explosionMagic();

        }
    }


    public void explosionMagic(){
        float q = 4.0F;
        int k = MathHelper.floor(basicMagic.getX() - (double) q - 1.0D);
        int l = MathHelper.floor(basicMagic.getX() + (double) q + 1.0D);
        int t = MathHelper.floor(basicMagic.getY() - (double) q - 1.0D);
        int u = MathHelper.floor(basicMagic.getY() + (double) q + 1.0D);
        int v = MathHelper.floor(basicMagic.getZ() - (double) q - 1.0D);
        int w = MathHelper.floor(basicMagic.getZ() + (double) q + 1.0D);
        List<Entity> list = basicMagic.world.getOtherEntities(basicMagic,
                new Box((double) k, (double) t, (double) v, (double) l, (double) u, (double) w));
        Vec3d vec3d = new Vec3d(basicMagic.getX(), basicMagic.getY(), basicMagic.getZ());
        for (int x = 0; x < list.size(); ++x) {
            Entity entity = (Entity) list.get(x);
            double y = (double) (MathHelper.sqrt((float) entity.squaredDistanceTo(vec3d)) / q);
            if (y <= 1.0D) {
                if (entity instanceof LivingEntity) {
                    entity.damage(DamageSource.player((PlayerEntity) this.shooter), 20);
                }
                basicMagic.world.createExplosion(basicMagic, basicMagic.getX(), basicMagic.getBodyY(0.0625D), basicMagic.getZ(), 0.0F,
                        Explosion.DestructionType.NONE);
            }
        }
    }
    
    

}
