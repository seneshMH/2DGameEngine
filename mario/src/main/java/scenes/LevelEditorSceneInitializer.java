package scenes;


import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import util.AssetPool;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;

public class LevelEditorSceneInitializer extends SceneInitializer {

    SpriteSheet sprites;
    private GameObject levelEditorStuff;


    public LevelEditorSceneInitializer(){

    }

    @Override
    public void init(Scene scene){

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png");
        SpriteSheet gizmos = AssetPool.getSpriteSheet("assets/images/gizmos.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new keyControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));

        scene.addGameObjectToScene(levelEditorStuff);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png")
                        ,16,16,81,0));

        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png")
                        ,16,16,26,0));

        AssetPool.addSpriteSheet("assets/images/items.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/items.png")
                        ,16,16,43,0));

        AssetPool.addSpriteSheet("assets/images/gizmos.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/gizmos.png"),
                        24,48,3,0));


        AssetPool.addSound("assets/sounds/main-theme-overworld.ogg", true);
        AssetPool.addSound("assets/sounds/flagpole.ogg", false);
        AssetPool.addSound("assets/sounds/break_block.ogg", false);
        AssetPool.addSound("assets/sounds/bump.ogg", false);
        AssetPool.addSound("assets/sounds/coin.ogg", false);
        AssetPool.addSound("assets/sounds/gameover.ogg", false);
        AssetPool.addSound("assets/sounds/jump-small.ogg", false);
        AssetPool.addSound("assets/sounds/mario_die.ogg", false);
        AssetPool.addSound("assets/sounds/pipe.ogg", false);
        AssetPool.addSound("assets/sounds/powerup.ogg", false);
        AssetPool.addSound("assets/sounds/powerup_appears.ogg", false);
        AssetPool.addSound("assets/sounds/stage_clear.ogg", false);
        AssetPool.addSound("assets/sounds/stomp.ogg", false);
        AssetPool.addSound("assets/sounds/kick.ogg", false);
        AssetPool.addSound("assets/sounds/invincible.ogg", false);

        for (GameObject g : scene.getGameObjects()){
            if(g.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if(spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }

            if(g.getComponent(StateMachine.class) != null){
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }

    @Override
    public void imGui(){
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imGui();
        ImGui.end();

        ImGui.begin("Objects");

        if(ImGui.beginTabBar("WindowTabBar")) {

            if(ImGui.beginTabItem("Blocks")) {

                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);

                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < sprites.size(); i++) {
                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 2;
                    float spriteHeight = sprite.getHeight() * 2;

                    int id = sprite.getTexID();
                    Vector2f[] texCoords = sprite.texCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObjects(sprite, 0.25f, 0.25f);
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }

                }
                ImGui.endTabItem();
            }

            if(ImGui.beginTabItem("prefabs")){

                SpriteSheet playerSprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 2;
                float spriteHeight = sprite.getHeight() * 2;

                int id = sprite.getTexID();
                Vector2f[] texCoords = sprite.texCoords();

                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateMario();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.sameLine();

                SpriteSheet items = AssetPool.getSpriteSheet("assets/images/items.png");
                sprite = items.getSprite(0);

                id = sprite.getTexID();
                texCoords = sprite.texCoords();

                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject object = Prefabs.generateQuestionBlock();
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.sameLine();

                ImGui.endTabItem();
            }

            if(ImGui.beginTabItem("Sounds")){
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for (Sound sound : sounds){
                    File tmp = new File(sound.getFilePath());
                    if(ImGui.button(tmp.getName())){
                        if(!sound.isPlaying()){
                            sound.play();
                        }else{
                            sound.stop();
                        }
                    }
                    if (ImGui.getContentRegionAvailX() > 100){
                        ImGui.sameLine();
                    }
                }

                ImGui.endTabItem();

            }


            ImGui.endTabBar();
        }

        ImGui.end();
    }


}
