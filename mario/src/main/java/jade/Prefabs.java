package jade;

import components.Sprite;
import components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs {

    public static GameObject generateSpriteObjects(Sprite sprite,float sizeX,float sizeY){
        GameObject block = new GameObject("sprite object gen",
                new Transform(new Vector2f(),new Vector2f(sizeX,sizeY)),0);

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}
