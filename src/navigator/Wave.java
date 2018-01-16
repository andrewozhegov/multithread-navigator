package navigator;

import javafx.util.Pair;
import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;

public class Wave
{
    public int[][] map; // наша целочисленная карта
    public int d; // число шагов распросранения волны

    public Pair <Integer, Integer> point; // текущая позиция
    public Pair <Integer, Integer> finish; // финишная позиция (когда найдена)

    public LinkedList <Pair <Integer, Integer>> queue; // список вершин

    public CyclicBarrier barrier;

    public int points;

    public Wave (int[][] map)
    {
        this.map = map;
        d = 0;
        point = null;
        finish = null;
        queue = new LinkedList<>();
        barrier = new CyclicBarrier(4, new Run(this));
        points = 1;
    }

    void nextStep ()
    {
        if (points == 0) {
            points = queue.size();
            ++d;
        }
        point = queue.poll(); // вытаскиваем первый элемент из очереди в точку
        --points;
    }
}
