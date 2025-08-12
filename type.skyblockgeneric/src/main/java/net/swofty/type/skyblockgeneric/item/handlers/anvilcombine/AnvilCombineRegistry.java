package net.swofty.type.skyblockgeneric.item.handlers.anvilcombine;

import net.swofty.commons.item.PotatoType;
import net.swofty.commons.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import net.swofty.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.EnchantableComponent;
import net.swofty.type.skyblockgeneric.item.components.HotPotatoableComponent;
import SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnvilCombineRegistry {
    private static final Map<String, AnvilCombineHandler> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("HOT_POTATO_BOOK", new AnvilCombineHandler(
                (upgradeItem, sacrificeItem) -> {
                    HotPotatoableComponent hotPotatoable = upgradeItem.getComponent(HotPotatoableComponent.class);
                    PotatoType potatoType = hotPotatoable.getPotatoType();

                    var type = sacrificeItem.getAttributeHandler().getPotentialType();

                    ItemAttributeHotPotatoBookData.HotPotatoBookData upgradeData = upgradeItem.getAttributeHandler().getHotPotatoBookData();
                    upgradeData.addAmount(type, 1);
                    upgradeData.setPotatoType(potatoType);
                    upgradeItem.getAttributeHandler().setHotPotatoBookData(upgradeData);
                },
                (player, upgradeItem, sacrificeItem) -> {
                    if (upgradeItem.hasComponent(HotPotatoableComponent.class)) {
                        System.out.println(upgradeItem.getAttributeHandler().getPotentialType());

                        var type = sacrificeItem.getAttributeHandler().getPotentialType();

                        HotPotatoableComponent hotPotatoable = upgradeItem.getComponent(HotPotatoableComponent.class);
                        ItemAttributeHotPotatoBookData.HotPotatoBookData upgradeData = upgradeItem.getAttributeHandler().getHotPotatoBookData();
                        return hotPotatoable.canApply(type) && upgradeData.getAmount(type) < hotPotatoable.getMax(type);
                    }
                    return false;
                },
                (SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, SkyBlockPlayer player) -> 0
        ));
        register("ENCHANTED_BOOK", new AnvilCombineHandler(
                (upgradeItem, sacrificeItem) -> {
                    // Remove existing enchantments
                    List<SkyBlockEnchantment> enchantments = sacrificeItem.getAttributeHandler().getEnchantments().toList();
                    enchantments.forEach(enchantment -> upgradeItem.getAttributeHandler().removeEnchantment(enchantment.type()));

                    // Add new enchantments
                    enchantments.forEach(enchantment -> {
                        upgradeItem.getAttributeHandler().addEnchantment(new SkyBlockEnchantment(
                                enchantment.type(),
                                enchantment.level()));
                    });
                },
                ((player, upgradeItem, sacrificeItem) -> {
                    if (upgradeItem.hasComponent(EnchantableComponent.class)) {
                        EnchantableComponent enchantable = upgradeItem.getComponent(EnchantableComponent.class);
                        List<SkyBlockEnchantment> enchantments = sacrificeItem.getAttributeHandler().getEnchantments().toList();
                        Set<EnchantItemGroups> sourceTypes = enchantments.stream()
                                .flatMap(enchantment -> enchantment.type().getEnch().getGroups().stream()).collect(Collectors.toSet());

                        List<EnchantItemGroups> applicableTypes = enchantable.getEnchantItemGroups();
                        return sourceTypes.stream().anyMatch(applicableTypes::contains);
                    }
                    return false;
                }),
                (SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, SkyBlockPlayer player) -> {
                    List<SkyBlockEnchantment> enchantments = sacrificeItem.getAttributeHandler().getEnchantments().toList();
                    return enchantments.stream()
                            .mapToInt(enchant -> enchant.type().getApplyCost(enchant.level(), player))
                            .sum();
                }
        ));
    }

    public static void register(String id, AnvilCombineHandler handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static AnvilCombineHandler getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}
