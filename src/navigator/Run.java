package navigator;

import javafx.util.Pair;

public class Run extends Thread
{
    Wave wave;

    Run (Wave wave)
    {
        this.wave = wave;
    }

    public void run ()
    {
        if (wave.finish == null)
        {
            if(wave.queue.isEmpty()) wave.finish = new Pair<Integer, Integer>(null, null); // случай, когда волна зашла в тупик
            wave.nextStep();
        }
    }
}

