package com.finpro.frontend.manager;

package com.finpro.frontend.manager;

import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class TileManager {

    private final int tileSize;
    private TiledMap map;
    private TiledMapTileLayer collisionLayer;
    private TiledMapTileLayer spawnLayer;

    public TileManager(int tileSize) {
        this.tileSize = tileSize;
    }

    public void load(String mapPath) {
        map = new TmxMapLoader().load(mapPath);

        collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
        spawnLayer = (TiledMapTileLayer) map.getLayers().get("spawn");
    }

    public int worldToTileX(float x) { return (int)(x / tileSize); }
    public int worldToTileY(float y) { return (int)(y / tileSize); }

    public float tileToWorldX(int tx) { return tx * tileSize; }
    public float tileToWorldY(int ty) { return ty * tileSize; }

    public boolean isBlocked(float worldX, float worldY) {
        int tx = worldToTileX(worldX);
        int ty = worldToTileY(worldY);

        TiledMapTileLayer.Cell cell = collisionLayer.getCell(tx, ty);
        if (cell == null) return false;

        return cell.getTile().getProperties().containsKey("blocked");
    }

    public List<Vector2> getZombieSpawnPoints() {
        List<Vector2> spawns = new ArrayList<>();

        for (int x = 0; x < spawnLayer.getWidth(); x++) {
            for (int y = 0; y < spawnLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = spawnLayer.getCell(x, y);

                if (cell != null && cell.getTile().getProperties().containsKey("zombie_spawn")) {
                    spawns.add(new Vector2(tileToWorldX(x), tileToWorldY(y)));
                }
            }
        }

        return spawns;
    }

    public TiledMap getMap() {
        return map;
    }
}

