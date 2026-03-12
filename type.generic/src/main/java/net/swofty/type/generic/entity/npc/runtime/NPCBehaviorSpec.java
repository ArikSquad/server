package net.swofty.type.generic.entity.npc.runtime;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record NPCBehaviorSpec(
        Map<String, NPCRoute> routes,
        Map<String, NPCSequence> sequences,
        NPCLoadout defaultLoadout
) {
    private static final NPCBehaviorSpec NONE = new NPCBehaviorSpec(Map.of(), Map.of(), NPCLoadout.EMPTY);

    public NPCBehaviorSpec {
        routes = copyById(routes, NPCRoute::id);
        sequences = copyById(sequences, NPCSequence::id);
        defaultLoadout = defaultLoadout == null ? NPCLoadout.EMPTY : defaultLoadout;
    }

    private static <T> Map<String, T> copyById(Map<String, T> input, Function<T, String> idExtractor) {
        if (input == null || input.isEmpty()) {
            return Map.of();
        }
        return input.values().stream().collect(Collectors.toUnmodifiableMap(idExtractor, Function.identity()));
    }

    public static NPCBehaviorSpec none() {
        return NONE;
    }
}
