package jade;

import components.*;
import util.AssetPool;

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

    public static GameObject generateMario(){
        SpriteSheet playerSprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
        GameObject mario = generateSpriteObjects(playerSprites.getSprite(0),0.25f,0.25f);

        AnimationState run = new AnimationState();
        run.title = "run";
        float defaultFrameTime = 0.23f;
        run.addFrame(playerSprites.getSprite(0),defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2),defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3),defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2),defaultFrameTime);
        run.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);
        mario.addComponent(stateMachine);


        return mario;
    }

    public static GameObject generateQuestionBlock(){
        SpriteSheet items = AssetPool.getSpriteSheet("assets/images/items.png");
        GameObject question = generateSpriteObjects(items.getSprite(0),0.25f,0.25f);

        AnimationState run = new AnimationState();
        run.title = "flicker";
        float defaultFrameTime = 0.23f;
        run.addFrame(items.getSprite(0),0.57f);
        run.addFrame(items.getSprite(1),defaultFrameTime);
        run.addFrame(items.getSprite(2),defaultFrameTime);
        run.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);
        question.addComponent(stateMachine);


        return question;
    }
}
