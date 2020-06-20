package pl.mr.checkers.server;

public class Utils
{
    public static char[] generateBoard()
    {
        char[] board = new char[64];

        // małe litery - białe
        // duże litery - czarne
        // p->pionek
        // d->damka

        board[1] = 'p';
        board[3] = 'p';
        board[5] = 'p';
        board[7] = 'p';
        board[8] = 'p';
        board[10] = 'p';
        board[12] = 'p';
        board[14] = 'p';
        board[17] = 'p';
        board[19] = 'p';
        board[21] = 'p';
        board[23] = 'p';

        board[41] = 'P';
        board[43] = 'P';
        board[45] = 'P';
        board[47] = 'P';
        board[48] = 'P';
        board[50] = 'P';
        board[52] = 'P';
        board[54] = 'P';
        board[57] = 'P';
        board[59] = 'P';
        board[61] = 'P';
        board[63] = 'P';

        return board;
    }

}
