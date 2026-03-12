package net.swofty.type.generic.entity.npc.runtime;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;

import java.util.EnumMap;
import java.util.Map;

public record NPCLoadout(
        ItemStack mainHand,
        ItemStack offHand,
        ItemStack helmet,
        ItemStack chestplate,
        ItemStack leggings,
        ItemStack boots
) {
    public static final NPCLoadout EMPTY = new NPCLoadout(
            ItemStack.AIR,
            ItemStack.AIR,
            ItemStack.AIR,
            ItemStack.AIR,
            ItemStack.AIR,
            ItemStack.AIR
    );

    public NPCLoadout {
        mainHand = sanitize(mainHand);
        offHand = sanitize(offHand);
        helmet = sanitize(helmet);
        chestplate = sanitize(chestplate);
        leggings = sanitize(leggings);
        boots = sanitize(boots);
    }

    private static ItemStack sanitize(ItemStack itemStack) {
        return itemStack == null ? ItemStack.AIR : itemStack;
    }

    public Map<EquipmentSlot, ItemStack> asEquipmentMap() {
        Map<EquipmentSlot, ItemStack> map = new EnumMap<>(EquipmentSlot.class);
        map.put(EquipmentSlot.MAIN_HAND, mainHand);
        map.put(EquipmentSlot.OFF_HAND, offHand);
        map.put(EquipmentSlot.HELMET, helmet);
        map.put(EquipmentSlot.CHESTPLATE, chestplate);
        map.put(EquipmentSlot.LEGGINGS, leggings);
        map.put(EquipmentSlot.BOOTS, boots);
        return map;
    }

    public boolean isEmpty() {
        return mainHand.isAir() && offHand.isAir() && helmet.isAir() && chestplate.isAir()
                && leggings.isAir() && boots.isAir();
    }
}
