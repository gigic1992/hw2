package gapp.ulg.game.util;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.Pos;

import java.util.List;

/**
 * Created by luigi on 07/04/2016.
 */
public class VisBoard<P> implements Board<P> {
    public Board<P> prima;
    public VisBoard(Board<P> inp){
        this.prima=inp;
    }
    @Override
    public System system() {
        return prima.system();
    }

    @Override
    public int width() {
        return prima.width();
    }

    @Override
    public int height() {
        return prima.height();
    }

    @Override
    public Pos adjacent(Pos p, Dir d) {
        return prima.adjacent(p,d);
    }

    @Override
    public List<Pos> positions() {
        return prima.positions();
    }

    @Override
    public P get(Pos p) {
        return prima.get(p);
    }

    @Override
    public boolean isModifiable(){
        return false;
    }

    @Override
    public P put(P pm,Pos p){
        throw new UnsupportedOperationException("la view non è modificabile");
        }

    @Override
    public P remove(Pos p){throw new UnsupportedOperationException("la view non è modificabile");}
    }
