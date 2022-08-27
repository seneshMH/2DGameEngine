package util;

import components.SpriteSheet;
import jade.Sound;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    public static Shader getShader(String resourceName){
        File file = new File(resourceName);
        if (shaders.containsKey(file.getAbsolutePath())){
            return shaders.get(file.getAbsolutePath());
        }else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(),shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName){
        File file = new File(resourceName);
        if (textures.containsKey(file.getAbsolutePath())){
            return textures.get(file.getAbsolutePath());
        }else {
            Texture texture = new Texture();
            texture.init(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(),texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet){
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            AssetPool.spriteSheets.put(file.getAbsolutePath(),spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName){
        File file = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath())){
            assert false : "ERROR : Tried to access sprite sheet " + resourceName + " and it not has been added to assets pool ";
        }

        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(),null);
    }

    public static Sound getSound(String soundFile){
        File file = new File(soundFile);
        if(!AssetPool.sounds.containsKey(file.getAbsolutePath())){
            assert false : "ERROR : Tried to access sound " + soundFile + " and it not has been added to assets pool ";
        }

        return AssetPool.sounds.getOrDefault(file.getAbsolutePath(),null);
    }

    public static Collection<Sound> getAllSounds(){
        return sounds.values();
    }

    public static Sound addSound(String soundFile,boolean loops){
        File file = new File(soundFile);
        if(AssetPool.sounds.containsKey(file.getAbsolutePath())){
            return sounds.get(file.getAbsolutePath());
        }else{
            Sound sound = new Sound(file.getAbsolutePath(),loops);
            AssetPool.sounds.put(file.getAbsolutePath(),sound);
            return sound;
        }
    }
}
