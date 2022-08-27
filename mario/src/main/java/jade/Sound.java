package jade;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {
    private int bufferID;
    private int sourceID;
    private String filePath;

    private boolean isPlaying = false;

    public Sound(String filePath,boolean loops){
        this.filePath = filePath;

        //Allocate space
        stackPush();
        IntBuffer chanelBuffer = stackMallocInt(1);

        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer  rawAudioBuffer = stb_vorbis_decode_filename(filePath,chanelBuffer,sampleRateBuffer);

        if(rawAudioBuffer == null){
            System.out.println("could not load sound " + filePath);
            stackPop();
            stackPop();
            return;
        }

        int channels = chanelBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        stackPop();
        stackPop();

        int format = -1;
        if(channels == 1){
            format = AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        bufferID = alGenBuffers();
        alBufferData(bufferID,format,rawAudioBuffer,sampleRate);

        //generate the source
        sourceID = alGenSources();

        alSourcei(sourceID,AL_BUFFER,bufferID);
        alSourcei(sourceID,AL_LOOPING,loops ? 1 : 0);
        alSourcei(sourceID,AL_POSITION,0);
        alSourcef(sourceID,AL_GAIN,0.3f);

        //Free
        free(rawAudioBuffer);
    }

    public void delete(){
        alDeleteSources(sourceID);
        alDeleteBuffers(bufferID);
    }

    public void play(){
        int state = alGetSourcei(sourceID,AL_SOURCE_STATE);

        if(state == AL_STOPPED){
            isPlaying = false;
            alSourcei(sourceID,AL_POSITION,0);
        }

        if(!isPlaying){
            alSourcePlay(sourceID);
            isPlaying = true;
        }
    }

    public void stop(){
        if(isPlaying){
            alSourceStop(sourceID);
            isPlaying =false;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(sourceID,AL_SOURCE_STATE);

        if(state == AL_STOPPED){
            isPlaying = false;
        }

        return isPlaying;
    }
}
