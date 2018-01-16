package navigator;

import javafx.util.Pair;
import java.util.concurrent.BrokenBarrierException;

public class Explorer extends Thread
{
    Wave wave; // ссылка на объект волны

    private int di; // смещение обзора по i
    private int dj; // смещение обзора по j

    Explorer (Wave wave, int di, int dj)
    {
        this.wave = wave;
        this.di = di;
        this.dj = dj;
    }

    public void run()
    {
        while (true)
        {
            // ожидание остальных потоков
            try {
                wave.barrier.await();
                if (wave.finish != null)
                {
                    wave.barrier.notifyAll();
                    break; // выходим из потока при обнаружении финиша
                }

            }
            catch (InterruptedException e) { e.printStackTrace(); }
            catch (BrokenBarrierException e) { e.printStackTrace(); }

            //System.out.println(" Мы внутри метода " + this.getName());

            if (wave.point.getKey() + di < 0 ||
                    wave.point.getValue() + dj < 0 ||
                    wave.point.getKey() + di > wave.map.length -1 ||
                    wave.point.getValue() + dj > wave.map[0].length - 1) continue;

            switch (wave.map[wave.point.getKey() + di][wave.point.getValue() + dj])
            {
                case -3: { //finish
                    wave.finish = new Pair<>(wave.point.getKey() + di, wave.point.getValue() + dj);
                }
                case -1: { //road
                    wave.map[wave.point.getKey() + di][wave.point.getValue() + dj] = wave.d + 1;
                    wave.queue.add(new Pair <> (wave.point.getKey() + di, wave.point.getValue() + dj));
                }
            }
        }
        //System.out.println("Последняя операция потока" + this.getName());
    }
}
