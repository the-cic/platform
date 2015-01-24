/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform;

import com.cic.platform.map.ObstacleMap;
import com.cic.platform.mob.GameCharacter;
import com.cic.platform.mob.Sprite;
import com.jme3.scene.Node;
import java.util.HashMap;

/**
 * Coordinate system, sprites, map, background, foreground..
 * 
 * @author cic
 */
public class Scene {
   
    // 0,0 is bottom left corner
    
    public float width;
    public float height;
    
    private Node sceneNode;
    private Node spriteLayer;
    private Node backgroundLayer;
    private Node foregroundLayer;
    private int spriteIdCount = 0;
    private int characterIdCount = 0;
    private HashMap<Integer, Sprite> sprites;
    private HashMap<Integer, GameCharacter> characters;
    private ObstacleMap obstacleMap;
    
    public Scene(){
        sceneNode = new Node("Scene");
        
        backgroundLayer = new Node("BackgroundLayer");
        spriteLayer = new Node("SpriteLayer");
        foregroundLayer = new Node("ForegroundLayer");
        
        backgroundLayer.setLocalTranslation(0, 0, -0.1f);
        foregroundLayer.setLocalTranslation(0, 0, +0.1f);
        
        sceneNode.attachChild(backgroundLayer);
        sceneNode.attachChild(spriteLayer);
        sceneNode.attachChild(foregroundLayer);
        
        sprites = new HashMap<Integer, Sprite>();
        characters = new HashMap<Integer, GameCharacter>();        
    }
    
    public void setMap(ObstacleMap map){
        obstacleMap = map;
    }
    
    public Node getNode(){
        return sceneNode;
    }
    
    public Integer addSprite(Sprite sprite){
        Integer id = spriteIdCount;
        spriteIdCount++;
        sprites.put(id, sprite);
        spriteLayer.attachChild(sprite);
        return id;
    }
    
    public Sprite getSprite(Integer id){
        return sprites.get(id);
    }
    
    public void removeSprite(Integer id){
        if (sprites.containsKey(id)) {
            Sprite sprite = sprites.get(id);
            sprites.remove(id);
            spriteLayer.detachChild(sprite);
        }
    }
    
    public Integer addCharacter(GameCharacter character){
        Integer id = characterIdCount;
        characterIdCount++;
        characters.put(id, character);
        spriteLayer.attachChild(character.getSprite());
        return id;
    }
    
    public void update(float tpf) {
        for (Sprite sprite : sprites.values()) {
            sprite.update(tpf);
        }
        for (GameCharacter character : characters.values()) {
            character.update(obstacleMap, tpf);
            Sprite sprite = character.getSprite();
            sprite.setLocalTranslation(character.xPos - sprite.width/2, character.yPos, 0);
        }
    }
}
