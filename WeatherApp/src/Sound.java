import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
    Clip clip;
    boolean muted = false;
    FloatControl fc;
    float previousVolume;
    float currentVolume;
    public void setSound(URL fileName){
        try{
            AudioInputStream sound = AudioSystem.getAudioInputStream(fileName);
            clip = AudioSystem.getClip();
            clip.open(sound);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void play(){
        if(!muted){
            clip.start();
        }

    }
    public void stop(){
        clip.stop();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void mute(){
        if(muted){
            //Unmuting
            currentVolume = previousVolume;
            fc.setValue(currentVolume);
            muted = false;
        }
        else{
            //muting
            previousVolume = fc.getValue();
            currentVolume = -80.0f;
            fc.setValue(currentVolume);
            muted = true;
        }

    }
}
