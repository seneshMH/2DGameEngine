package jade;


import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene{

    GameObject obj1;
    GameObject obj2;
    SpriteSheet sprites;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        loadResources();
        this.camera = new Camera(new Vector2f());

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        obj1 = new GameObject("obj1" , new Transform(new Vector2f(100,100),new Vector2f(256,256)),-1);
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        obj2 = new GameObject("obj2" , new Transform(new Vector2f(400,100),new Vector2f(256,256)),2);
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(10)));
        this.addGameObjectToScene(obj2);



    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png")
                ,16,16,26,0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {

        spriteFlipTimeLeft -= dt;
        if(spriteFlipTimeLeft <= 0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex ++;

            if(spriteIndex > 4){
                spriteIndex = 0;
            }

            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        }


        for (GameObject go : this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imGui(){
        ImGui.begin("Test");
        ImGui.text("test text");
        ImGui.end();
    }


}
