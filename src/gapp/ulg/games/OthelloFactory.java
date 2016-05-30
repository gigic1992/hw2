package gapp.ulg.games;

import gapp.ulg.game.GameFactory;
import gapp.ulg.game.Param;
import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.PieceModel;

import static gapp.ulg.game.board.PieceModel.Species;

import java.util.Arrays;
import java.util.List;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Una OthelloFactory è una fabbrica di {@link GameRuler} per giocare a Othello.
 * I {@link GameRuler} fabbricati dovrebbero essere oggetti {@link Othello}. */
public class OthelloFactory implements GameFactory<GameRuler<PieceModel<Species>>> {
    private String[] a_Nomi;
    private List<Param<String>> lista_p;

    /**
     * Crea una fattoria di {@link GameRuler} per giocare a Othello
     */
    public OthelloFactory() {
    }

    @Override
    public String name() {
        return "Othello";
    }

    @Override
    public int minPlayers() {
        return 2;
    }

    @Override
    public int maxPlayers() {
        return 2;
    }

    /**
     * Ritorna una lista con i seguenti due parametri:
     * <pre>
     * Primo parametro, valori di tipo String
     *     - name: "Time"
     *     - prompt: "Time limit for a move"
     *     - values: ["No limit","1s","2s","3s","5s","10s","20s","30s","1m","2m","5m"]
     *     - default: "No limit"
     * Secondo parametro, valori di tipo String
     *     - name: "Board"
     *     - prompt: "Board size"
     *     - values: ["6x6","8x8","10x10","12x12"]
     *     - default: "8x8"
     * </pre>
     *
     * @return la lista con i due parametri
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Param<?>> params() {
        return null;
    }


    @Override
    public void setPlayerNames(String... names) {
        if (names.length != 2) {
            throw new IllegalArgumentException("il numero dei giocatori deve essere 2");
        }
        for (String n : names) {
            if (n == null) {
                throw new NullPointerException("il nome di un giocatore non può essere nullo");
            }
        }
        a_Nomi = names;
    }

    @Override
    public GameRuler<PieceModel<Species>> newGame() {
        if (a_Nomi == null) {
            throw new IllegalArgumentException("i nomi dei giocatori non sono stati impostati");
        }
        return new Othello(a_Nomi[0], a_Nomi[1]);
    }
}