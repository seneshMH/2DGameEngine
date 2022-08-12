package jade;

import static java.awt.event.KeyEvent.VK_SPACE;

public class LevelEditorScene extends Scene{

    public LevelEditorScene(){

    }

    @Override
    public void update(float dt) {
        System.out.println(" " + (1.0f / dt) + "FPS");
    }


}
