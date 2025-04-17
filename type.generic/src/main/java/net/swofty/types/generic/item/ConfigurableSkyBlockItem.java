package net.swofty.types.generic.item;


import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigurableSkyBlockItem {
    private final static Map<String, ConfigurableSkyBlockItem> CACHED_ITEMS = new HashMap<>();

    @Getter
    private final String id;
    @Getter
    private final Material material;
    @Getter
    private final List<String> lore;
    @Getter
    private final ItemStatistics defaultStatistics;

    private final Map<Class<? extends SkyBlockItemComponent>, ComponentEntry> components = new HashMap<>();
    private final Set<Class<? extends SkyBlockItemComponent>> explicitComponents = new HashSet<>();

    public ConfigurableSkyBlockItem(String id, Material material, List<String> lore,
                              Map<String, Double> statistics) {
        this.id = id;
        this.material = material;
        this.lore = lore;

        ItemStatistics.Builder builder = ItemStatistics.builder();
        for (Map.Entry<String, Double> entry : statistics.entrySet()) {
            String stat = entry.getKey();
            Double value = entry.getValue();
            builder = builder.withAdditive(ItemStatistic.valueOf(stat.toUpperCase()), value);
        }
        this.defaultStatistics = builder.build();
    }

    public void addComponent(SkyBlockItemComponent component, boolean isExplicit) {
        if (isExplicit) {
            explicitComponents.add(component.getClass());
        }

        // Process the component and all its parent classes
        processComponent(component, isExplicit ? ComponentSource.EXPLICIT : ComponentSource.INHERITED);

        // Process inherited components only if this isn't an explicit component
        // or if it's not overriding an inherited component
        if (!isExplicit || !isInheritedComponentOverridden(component.getClass())) {
            for (SkyBlockItemComponent inherited : component.getInheritedComponents()) {
                if (!explicitComponents.contains(inherited.getClass())) {
                    processComponent(inherited, ComponentSource.INHERITED);
                }
            }
        }
    }

    private void processComponent(SkyBlockItemComponent component, ComponentSource source) {
        Class<? extends SkyBlockItemComponent> componentClass = component.getClass();

        // Add only the direct component class, not its parent classes
        addComponentEntry(componentClass, component, source);

        // For inherited components, we still process their own inheritance chain
        if (source == ComponentSource.INHERITED) {
            Class<?> parentClass = componentClass.getSuperclass();
            while (parentClass != null && SkyBlockItemComponent.class.isAssignableFrom(parentClass)) {
                addComponentEntry((Class<? extends SkyBlockItemComponent>) parentClass, component, source);
                parentClass = parentClass.getSuperclass();
            }
        }
    }

    private void addComponentEntry(Class<? extends SkyBlockItemComponent> componentClass,
                                   SkyBlockItemComponent component,
                                   ComponentSource source) {
        ComponentEntry existing = components.get(componentClass);

        if (existing != null) {
            // Only throw conflict error if the components are of the same actual type
            // (not just sharing a parent class)
            if (existing.source == source && componentClass == component.getClass()) {
                throw new ComponentConflictException(String.format(
                        "Component conflict in item %s:%n" +
                                "Duplicate declaration of component type: %s%n" +
                                "Components cannot be declared multiple times with the same source (%s)",
                        id,
                        componentClass.getSimpleName(),
                        source.name().toLowerCase()
                ));
            }

            // Handle inheritance priority
            if (existing.source == ComponentSource.EXPLICIT && source == ComponentSource.INHERITED) {
                return;
            }
            if (existing.source == ComponentSource.INHERITED && source == ComponentSource.EXPLICIT) {
                components.put(componentClass, new ComponentEntry(component, source));
                return;
            }
        }

        components.put(componentClass, new ComponentEntry(component, source));
    }

    private boolean isInheritedComponentOverridden(Class<? extends SkyBlockItemComponent> componentClass) {
        return explicitComponents.stream()
                .anyMatch(explicit -> {
                    if (explicit == componentClass) return false;
                    return componentClass.isAssignableFrom(explicit);
                });
    }

    public <T extends SkyBlockItemComponent> boolean hasComponent(Class<T> componentClass) {
        return components.containsKey(componentClass) || explicitComponents.contains(componentClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends SkyBlockItemComponent> T getComponent(Class<T> componentClass) {
        ComponentEntry entry = components.get(componentClass);
        if (entry == null) {
            throw new ComponentNotFoundException(String.format(
                    "Component %s not found in item %s",
                    componentClass.getSimpleName(),
                    id
            ));
        }
        return (T) entry.component;
    }

    public void register() {
        CACHED_ITEMS.put(id, this);
    }

    public static @Nullable ConfigurableSkyBlockItem getFromID(String id) {
        return CACHED_ITEMS.get(id);
    }

    private enum ComponentSource {
        EXPLICIT,
        INHERITED
    }

    private record ComponentEntry(SkyBlockItemComponent component, ComponentSource source) {}

    public static class ComponentConflictException extends RuntimeException {
        public ComponentConflictException(String message) {
            super(message);
        }
    }

    @Override
    public String toString() {
        return "ConfigurableSkyBlockItem{" +
                "id='" + id + '\'' +
                ", components=" + components.entrySet().stream().map(componentEntry -> {
                    return componentEntry.getKey() + ":" + componentEntry.getValue().source;
                }).collect(Collectors.joining(",")) +
                '}';
    }

    public static class ComponentNotFoundException extends RuntimeException {
        public ComponentNotFoundException(String message) {
            super(message);
        }
    }
}