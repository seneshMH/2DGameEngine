package components;

import jade.KeyListener;
import jade.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class GizmoSystem extends Component{
    private SpriteSheet gizmo;
    private int usingGizmo = 0;

    public GizmoSystem(SpriteSheet gizmoSprite){
        gizmo = gizmoSprite;
    }

    @Override
    public void start(){
        gameObject.addComponent(new TranslateGizmo(gizmo.getSprite(1),
                Window.getImGuiLayer().getPropertiesWindow()));
        gameObject.addComponent(new ScaleGizmo(gizmo.getSprite(2),
                Window.getImGuiLayer().getPropertiesWindow()));
    }

    @Override
    public void editorUpdate(float dt){
        if (usingGizmo == 0){
            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
        } else if (usingGizmo == 1) {
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setUsing();
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_E)){
            usingGizmo = 0;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_R)) {
            usingGizmo = 1;
        }
    }
}
