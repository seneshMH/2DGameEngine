package editor;

import components.NoPickable;
import imgui.ImGui;
import jade.GameObject;
import jade.MouseListener;
import physics2D.components.Box2DCollider;
import physics2D.components.CircleCollider;
import physics2D.components.RigidBody2D;
import renderer.PickingTexture;
import scenes.Scene;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private List<GameObject> activeGameObjects;
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;



    public PropertiesWindow(PickingTexture pickingTexture){
        this.activeGameObjects = new ArrayList<>();
        this.pickingTexture = pickingTexture;
    }



    public void imGui(){

        if(activeGameObjects.size() == 1 && activeGameObjects.get(0) != null){
            activeGameObject = activeGameObjects.get(0);
            ImGui.begin("Properties");

            if(ImGui.beginPopupContextWindow("ComponentAdder")){
                if(ImGui.menuItem("Add RigidBody")){
                    if(activeGameObject.getComponent(RigidBody2D.class) == null){
                        activeGameObject.addComponent(new RigidBody2D());
                    }
                }

                if(ImGui.menuItem("Add Box Collider")){
                    if(activeGameObject.getComponent(Box2DCollider.class) == null &&
                            activeGameObject.getComponent(CircleCollider.class) == null){
                        activeGameObject.addComponent(new Box2DCollider());
                    }
                }

                if(ImGui.menuItem("Add Circle Collider")){
                    if(activeGameObject.getComponent(CircleCollider.class) == null &&
                            activeGameObject.getComponent(Box2DCollider.class) == null){
                        activeGameObject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();

            }

            activeGameObject.imGui();
            ImGui.end();
        }

    }

    public GameObject getActiveGameObject(){
        return activeGameObjects.size() == 1 ? this.activeGameObjects.get(0) : null;
    }

    public List<GameObject> getActiveGameObjects(){
        return this.activeGameObjects;
    }

    public void clearSelected(){
        this.activeGameObjects.clear();
    }

    public void setActiveGameObject(GameObject go){
        if(go != null){
            clearSelected();
            this.activeGameObjects.add(go);
        }

    }

    public void addActiveGameObject(GameObject go){
        this.activeGameObjects.add(go);
    }

    public PickingTexture getPickingTexture() {
        return pickingTexture;
    }
}
