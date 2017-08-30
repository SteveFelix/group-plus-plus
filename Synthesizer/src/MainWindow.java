
/**
 * Created by Steven on 8/30/2017.
 */
import processing.core.PApplet;
import processing.core.PFont;

import ddf.minim.*;
import ddf.minim.signals.*;


/**
 * Created by Steven on 8/27/2017.
 */
public class MainWindow extends PApplet {

    public static void main(String[] args) {
        PApplet.main(new String[] { "--location=100,100", "MainWindow" });
    }

    /*
    Use 1-8 on your keyboard to play tones in the C major scale
    */

    Minim minim;
    AudioOutput out;
    SineWave sine;
    SineWave nullSine;

    float freq = 0;
    int keysPressed = 0;
    int port   = 30;
    static final float MAXAMP = 1.0f;
    float amp  = MAXAMP;

    String printScale;

    //c5-c6 major scale in hz
    float[] tones = {523.25f, 587.33f, 659.26f, 698.46f, 783.99f, 880.00f, 987.77f, 1046.50f};

    char keys[] =    {'1','2','3','4','5','6','7','8'};
    String notes[] = {"C","D","E","F","G","A","B","C"};
    boolean keyStates[];

    PFont font;

    @Override
    public void setup()
    {
        //size(512, 120, P2D);
        keyStates = new boolean[keys.length];
        minim = new Minim(this);

        out = minim.getLineOut(Minim.STEREO);
        nullSine= new SineWave(0, 0, out.sampleRate());
        sine = new SineWave(0, amp, out.sampleRate());
        sine.portamento(port);

        //font = loadFont("defaultFont.vlw");
        //textFont(font, 12);
    }
    @Override
    public void settings() {
        size(512, 120, "processing.opengl.PGraphics2D");
    }

    //determine which keys have been pressed
    @Override
    public void keyPressed()
    {
        keysPressed=0;
        out.clearSignals();
        for(int i=0;i<keys.length;i++)
        {
            if(key == keys[i])
            {
                keyStates[i] = true;
            }
            if(keyStates[i])
            {
                keysPressed++;
            }
        }

        if(keysPressed == 0) amp = MAXAMP;
        else amp=MAXAMP/keysPressed;

        for(int i=0;i<keys.length;i++)
        {
            if(keyStates[i])
            {
                out.addSignal(new SineWave(tones[i], amp, out.sampleRate()));
            }
        }
    }

    //determine which keys have been released
    @Override
    public void keyReleased()
    {
        keysPressed = 0;
        out.clearSignals();
        for(int i=0;i<keys.length;i++)
        {
            if(key == keys[i])
            {
                keyStates[i] = false;
            }
            if(keyStates[i])
            {
                keysPressed++;
            }
        }

        if(keysPressed == 0) amp = MAXAMP;
        else amp=MAXAMP/keysPressed;

        for(int i=0;i<keys.length;i++)
        {
            if(keyStates[i])
            {
                out.addSignal(new SineWave(tones[i], amp, out.sampleRate()));
            }
        }
    }
    @Override
    public void draw()
    {
        background(100);
        stroke(255);


        //draw wave
        for(int i = 0; i < out.bufferSize() - 1; i++)
        {
            float x1 = map(i, 0, out.bufferSize(), 0, width);
            float x2 = map(i+1, 0, out.bufferSize(), 0, width);
            line(x1, 50 + out.left.get(i)*50, x2, 50 + out.left.get(i+1)*50);
        }
        stroke(120);
        line(0, 100, 512, 100);
        line(0, 0, 512, 0);
        fill(0);
        text("amplitude: "+amp*100+"%", 10, 115);
        //text(amp, 45, 115);

        int x = 150;
        for(int i=0;i<keys.length;i++)
        {
            if(keyStates[i])
            {
                fill(255);
                text(notes[i], x+(i*10), 115);
            } else {
                fill(0);
                text(notes[i], x+(i*10), 115);
            }
        }
    }
    @Override
    public void stop()
    {
        // always close Minim audio classes when you are finished with them
        out.close();
        minim.stop();

        super.stop();
    }
}
