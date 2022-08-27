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

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture){
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene){

        debounce -= dt;

        if(!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0){
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();

            int gameObjectID = pickingTexture.readPixel(x,y);

            GameObject pickObj = currentScene.getGameObject(gameObjectID);

            if(pickObj != null && pickObj.getComponent(NoPickable.class) == null) {
                activeGameObject = currentScene.getGameObject(gameObjectID);
            } else if (pickObj == null && !MouseListener.isDragging()) {
                activeGameObject = null;
            }


            this.debounce = 0.2f;
        }


    }

    public void imGui(){

        if(activeGameObject != null){
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
        return this.activeGameObject;
    }

    public void setActiveGameObject(GameObject go){
        this.activeGameObject = go;
    }
}
