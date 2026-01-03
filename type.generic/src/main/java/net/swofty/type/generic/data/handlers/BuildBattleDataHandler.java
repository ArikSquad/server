package net.swofty.type.generic.data.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.data.GameDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryLeaderboardPreferences;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import net.swofty.type.generic.user.HypixelPlayer;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BuildBattleDataHandler extends DataHandler implements GameDataHandler {
    public static final Map<UUID, BuildBattleDataHandler> buildBattleCache = new HashMap<>();

    public BuildBattleDataHandler() { super(); }
    public BuildBattleDataHandler(UUID uuid) { super(uuid); }

    @Override
    public String getHandlerId() {
        return "buildbattle";
    }

    @Override
    public Map<UUID, ? extends DataHandler> getCache() {
        return buildBattleCache;
    }

    @Override
    public DataHandler createFromDocument(UUID playerUuid, Document document) {
        BuildBattleDataHandler handler = new BuildBattleDataHandler(playerUuid);
        return handler.fromDocument(document);
    }

    @Override
    public DataHandler initWithDefaults(UUID playerUuid) {
        return initUserWithDefaultData(playerUuid);
    }

    @Override
    public DataHandler getHandler(UUID playerUuid) {
        return buildBattleCache.get(playerUuid);
    }

    @Override
    public void cacheHandler(UUID playerUuid, DataHandler handler) {
        buildBattleCache.put(playerUuid, (BuildBattleDataHandler) handler);
    }

    @Override
    public void removeFromCache(UUID playerUuid) {
        buildBattleCache.remove(playerUuid);
    }

    @Override
    public boolean hasDataInDocument(Document document) {
        if (document == null) return false;
        for (Data data : Data.values()) {
            if (document.containsKey(data.getKey())) {
                return true;
            }
        }
        return false;
    }

    public static BuildBattleDataHandler getUser(UUID uuid) {
        if (!buildBattleCache.containsKey(uuid)) throw new RuntimeException("User " + uuid + " does not exist!");
        return buildBattleCache.get(uuid);
    }

    public static @Nullable BuildBattleDataHandler getUser(HypixelPlayer player) {
        try { return getUser(player.getUuid()); } catch (Exception e) { return null; }
    }

    @Override
    public BuildBattleDataHandler fromDocument(Document document) {
        if (document == null) return initUserWithDefaultData(this.uuid);

        if (document.containsKey("_id")) {
            this.uuid = UUID.fromString(document.getString("_id"));
        }

        for (Data data : Data.values()) {
            String key = data.getKey();
            if (!document.containsKey(key)) {
                this.datapoints.put(key, data.getDefaultDatapoint().deepClone().setUser(this).setData(data));
                continue;
            }
            String jsonValue = document.getString(key);
            try {
                Datapoint<?> dp = data.getDefaultDatapoint().deepClone();
                dp.deserializeValue(jsonValue);
                this.datapoints.put(key, dp.setUser(this).setData(data));
            } catch (Exception e) {
                this.datapoints.put(key, data.getDefaultDatapoint().deepClone().setUser(this).setData(data));
                Logger.info("Issue with datapoint " + key + " for user " + this.uuid + " - defaulting");
                e.printStackTrace();
            }
        }
        return this;
    }

    public static BuildBattleDataHandler createFromDocument(Document document) {
        BuildBattleDataHandler handler = new BuildBattleDataHandler();
        return handler.fromDocument(document);
    }

    @Override
    public Document toDocument() {
        Document document = new Document();
        document.put("_owner", this.uuid.toString());
        for (Data data : Data.values()) {
            try {
                document.put(data.getKey(), getDatapoint(data.getKey()).getSerializedValue());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return document;
    }

    public Datapoint<?> get(Data datapoint) {
        Datapoint<?> dp = this.datapoints.get(datapoint.key);
        return dp != null ? dp : datapoint.defaultDatapoint;
    }

    @SuppressWarnings("unchecked")
    public <R extends Datapoint<?>> R get(Data datapoint, Class<R> type) {
        Datapoint<?> dp = this.datapoints.get(datapoint.key);
        return (R) (dp != null ? type.cast(dp) : type.cast(datapoint.defaultDatapoint));
    }

    @Override
    public void runOnLoad(HypixelPlayer player) {
        for (Data data : Data.values()) {
            if (data.onLoad != null) {
                data.onLoad.accept(player, get(data));
            }
        }
    }

    @Override
    public void runOnSave(HypixelPlayer player) {
        for (Data data : Data.values()) {
            if (data.onQuit != null) {
                Datapoint<?> produced = data.onQuit.apply(player);
                Datapoint<?> target = get(data);
                target.setFrom(produced);
            }
        }
    }

    public static BuildBattleDataHandler initUserWithDefaultData(UUID uuid) {
        BuildBattleDataHandler handler = new BuildBattleDataHandler();
        handler.uuid = uuid;
        for (Data data : Data.values()) {
            try {
                handler.datapoints.put(
                        data.getKey(),
                        data.getDefaultDatapoint().deepClone().setUser(handler).setData(data)
                );
            } catch (Exception e) {
                Logger.error("Issue with datapoint " + data.getKey() + " for user " + uuid + " - fix");
                e.printStackTrace();
            }
        }
        return handler;
    }

    public enum Data {
        ;

        @Getter
        private final String key;
        @Getter private final Class<? extends Datapoint<?>> type;
        @Getter private final Datapoint<?> defaultDatapoint;
        public final BiConsumer<HypixelPlayer, Datapoint<?>> onChange;
        public final BiConsumer<HypixelPlayer, Datapoint<?>> onLoad;
        public final Function<HypixelPlayer, Datapoint<?>> onQuit;

        Data(String key,
             Class<? extends Datapoint<?>> type,
             Datapoint<?> defaultDatapoint,
             BiConsumer<HypixelPlayer, Datapoint<?>> onChange,
             BiConsumer<HypixelPlayer, Datapoint<?>> onLoad,
             Function<HypixelPlayer, Datapoint<?>> onQuit) {
            this.key = key; this.type = type; this.defaultDatapoint = defaultDatapoint;
            this.onChange = onChange; this.onLoad = onLoad; this.onQuit = onQuit;
        }
        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint,
             BiConsumer<HypixelPlayer, Datapoint<?>> onChange, BiConsumer<HypixelPlayer, Datapoint<?>> onLoad) {
            this(key, type, defaultDatapoint, onChange, onLoad, null);
        }

        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint,
             BiConsumer<HypixelPlayer, Datapoint<?>> onChange) {
            this(key, type, defaultDatapoint, onChange, null, null);
        }

        Data(String key, Class<? extends Datapoint<?>> type, Datapoint<?> defaultDatapoint) {
            this(key, type, defaultDatapoint, null, null, null);
        }

        public static Data fromKey(String key) {
            for (Data d : values()) if (d.getKey().equals(key)) return d;
            return null;
        }
    }
}
