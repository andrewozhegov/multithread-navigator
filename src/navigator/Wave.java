package navigator;

import javafx.util.Pair;
import java.util.LinkedList;
import java.util.concurrent.CyclicBarrier;

public class Wave
{
    int[][] map; // наша целочисленная карта
    public int d; // число шагов распросранения волны

    public Pair <Integer, Integer> point; // текущая позиция
    public Pair <Integer, Integer> finish; // финишная позиция (когда найдена)

    public LinkedList <Pair <Integer, Integer>> queue; // список вершин

    public CyclicBarrier barrier;

    public Wave (int[][] map)
    {
        this.map = map;
        d = 0;
        point = null;
        finish = null;
        queue = new LinkedList<>();
        barrier = new CyclicBarrier(4, new Run(this));
    }

    void nextStep ()
    {
        point = queue.poll(); // вытаскиваем первый элемент из очереди в точку
        this.d++;
    }
}
