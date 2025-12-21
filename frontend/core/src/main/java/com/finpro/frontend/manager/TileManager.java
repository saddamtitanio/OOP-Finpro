package com.finpro.frontend.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class TileManager {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private final float unitScale;

    public TileManager(float unitScale){
        this.unitScale = unitScale;
    }

    public void load(String mapPath){
        map = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    public void render(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    public TiledMap getMap(){
        return map;
    }
    public MapLayer getLayer(String name){
        return map.getLayers().get(name);
    }
    public void dispose(){
        map.dispose();
        renderer.dispose();
    }

    public float getWorldWidth() {
        int mapWidth = map.getProperties().get("width", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        return mapWidth * tileWidth * unitScale;
    }

    public float getWorldHeight() {
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);
        return mapHeight * tileHeight * unitScale;
    }

}

