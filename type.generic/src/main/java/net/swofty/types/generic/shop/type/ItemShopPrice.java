package net.swofty.types.generic.shop.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.shop.ShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemShopPrice implements ShopPrice {
    ItemType type;
    int amount;

    @Override
    public List<String> getGUIDisplay() {
        return List.of("§6" + type.rarity.getColor() + type.getDisplayName() + " §8x" + amount);
    }

    @Override
    public String getNamePlural() {
        return "configuration/items";
    }

    @Override
    public boolean canAfford(SkyBlockPlayer player) {
        return player.getAmountInInventory(type) >= amount;
    }

    @Override
    public void processPurchase(SkyBlockPlayer player) {
        player.takeItem(type, amount);
    }

    @Override
    public ShopPrice multiply(int amount) {
        return new ItemShopPrice(type, this.amount * amount);
    }

    @Override
    public ShopPrice divide(double amount) {
        return new ItemShopPrice(type, Math.max((int) (this.amount / amount), 1));
    }

}
