package navigator;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        MachuPickchuNavigator nav = new MachuPickchuNavigator();

        char[][] map = {
//                {'@', '.', '.'},
//                {'#', '.', '#'},
//                {'X', '.', '.'}

//                {'.', '.', '.', '@', '.'},
//                {'.', '#', '#', '#', '#'},
//                {'.', '.', '.', '.', '.'},
//                {'#', '#', '#', '#', '.'},
//                {'.', 'X', '.', '.', '.'}

                {'#', '.', '.', '.', 'X'},
                {'.', '.', '.', '#', '.'},
                {'.', '#', '#', '.', '.'},
                {'#', '#', '.', '.', '.'},
                {'@', '.', '.', '#', '.'}
        };

        char[][] route = nav.searchRoute(map);

        printArrayInChar(route);
    }

    public static void printArrayInChar (char[][] array) {
        if (array == null)
        {
            System.out.println(array);
            return;
        }
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[i].length; ++j) {
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
    }
}
