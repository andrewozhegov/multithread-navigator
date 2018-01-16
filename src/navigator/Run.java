package navigator;

public class Run extends Thread
{
    Wave wave;

    Run (Wave wave)
    {
        this.wave = wave;
    }

    public void run ()
    {
        //System.out.println("Sync throws");

        if (wave.finish == null)
        {
            if(wave.queue.isEmpty()) return; // случай, когда волна зашла в тупик
            wave.nextStep();
        }
    }
}

