package net.swofty.type.generic.cosmetic;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class PlayerCosmeticData {
    private Map<String, Set<String>> ownedCosmetics = new HashMap<>();
    private Map<String, String> activeCosmetics = new HashMap<>();
    private long activeMorphExpiry = 0;

    public PlayerCosmeticData() {
    }

    public boolean owns(String category, String cosmeticId) {
        Set<String> owned = ownedCosmetics.get(category);
        return owned != null && owned.contains(cosmeticId);
    }

    public void unlock(String category, String cosmeticId) {
        ownedCosmetics.computeIfAbsent(category, k -> new HashSet<>()).add(cosmeticId);
    }

    public void activate(String category, String cosmeticId) {
        activeCosmetics.put(category, cosmeticId);
    }

    public void deactivate(String category) {
        activeCosmetics.remove(category);
    }

    public String getActive(String category) {
        return activeCosmetics.get(category);
    }

    public boolean isActive(String category, String cosmeticId) {
        String active = activeCosmetics.get(category);
        return active != null && active.equals(cosmeticId);
    }

    public int getOwnedCount(String category) {
        Set<String> owned = ownedCosmetics.get(category);
        return owned == null ? 0 : owned.size();
    }

    public Set<String> getOwned(String category) {
        return ownedCosmetics.getOrDefault(category, Set.of());
    }

    public void activateMorph(String morphId) {
        activeCosmetics.put("MORPHS", morphId);
        activeMorphExpiry = System.currentTimeMillis() + 300_000;
    }

    public boolean isMorphActive() {
        if (activeMorphExpiry == 0) return false;
        if (System.currentTimeMillis() >= activeMorphExpiry) {
            deactivateMorph();
            return false;
        }
        return activeCosmetics.containsKey("MORPHS");
    }

    public long getMorphRemainingMillis() {
        if (!isMorphActive()) return 0;
        return Math.max(0, activeMorphExpiry - System.currentTimeMillis());
    }

    public void deactivateMorph() {
        activeCosmetics.remove("MORPHS");
        activeMorphExpiry = 0;
    }
}
