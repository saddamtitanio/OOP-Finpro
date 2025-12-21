package com.finpro.frontend.factory;

import com.badlogic.gdx.math.Vector2;
import com.finpro.frontend.enemies.BaseZombie;

import java.util.*;

public class ZombieFactory {

    public interface ZombieCreator {
        BaseZombie create(float x, float y);
        void release(BaseZombie zombie);
        void releaseAll();
        List<? extends BaseZombie> getInUse();
        boolean supports(BaseZombie zombie);
        String getName();
    }

    private final Map<String, ZombieCreator> creators = new HashMap<>();
    private final List<ZombieCreator> weightedSelection = new ArrayList<>();
    private final Random random = new Random();

    public ZombieFactory(){
        register(new FastZombieCreator());
        register(new JumpingZombieCreator());
        register(new BasicZombieCreator());
    }

    private void register(ZombieCreator creator) {
        creators.put(creator.getName(), creator);
    }

    public void setWeights(Map<String, Integer> weights) {
        weightedSelection.clear();
        for (Map.Entry<String, Integer> entry : weights.entrySet()) {
            String name = entry.getKey();
            int weight = entry.getValue();
            if (creators.containsKey(name)) {
                for (int i = 0; i < weight; i++) {
                    weightedSelection.add(creators.get(name));
                }
            }
        }
    }

    public BaseZombie createRandomZombie(float spawnX, float spawnY) {
        if (weightedSelection.isEmpty()) {
            return null;
        }

        ZombieCreator creator = selectWeightedCreator();
        return creator.create(spawnX,spawnY);
    }

    private ZombieCreator selectWeightedCreator() {
        int randomIndex = random.nextInt(weightedSelection.size());
        return weightedSelection.get(randomIndex);
    }

    public void releaseZombie(BaseZombie zombie) {
        for (ZombieCreator creator : creators.values()) {
            if (creator.supports(zombie)) {
                creator.release(zombie);
                return;
            }
        }
    }

    public void releaseAllZombies() {
        for (ZombieCreator creator : creators.values()) {
            creator.releaseAll();
        }
    }

    public List<BaseZombie> getAllInUseZombies() {
        List<BaseZombie> list = new ArrayList<>();
        for (ZombieCreator creator : creators.values()) {
            list.addAll(creator.getInUse());
        }
        return list;
    }

    public List<String> getRegisteredCreatorNames() {
        return new ArrayList<>(creators.keySet());
    }

}
