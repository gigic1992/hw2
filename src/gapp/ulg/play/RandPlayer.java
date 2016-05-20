package gapp.ulg.play;

import gapp.ulg.game.board.GameRuler;
import gapp.ulg.game.board.Move;
import gapp.ulg.game.board.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto RandPlayer è un oggetto che può giocare un qualsiasi gioco regolato
 * da un {@link GameRuler} perché, ad ogni suo turno, sceglie in modo random una
 * mossa tra quelle valide esclusa {@link Move.Kind#RESIGN}.
 * @param <P>  tipo del modello dei pezzi */
public class RandPlayer<P> implements Player<P> {
    /** Crea un giocatore random, capace di giocare a un qualsiasi gioco, che ad
     * ogni suo turno fa una mossa scelta in modo random tra quelle valide.
     * @param name  il nome del giocatore random
     * @throws NullPointerException se name è null */
    public String name;
    public GameRuler<P> grul;
    public Move<P> uMos;
    public RandPlayer(String name) {
        if (name==null){throw new NullPointerException("il nome non può essere nullo");}
        this.name=name;
        this.grul=null;
        this.uMos = null;
    }


    @Override
    public String name() {return name ;}

    @Override
    public void setGame(GameRuler<P> g) {grul=g;
    }


    @Override
    public void moved(int i, Move<P> m) {
        if(grul==null||grul.result()>=0){throw new IllegalStateException("il gioco non è impostato oppure è terminato"); }
        if (m==null){throw new NullPointerException("la mossa non può essere nulla");}
        if(i<1||i>grul.players().size()||
                !grul.isValid(m)){throw new IllegalArgumentException("la mossa non è valoda oppure indice di turnazione non valido");}
        grul.move(m);
        if(grul.result()==-1){
        }
        if(i == grul.turn()) { uMos = m; }
    }

    @Override
    public Move<P> getMove() {
        if (grul == null || grul.result() >= 0 || grul.turn() != grul.players().indexOf(name) + 1) {
            throw new IllegalStateException("il gioco non e impostato oppure non il turno del giocatore indicato oppure il gioco è terminato ");
        }
        int n=grul.validMoves().size();
        List listaMosse=new ArrayList<>();
        listaMosse.addAll(grul.validMoves());
        if(listaMosse.size()==0){return null;}
        Random k=new Random();
        Move mossa = null;
        mossa= (Move) listaMosse.get(k.nextInt(n));
        return mossa;
    }
}
