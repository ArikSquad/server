package net.swofty.type.lobby.cosmetics;

import lombok.Getter;

@Getter
public enum LobbyEmoteData {
    CRY("Cry", CosmeticRarity.RARE, 36, null,
        "Sad you can't play Hypixel all the time? Shed tears with this animated Crying Emote!"),
    SLEEPY("Sleepy", CosmeticRarity.RARE, 30, null,
        "Stayed up all night playing your favorite minigame? Fall asleep with this animated Sleepy Emote!"),
    RAGE("Rage", CosmeticRarity.RARE, 45, null,
        "Is something boiling your blood? Let off steam with this animated Rage Emote!"),
    FROWN("Frown", CosmeticRarity.RARE, 30, null,
        "Has something got you down? Let the world know with this animated Frown Emote!"),
    SMILE("Smile", CosmeticRarity.COMMON, 15, null,
        "Feeling great? Show the world how happy you are with this animated Smile Emote!"),
    CHEEKY("Cheeky", CosmeticRarity.RARE, 30, null,
        "Up to some mischief? Warn everyone how you're feeling with this animated Cheeky Emote!"),
    COOL("Cool", CosmeticRarity.COMMON, 18, null,
        "Feel like the coolest cat on the server? Strut your stuff with this animated Cool Emote!"),
    WINK("Wink", CosmeticRarity.COMMON, 18, null,
        "Having fun? Let someone know you're in on the joke with this animated Wink Emote!"),
    GRIN("Grin", CosmeticRarity.COMMON, 20, null,
        "If you are feeling amazing, there's no better way to show it than with this animated Grin Emote!"),
    SURPRISED("Surprised", CosmeticRarity.COMMON, 25, null,
        "Has something shocked you!? Let the lobby know with this animated Surprised Emote!"),
    SUN_TAN("Sun Tan", CosmeticRarity.RARE, 0, "Summer Event Shop",
        "Did you forget your sun screen when you went to the beach? Let others know how crispy you are with this Sun Tan Emote!"),
    HEART_EYES("Heart Eyes", CosmeticRarity.RARE, 0, "Currently Unavailable",
        "Feeling good? Spread the good vibes with the Heart Eyes Emote!"),
    DIZZY("Dizzy", CosmeticRarity.RARE, 0, "Currently Unavailable",
        "The rollercoaster in the main lobby is not for you? Show others how you feel with the Dizzy Emote!"),
    RIP("RIP", CosmeticRarity.RARE, 0, "Currently Unavailable",
        "Did you get that final blow? Show mercy to your opponents with this Rest in Pepperoni Emote!"),
    RELAX("Relax", CosmeticRarity.RARE, 0, "Currently Unavailable",
        "Feeling Cozy? You surely will with the Relax Emote!"),
    SPICY("Spicy", CosmeticRarity.RARE, 0, "Currently Unavailable",
        "Did that habanero pepper get to you? Let the rest know with this Spicy Emote!"),
    DEAL_WITH_IT("Deal With It", CosmeticRarity.RARE, 0, "Currently Unavailable",
        "Do you feel like showing off? Let others see how cool you feel with the real Deal With It Emote!"),
    MOUSTACHE("Moustache", CosmeticRarity.LEGENDARY, 0, "Reward Cards",
        "Moustache to Goatee to Epic Beard! Show off how fetching you are with this gentlemanly Moustache Emote!"),
    FACE_MELTER("Face Melter", CosmeticRarity.EPIC, 0, "Halloween Event Shop",
        "If you stare for too long into the darkness, the darkness might just stare back into you!"),
    PRESENT_UNWRAP("Present Unwrap", CosmeticRarity.LEGENDARY, 0, "Holidays Event Shop",
        "Give your friends the best gift they could receive this holiday season: YOU! Just activate this Present Unwrap Emote!"),
    ;

    private final String displayName;
    private final CosmeticRarity rarity;
    private final int dustCost;
    private final String specialSource;
    private final String description;

    LobbyEmoteData(String displayName, CosmeticRarity rarity, int dustCost, String specialSource, String description) {
        this.displayName = displayName;
        this.rarity = rarity;
        this.dustCost = dustCost;
        this.specialSource = specialSource;
        this.description = description;
    }

    public boolean isPurchasableWithDust() {
        return dustCost > 0;
    }

    public String getCostDisplay() {
        if (dustCost > 0) return dustCost + " Mystery Dust";
        if (specialSource != null) return specialSource;
        return "Currently Unavailable";
    }
}
