package navigator;

import javafx.util.Pair;

public class Explorer extends Thread
{
    private Wave wave; // ссылка на объект волны

    private Pair<Integer, Integer> point; // позиция, обработкой которой занят поток

    private final int di[] = {0, 1, 0, -1}; // массивы смещений по осям для обхода окресности фон Неймана
    private final int dj[] = {-1, 0, 1, 0};

    Explorer (Wave wave)
    {
        this.wave = wave;
    }

    public void run()
    {
        while (true)
        {
            if (wave.finish != null) break;

            while (wave.isEmptyQueue()) Thread.yield();

            this.point = wave.getPoint(); // вытаскиваем первый элемент из очереди в точку

            for (int k = 0; k < 4; ++k) // обход окресности фон Неймана при помощи массивов смещений по осям
            {

                if (point.getKey() + di[k] < 0 ||
                        point.getValue() + dj[k] < 0 ||
                        point.getKey() + di[k] > wave.map.length - 1 ||
                        point.getValue() + dj[k] > wave.map[0].length - 1) continue;

                switch (wave.map[point.getKey() + di[k]][point.getValue() + dj[k]])
                {
                    case -3: { //finish
                        wave.finish = new Pair<>(point.getKey() + di[k], point.getValue() + dj[k]);
                    }
                    case -1: { //road
                        wave.map[point.getKey() + di[k]][point.getValue() + dj[k]] = wave.map[point.getKey()][point.getValue()] + 1;
                        wave.addToQueue(new Pair <> (point.getKey() + di[k], point.getValue() + dj[k]));
                    }
                }
            }
        }
    }
}
