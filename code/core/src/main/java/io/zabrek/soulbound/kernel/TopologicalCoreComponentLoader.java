package io.zabrek.soulbound.kernel;

import io.zabrek.soulbound.api.kernel.CoreComponent;
import io.zabrek.soulbound.api.kernel.CoreComponentLoader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/**
 * Manages and loads the core components.
 *
 * @since 1.0.0
 */
public class TopologicalCoreComponentLoader implements CoreComponentLoader {

    /**
     * The ordered set of registered core components.
     */
    private final Set<CoreComponent> components = new LinkedHashSet<>();

    /**
     * The central provider.
     */
    private final DependencyProvider provider = new DependencyProvider();

    /**
     * Instances a new TopologicalCoreComponentLoader.
     */
    public TopologicalCoreComponentLoader() {
    }

    @Override
    public void register(final CoreComponent component) {
        components.add(component);
    }

    @Override
    public <T> void init(final Class<T> clazz, final T instance) {
        provider.take(clazz, instance);
    }

    @Override
    public <T> T get(final Class<T> type) {
        return provider.get(type);
    }

    @Override
    public <T> Optional<T> getOptional(final Class<T> type) {
        try {
            return Optional.ofNullable(provider.get(type));
        } catch (final NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Collection<T> getAll(final Class<T> type) {
        return provider.getAll(type);
    }

    @Override
    public void load() {
        final Map<Class<?>, CoreComponent> producerMap = buildProducerMap();

        final Map<CoreComponent, Set<CoreComponent>> graph = new HashMap<>();
        final Map<CoreComponent, Integer> inDegree = initializeGraphStructures(graph);

        buildGraphAndInDegrees(producerMap, graph, inDegree);

        final List<CoreComponent> sortedComponents = topologicalSort(graph, inDegree);
        executeComponentLoading(sortedComponents);
    }

    private Map<Class<?>, CoreComponent> buildProducerMap() {
        final Map<Class<?>, CoreComponent> producerMap = new HashMap<>();
        for (final CoreComponent comp : components) {
            for (final Class<?> provided : comp.provides()) {
                producerMap.put(provided, comp);
            }
        }
        return producerMap;
    }

    private Map<CoreComponent, Integer> initializeGraphStructures(final Map<CoreComponent, Set<CoreComponent>> graph) {
        final Map<CoreComponent, Integer> inDegree = new HashMap<>();
        for (final CoreComponent comp : components) {
            graph.putIfAbsent(comp, new HashSet<>());
            inDegree.putIfAbsent(comp, 0);
        }
        return inDegree;
    }

    private void buildGraphAndInDegrees(
            final Map<Class<?>, CoreComponent> producerMap,
            final Map<CoreComponent, Set<CoreComponent>> graph,
            final Map<CoreComponent, Integer> inDegree
    ) {
        for (final CoreComponent comp : components) {
            for (final Class<?> req : comp.requires()) {
                if (isDependencyPreProvided(req)) {
                    continue;
                }

                final CoreComponent dependencyComp = producerMap.get(req);
                if (dependencyComp == null) {
                    throw new IllegalStateException("Dependency provider " + req + " not found, but no registered component provides it.");
                }

                graph.get(dependencyComp).add(comp);
                inDegree.put(comp, inDegree.get(comp) + 1);
            }
        }
    }

    private boolean isDependencyPreProvided(final Class<?> req) {
        try {
            provider.get(req);
            return true;
        } catch (final IllegalStateException ignored) {
            return false;
        }
    }

    private List<CoreComponent> topologicalSort(
            final Map<CoreComponent, Set<CoreComponent>> graph,
            final Map<CoreComponent, Integer> inDegree
    ) {
        final Queue<CoreComponent> queue = new ArrayDeque<>();
        for (final Map.Entry<CoreComponent, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        final List<CoreComponent> sortedOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            final CoreComponent current = queue.poll();
            sortedOrder.add(current);

            for (final CoreComponent neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sortedOrder.size() != components.size()) {
            throw new IllegalStateException("A circular dependency was detected among the components of the core!");
        }

        return sortedOrder;
    }

    private void executeComponentLoading(final List<CoreComponent> sortedOrder) {
        for (final CoreComponent comp : sortedOrder) {
            comp.load(provider);
        }
    }
}
