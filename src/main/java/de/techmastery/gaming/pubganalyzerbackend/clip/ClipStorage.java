package de.techmastery.gaming.pubganalyzerbackend.clip;

import java.util.*;

public class ClipStorage {

    private final Map<ClipIdentifier, List<Clip>> storage = new HashMap<>();

    public List<Clip> get(ClipIdentifier id) {
        if (!storage.containsKey(id) || storage.get(id) == null) {
            return new ArrayList<>();
        }
        return storage.get(id);
    }

    public void put(ClipIdentifier id, Clip c) {
        if (!storage.containsKey(id)) {
            storage.put(id, new ArrayList<>());
        }
        storage.get(id).add(c);
    }
}
