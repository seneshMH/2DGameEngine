package jade;

import components.Sprite;
import components.SpriteRenderer;

public class Prefabs {

    public static GameObject generateSpriteObjects(Sprite sprite,float sizeX,float sizeY){
        GameObject block = Window.getScene().createGameObject("sprite object gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}
