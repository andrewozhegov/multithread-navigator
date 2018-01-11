package navigator;

import javafx.util.Pair;

public class MachuPickchuNavigator implements Navigator
{
    private final int roadCell    = -1;           // . (точка) дорога
    private final int barrierCell = -2;           // # (решетка) стена
    private final int startCell   =  0;           // @ (собака) старт
    private final int finishCell  = -3;           // X (заглавный икс) финиш

    private char[][] mapInChar;

    private Pair <Integer, Integer> start; // позиции граничных элементов волны для обхода

    /**
     * Поиск кратчайшего маршрута на карте города между двумя точками
     * @param map карта города
     * @return карта города с построенным маршрутом
     */
    public char[][] searchRoute (char[][] map)
    {
        if (map.length > 10000 || map[0].length > 10000) return null; // размер карты превышает допустимые

        mapInChar = map;
        int[][] mapInInts = toIntMap(map);
        if (start == null) return null; // количество начальных точек не соответствует ожиданиям
        if (mapInInts == null) return null; // на карте найдены неверные символы

        // Критическая область
        Wave Q = new Wave(mapInInts);
        Q.queue.add(start);

        Explorer north = new Explorer (Q, 0, -1);
        Explorer east = new Explorer (Q, 1, 0);
        Explorer south = new Explorer (Q, 0, 1);
        Explorer west = new Explorer (Q, -1, 0);

        north.start();
        east.start();
        south.start();
        west.start();

        while (Q.finish == null) Thread.yield(); // ожидаем нахождения финишной точки

        int i = Q.finish.getKey();
        int j = Q.finish.getValue();

        for (int d = mapInInts[i][j] - 1; d > 0; --d)
        {
            if (j > 0)
                if (mapInInts[i][j - 1] == d) {
                    mapInChar[i][--j] = '+';
                    continue;
                }

            if (i > 0)
                if (mapInInts[i - 1][j] == d) {
                    mapInChar[--i][j] = '+';
                    continue;
                }

            if (i < map.length - 1)
                if (mapInInts[i + 1][j] == d) {
                    mapInChar[++i][j] = '+';
                    continue;
                }

            if (j < map[0].length - 1)
                if (mapInInts[i][j + 1] == d) {
                    mapInChar[i][++j] = '+';
                    continue;
                }
        }
        return mapInChar;
    }

    /**
     * Преобразование символьной карты в целочисленую
     * @param map символьная карта города
     * @return карта города с использованием целочисленных обозначений
     */
    private int[][] toIntMap (char[][] map)
    {
        int[][] out = new int[map.length][map[0].length];

        for (int i = 0; i < map.length; ++i)
        {
            for (int j = 0; j < map[i].length; ++j)
            {
                switch (map[i][j])
                {
                    case '.': {
                        out[i][j] = roadCell;
                        break;
                    }
                    case '#': {
                        out[i][j] = barrierCell;
                        break;
                    }
                    case '@': {
                        out[i][j] = startCell;
                        start = new Pair<>(i, j); // запоминаем координаты стартовой вершины
                        break;
                    }
                    case 'X': {
                        out[i][j] = finishCell;
                        break;
                    }
                    default: return null; // на карте находятся посторонние символы
                }
            }
        }
        return out;
    }
}
