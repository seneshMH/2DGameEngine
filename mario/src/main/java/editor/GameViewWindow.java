package editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import jade.Window;

public class GameViewWindow {

    public static void imGui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getLargestSizeViewport();
        ImVec2 windowPos = getCenterPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x,windowPos.y);
        int textureID = Window.getFrameBuffer().getTextureID();
        ImGui.image(textureID,windowSize.x,windowSize.y,0,1,1,0);

        ImGui.end();
    }

    private static ImVec2 getLargestSizeViewport(){

        ImVec2 windowSize= new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = windowSize.y /Window.getTargetAspectRatio();

        if(aspectHeight > windowSize.y){
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth,aspectHeight);
    }

    private static ImVec2 getCenterPositionForViewport(ImVec2 aspectSize){
        ImVec2 windowSize= new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(),viewportY + ImGui.getCursorPosY());
    }
}
