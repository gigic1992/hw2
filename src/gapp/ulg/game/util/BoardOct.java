package gapp.ulg.game.util;

import gapp.ulg.game.board.Board;
import gapp.ulg.game.board.Pos;

import java.util.*;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Gli oggetti BoardOct implementano l'interfaccia {@link Board} per rappresentare
 * board generali con sistema di coordinate {@link System#OCTAGONAL}
 * modificabili.
 * @param <P>  tipo del modello dei pezzi */
public class BoardOct<P> implements Board<P> {
    public final int width;
    public final int height;
    public final System system;
    public final Map<Pos,P> posizioni;
    /** Crea una BoardOct con le dimensioni date (può quindi essere rettangolare).
     * Le posizioni della board sono tutte quelle comprese nel rettangolo dato e le
     * adiacenze sono tutte e otto, eccetto per le posizioni di bordo.
     * @param width  larghezza board
     * @param height  altezza board
     * @throws IllegalArgumentException se width <= 0 o height <= 0 */
    public BoardOct(int width, int height) {
        if (width<=0||height<=0){throw new IllegalArgumentException("largezza e altezza non possono essere minori o ugulai a zero");}
        this.width=width;
        this.height=height;
        this.system=System.OCTAGONAL;
        Map<Pos,P> luigi=new HashMap<>();
        for (int i =0;i<width;i++){
            for (int a=0;a<height;a++){
                luigi.put(new Pos(i,a),null);
            }
        }
        this.posizioni=luigi;

    }

    /** Crea una BoardOct con le dimensioni date (può quindi essere rettangolare)
     * escludendo le posizioni in exc. Le adiacenze sono tutte e otto, eccetto per
     * le posizioni di bordo o adiacenti a posizioni escluse. Questo costruttore
     * permette di creare board per giochi come ad es.
     * <a href="https://en.wikipedia.org/wiki/Camelot_(board_game)">Camelot</a>
     * @param width  larghezza board
     * @param height  altezza board
     * @param exc  posizioni escluse dalla board
     * @throws NullPointerException se exc è null
     * @throws IllegalArgumentException se width <= 0 o height <= 0 */
    public BoardOct(int width, int height, Collection<? extends Pos> exc) {
        if (width<=0||height<=0){throw new IllegalArgumentException("largezza e altezza non possono essere minori o ugulai a zero");}
        this.width=width;
        this.height=height;
        this.system=System.OCTAGONAL;
        Map<Pos,P> luigi=new HashMap<>();
        for (int i =0;i<width;i++){
            for (int a=0;a<height;a++){
                luigi.put(new Pos(i,a),null);
            }
        }
        for(Pos i :exc){
            luigi.remove(i);
        }
        this.posizioni=luigi;
    }

    @Override
    public System system() { return system;}

    @Override
    public int width() { return width;}

    @Override
    public int height() { return height;}

    @Override
    public Pos adjacent(Pos p, Dir d) {
        if(p==null||d==null){throw new NullPointerException("posizione e direzioni non possono essere nulle");}
        if(posizioni.containsKey(p)) {
            if(d==Dir.UP) {
                Pos temp = new Pos(p.getB(),p.getT()+1);
                if (posizioni.containsKey(temp)){
                    return temp;
                }
            }
            if(d==Dir.DOWN&&p.getT()>=0) {
                Pos temp = new Pos(p.getB(),p.getT()-1);
                if (posizioni.containsKey(temp)){
                    return temp;
                }
            }
            if(d==Dir.LEFT && p.getB()-1 >= 0) {
                Pos temp = new Pos(p.getB()-1,p.getT());
                if (posizioni.containsKey(temp)){
                    return temp;
                }
            }
            if(d==Dir.RIGHT) {
                Pos temp = new Pos(p.getB()+1,p.getT());
                if (posizioni.containsKey(temp)){
                    return temp;
                }
            }
            if(d==Dir.UP_L &&p.getB()>=0) {
                Pos temp = new Pos(p.getB()-1,p.getT()+1);
                if (posizioni.containsKey(temp)){
                    return temp;
                }
            }
            if(d==Dir.UP_R) {
                Pos temp = new Pos(p.getB()+1,p.getT()+1);
                if (posizioni.containsKey(temp)){
                    return temp;
                }
            }
            if(d==Dir.DOWN_L&&p.getB()>=0&&p.getT()>=0) {
                Pos temp = new Pos(p.getB()-1,p.getT()-1);
                if (posizioni.containsKey(temp)){
                    return temp;
                }
            }
            if(d==Dir.DOWN_R&&p.getT()>=0) {
                Pos temp = new Pos(p.getB()+1,p.getT()-1);
                if (posizioni.containsKey(temp)){
                    return temp;
                }
            }
        }return null;

    }

    @Override
    public List<Pos> positions() { List<Pos> luigi=new ArrayList<>();
        luigi.addAll(posizioni.keySet());
        return Collections.unmodifiableList(luigi);
    }

    @Override
    public P get(Pos p) {
        if(p==null){throw new NullPointerException("la posizione non può essere nulla");}
        if(posizioni.containsKey(p)){
            return posizioni.get(p);
        }
        return null;
    }


    @Override
    public boolean isModifiable() { return true; }

    @Override
    public P put(P pm, Pos p) {
        if(isModifiable()!=true){throw new UnsupportedOperationException("la board non è modificabile");}
        if(pm==null||p==null||p==null){throw new NullPointerException("posizione, direzione e numero di posizioni non possono essere nulli.");}
        if(!posizioni.containsKey(p)){throw new IllegalArgumentException("numero posizioni non può essere nullo.");}
        posizioni.put(p,pm);
        return pm;
    }

    @Override
    public P remove(Pos p) {
        if(isModifiable()!=true){throw new UnsupportedOperationException("la board non è modificabile");}
        if(p==null){throw new NullPointerException("la posizione non può essere nulla");}
        if(!posizioni.containsKey(p)){throw new IllegalArgumentException("la posizione p non appartiene alla board");}
        P posizioniv=posizioni.remove(p);
        posizioni.put(p,null);
        return posizioniv;
    }
}
