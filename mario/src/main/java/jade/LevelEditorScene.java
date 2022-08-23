package jade;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

        if(levelLoaded){
            return;
        }

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        obj1 = new GameObject("obj1" , new Transform(new Vector2f(100,100),new Vector2f(256,256)),-1);
        SpriteRenderer objSprite = new SpriteRenderer();
        objSprite.setColor(new Vector4f(1,0,0,1));
        obj1.addComponent(objSprite);
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        obj2 = new GameObject("obj2" , new Transform(new Vector2f(400,100),new Vector2f(256,256)),2);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/testimage.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);


    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheet.png")
                ,16,16,26,0));
    }

    @Override
    public void update(float dt) {

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
