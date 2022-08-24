package renderer;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {

    private int fboID = 0;
    private Texture texture = null;

    public int getFboID() {
        return fboID;
    }

    public int getTextureID() {
        return texture.getTexID();
    }

    public FrameBuffer(int width, int height){
        //Generate framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,fboID);

        //Create the texture to render the data and attach it to frame buffer
        this.texture = new Texture(width,height);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,this.texture.getTexID(),0);

        //Create render buffer for store depth info
        int rboId = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER,rboId);
        glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH_COMPONENT32,width,height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER,rboId);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            assert false:"ERROR : Frame buffer not complete";
        }

        glBindFramebuffer(GL_FRAMEBUFFER,0);


    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER,fboID);
    }

    public void unBind(){
        glBindFramebuffer(GL_FRAMEBUFFER,0);
    }
}
