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

        board[40] = 'P';
        board[42] = 'P';
        board[44] = 'P';
        board[46] = 'P';
        board[49] = 'P';
        board[51] = 'P';
        board[53] = 'P';
        board[55] = 'P';
        board[56] = 'P';
        board[58] = 'P';
        board[60] = 'P';
        board[62] = 'P';

        return board;
    }

}
