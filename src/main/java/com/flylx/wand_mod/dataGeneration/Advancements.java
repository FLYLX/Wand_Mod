package com.flylx.wand_mod.dataGeneration;

import com.flylx.wand_mod.item.modItemRegistry;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class Advancements implements Consumer<Consumer<Advancement>> {

    @Override
    public void accept(Consumer<Advancement> consumer) {
        Advancement rootAdvancement = Advancement.Builder.create()
                .display(
                        modItemRegistry.MAGIC_DUST, // 显示的图标
                        Text.literal("启程之路！"), // 标题
                        Text.literal("法师的开始！"), // 描述
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"), // 使用的背景图片
                        AdvancementFrame.TASK, // 选项: TASK, CHALLENGE, GOAL
                        true, // 在右上角显示
                        true, // 在聊天框中提示
                        false // 在进度页面里隐藏
                )
                // Criterion 中使用的第一个字符串是其他进度在需要 'requirements' 时引用的名字
                .criterion("got_magic", InventoryChangedCriterion.Conditions.items(modItemRegistry.MAGIC_DUST))
                .build(consumer, "wand_mod" + "/root");
    }
}