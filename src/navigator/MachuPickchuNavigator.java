package navigator;

import javafx.util.Pair;

public class MachuPickchuNavigator implements Navigator
{
    private final int roadCell    = -1;           // . (точка) дорога
    private final int barrierCell = -2;           // # (решетка) стена
    private final int startCell   =  0;           // @ (собака) старт
    private final int finishCell  = -3;           // X (заглавный икс) финиш

    private int mapMaxI;
    private int mapMaxJ;

    private char[][] mapInChar;

    private Pair <Integer, Integer> start; // позиции граничных элементов волны для обхода

    MachuPickchuNavigator ()
    {
        mapMaxI = 10000;
        mapMaxJ = 10000;
    }

    /**
     * Поиск кратчайшего маршрута на карте города между двумя точками
     * @param map карта города
     * @return карта города с построенным маршрутом
     */
    public char[][] searchRoute (char[][] map)
    {
        if (map.length > mapMaxI || map[0].length > mapMaxJ) return null; // размер карты превышает допустимые

        mapInChar = map;
        int[][] mapInInts = toIntMap(map);
        if (start == null) return null; // количество начальных точек не соответствует ожиданиям
        if (mapInInts == null) return null; // на карте найдены неверные символы

        Pair <Integer, Integer> finish = doWaveToFinish(mapInInts); // распространение волны и нахождение финиша

        if (finish.getKey() == null && finish.getValue() == null) return null;

        routeBuilding(mapInInts, finish); // построение кратчайшего пути

        return mapInChar; // возврат символьной карты с проложенным маршрутом
    }

    /**
     * Распространение волны до нахождения финишной позиции
     * @param mapInInts целочисленная карта города
     * @return объект волны в момент нахождения финишной позиции
     */
    private Pair <Integer, Integer> doWaveToFinish (int[][] mapInInts)
    {
        // Критическая область
        Wave Q = new Wave(mapInInts);
        
        Q.addToQueue(start);

        new Thread(new Explorer (Q)).start();
        new Thread(new Explorer (Q)).start();
        new Thread(new Explorer (Q)).start();
        new Thread(new Explorer (Q)).start();

        while (Q.finish == null) Thread.yield(); // ожидаем нахождения финишной точки

        return Q.finish;
    }

    /**
     * Построение маршрута на символьной карте
     * @param mapInInts индексированная целочисленная карта города
     * @param finish финишная позиция
     */
    private void routeBuilding (int[][] mapInInts, Pair <Integer, Integer> finish)
    {
        int i = finish.getKey();
        int j = finish.getValue();

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

            if (i < mapInChar.length - 1)
                if (mapInInts[i + 1][j] == d) {
                    mapInChar[++i][j] = '+';
                    continue;
                }

            if (j < mapInChar[0].length - 1)
                if (mapInInts[i][j + 1] == d) {
                    mapInChar[i][++j] = '+';
                    continue;
                }
        }
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
