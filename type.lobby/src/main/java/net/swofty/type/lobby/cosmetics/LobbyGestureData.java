package net.swofty.type.lobby.cosmetics;

import lombok.Getter;

@Getter
public enum LobbyGestureData {
    CLAPPING("Clapping", CosmeticRarity.RARE, 25, null,
        "Need to congratulate someone on a Good Game, Well Played? Just activate this Clapping Gesture!"),
    COOL_DANCE("Cool Dance", CosmeticRarity.RARE, 30, null,
        "Show off your ice cold moves - Own the dancefloor by activating this Cool Dance Gesture!"),
    VICTORY_POSE("Victory Pose", CosmeticRarity.EPIC, 60, null,
        "Feeling proud? Celebrate in style by activating this Victory Pose Gesture!"),
    GOODBYE("Goodbye", CosmeticRarity.RARE, 20, null,
        "Whether it's time to go or you're just switching lobbies, bid farewell by activating this Goodbye Gesture!"),
    JUMP_AROUND("Jump Around", CosmeticRarity.LEGENDARY, 250, null,
        "Overly excited? Feeling crazy? Get rid of all that excess energy - activate this Jump Around Gesture!"),
    HYPE_DANCE("Hype Dance", CosmeticRarity.EPIC, 40, null,
        "Put your arms in the air like you just don't care! Amp yourself up by activating this Hype Dance Gesture!"),
    WAVE_DANCE("Wave Dance", CosmeticRarity.EPIC, 50, null,
        "Close your eyes, nod your head, and move to the music by activating this Wave Dance Gesture!"),
    HI_5("Hi-5", CosmeticRarity.RARE, 0, "Summer Event Shop",
        "Happy about your victory? The Hi-5 Gesture will let you show your appreciation to your mates!"),
    BALLET_DANCE("Ballet Dance", CosmeticRarity.RARE, 0, "Currently Unavailable",
        "Feel like you need to dance with style? Show your moves with the Ballet Dance Gesture!"),
    MIND_BLOWN("Mind Blown", CosmeticRarity.EPIC, 0, "Currently Unavailable",
        "You can't just believe it! This gesture will get your mind blown... Literally!"),
    CAN_CAN_DANCE("Can Can Dance", CosmeticRarity.EPIC, 0, "Currently Unavailable",
        "Dance like a total pro with this Can Can Dance Gesture! Do you think you can do it for longer than 10 minutes?"),
    KARAOKE("Karaoke", CosmeticRarity.RARE, 0, "Currently Unavailable",
        "Show your friends how it's done by singing along with this Karaoke Gesture!"),
    HULA_DANCE("Hula Dance", CosmeticRarity.EPIC, 0, "Currently Unavailable",
        "Move your hips like you are in Hawaii with this Hula Dance Gesture!"),
    GRADUATION("Graduation", CosmeticRarity.EPIC, 0, "Currently Unavailable",
        "Yayy!!! School is over and you just graduated! Share it with your friends with the Graduation Gesture!"),
    CRAB_DANCE("Crab Dance", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable",
        "If you feel like dancing... why not Crab Dance? Woop woop woop your way around the lobby with this Crab Dance Gesture!"),
    PRAISE_THE_SUN("Praise The Sun", CosmeticRarity.LEGENDARY, 0, "Currently Unavailable",
        "Feeling jolly? Celebrate Summer with this Praise the Sun Gesture! If only we could be so grossly incandescent..."),
    DIG_FOR_TREASURE("Dig For Treasure", CosmeticRarity.LEGENDARY, 0, "Reward Cards",
        "You've raided tombs, you've explored uncharted territories, and now you're ready to gather your riches!"),
    ZOMBIE_DANCE("Zombie Dance", CosmeticRarity.LEGENDARY, 0, "Halloween Event Shop",
        "When darkness falls across the land and the midnight hour is close at hand, Zombies Dance!"),
    POSSESSED("Possessed", CosmeticRarity.EPIC, 0, "Currently Unavailable",
        "Have you ever felt like you just aren't quite yourself? Give up the ghost with this Possessed Gesture!"),
    SNOWBALL_TOSS("Snowball Toss", CosmeticRarity.RARE, 0, "Holidays Event Shop",
        "Throwing snowballs is snow joke! Give someone the cold shoulder with this Winter Gesture."),
    ;

    private final String displayName;
    private final CosmeticRarity rarity;
    private final int dustCost;
    private final String specialSource;
    private final String description;

    LobbyGestureData(String displayName, CosmeticRarity rarity, int dustCost, String specialSource, String description) {
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
