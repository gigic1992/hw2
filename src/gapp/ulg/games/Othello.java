package gapp.ulg.games;

import gapp.ulg.game.board.*;
import gapp.ulg.game.util.BoardOct;
import gapp.ulg.game.util.Utils;
import gapp.ulg.play.RandPlayer;

import static gapp.ulg.game.board.PieceModel.Species;

import java.util.*;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC. Non modificare
 * le intestazioni dei metodi.</b>
 * <br>
 * Un oggetto Othello rappresenta un GameRuler per fare una partita a Othello. Il
 * gioco Othello si gioca su una board di tipo {@link Board.System#OCTAGONAL} 8x8.
 * Si gioca con pezzi o pedine di specie {@link Species#DISC} di due
 * colori "nero" e "bianco". Prima di inziare a giocare si posizionano due pedine
 * bianche e due nere nelle quattro posizioni centrali della board in modo da creare
 * una configurazione a X. Quindi questa è la disposzione iniziale (. rappresenta
 * una posizione vuota, B una pedina bianca e N una nera):
 * <pre>
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . B N . . .
 *     . . . N B . . .
 *     . . . . . . . .
 *     . . . . . . . .
 *     . . . . . . . .
 * </pre>
 * Si muove alternativamente (inizia il nero) appoggiando una nuova pedina in una
 * posizione vuota in modo da imprigionare, tra la pedina che si sta giocando e
 * quelle del proprio colore già presenti sulla board, una o più pedine avversarie.
 * A questo punto le pedine imprigionate devono essere rovesciate (da bianche a nere
 * o viceversa, azione di tipo {@link Action.Kind#SWAP}) e diventano
 * di proprietà di chi ha eseguito la mossa. È possibile incastrare le pedine in
 * orizzontale, in verticale e in diagonale e, a ogni mossa, si possono girare
 * pedine in una o più direzioni. Sono ammesse solo le mosse con le quali si gira
 * almeno una pedina, se non è possibile farlo si salta il turno. Non è possibile
 * passare il turno se esiste almeno una mossa valida. Quando nessuno dei giocatori
 * ha la possibilità di muovere o quando la board è piena, si contano le pedine e si
 * assegna la vittoria a chi ne ha il maggior numero. Per ulteriori informazioni si
 * può consultare
 * <a href="https://it.wikipedia.org/wiki/Othello_(gioco)">Othello</a> */
public class Othello implements GameRuler<PieceModel<Species>> {
    private long time;
    private int size;
    private Player<PieceModel<Species>> giocatore1;
    private Player<PieceModel<Species>> giocatore2;
    private Board<PieceModel<Species>> tavolo;
    private int turnoG;
    private int vtavolino;
    private List<GameRuler<PieceModel<Species>>> lista_stati;
    /** Crea un GameRuler per fare una partita a Othello, equivalente a
     * {@link Othello#Othello(long, int, String, String) Othello(0,8,p1,p2)}.
     * @param p1  il nome del primo giocatore
     * @param p2  il nome del secondo giocatore
     * @throws NullPointerException se p1 o p2 è null */
    public Othello(String p1, String p2) {
        this(0,8,p1,p2);
    }

    /** Crea un GameRuler per fare una partita a Othello.
     * @param time  tempo in millisecondi per fare una mossa, se <= 0 significa nessun
     *              limite
     * @param size  dimensione della board, sono accettati solamente i valori 6,8,10,12
     * @param p1  il nome del primo giocatore
     * @param p2  il nome del secondo giocatore
     * @throws NullPointerException se {@code p1} o {@code p2} è null
     * @throws IllegalArgumentException se size non è uno dei valori 6,8,10 o 12 */
    public Othello(long time, int size, String p1, String p2){
        if(!Arrays.asList(6,8,10,12).contains(size)){ throw new IllegalArgumentException("la dimensione non è consentita"); }
        if (p1 == null || p2 == null) { throw new NullPointerException("nessun giocatore può essere nullo");}
        this.giocatore1=new RandPlayer<>(p1);
        this.giocatore2=new RandPlayer<>(p2);
        this.size=size;
        this.time=time;
        this.tavolo=new BoardOct<PieceModel<Species>>(size,size);
        this.turnoG=1;
        this.vtavolino=-1;
        this.tavolo.put(new PieceModel<Species>(Species.DISC,"bianco"),new Pos((size/2)-1,size/2));
        this.tavolo.put(new PieceModel<Species>(Species.DISC,"nero"),new Pos(size/2,size/2));
        this.tavolo.put(new PieceModel<Species>(Species.DISC,"nero"),new Pos((size/2)-1,(size/2)-1));
        this.tavolo.put(new PieceModel<Species>(Species.DISC,"bianco"),new Pos(size/2,(size/2)-1));
        this.lista_stati=new ArrayList<>();
        lista_stati.add(copy());
        this.giocatore1.setGame(this);
        this.giocatore2.setGame(this);
        }

    /** Il nome rispetta il formato:
     * <pre>
     *     Othello<i>Size</i>
     * </pre>
     * dove <code><i>Size</i></code> è la dimensione della board, ad es. "Othello8x8". */
    @Override
    public String name() {return "Othello"+String.valueOf(size)+"x"+String.valueOf(size);
    }

    @Override
    public <T> T getParam(String name, Class<T> c) {
        return null;
    }

    @Override
    public List<String> players() { return Collections.unmodifiableList(Arrays.asList(giocatore1.name(), giocatore2.name()));}

    /** Assegna il colore "nero" al primo giocatore e "bianco" al secondo. */
    @Override
    public String color(String name) {
        if (name==null){throw new NullPointerException("il nome non può essre nullo");}
        if (!players().contains(name)) {
            throw new IllegalArgumentException("il nome di entrambi i giocatori non corrisponde");}
        if(giocatore1.name().equals(name)){return "nero";}
        return "bianco";
    }
    @Override
    public Board<PieceModel<Species>> getBoard() { return Utils.UnmodifiableBoard(tavolo);}

    /** Se il giocatore di turno non ha nessuna mossa valida il turno è
     * automaticamente passato all'altro giocatore. Ma se anche l'altro giuocatore
     * non ha mosse valide, la partita termina. */
    @Override
    public int turn() {
        return turnoG;
    }

    /** Se la mossa non è valida termina il gioco dando la vittoria all'altro
     * giocatore. */
    @Override
    public boolean move(Move<PieceModel<Species>> m) {
        if(m==null){throw new NullPointerException("la mossa non può essere nulla");}
        if(turnoG==0){throw new IllegalStateException("il gioco è terminato");}
        if(isValid(m) && m.getKind()!=Move.Kind.RESIGN){
            for(Action a : m.getActions()){
                if(a.getKind() == Action.Kind.ADD) { tavolo.put((PieceModel) a.getPiece(), (Pos) a.getPos().get(0)); }
                if(a.getKind() == Action.Kind.SWAP) {
                    for (Object p:a.getPos()){
                        tavolo.put((PieceModel)a.getPiece(),(Pos)p);
                    }
                }
            }
            if(turnoG==1){
                turnoG=2;
            }
            else if (turnoG==2){
                turnoG=1;
            }
            if(validMoves().isEmpty()){
                if(turnoG==1){
                    turnoG=2;
                }
                else if (turnoG==2){
                    turnoG=1;
                }
                if(validMoves().isEmpty()) { turnoG = 0; }
            }
            lista_stati.add(copy());
            return true;
        }
        if(m.getKind()== Move.Kind.RESIGN){
            if(turnoG==2){vtavolino=1;}
            else if(turnoG == 1) {vtavolino=2;}
            turnoG = 0;
            lista_stati.add(copy());
            return true;
        }
        int other = 0;
        if(turnoG==2){other=1;}
        other=2;
        vtavolino = other;
        turnoG = 0;
        lista_stati.add(copy());
        return false;
    }

    @Override
    public boolean unMove() {
        if (lista_stati.size() == 1) {
            return false;
        }
        Board passata = lista_stati.get(lista_stati.size() - 2).getBoard();
        for (Pos p : tavolo.positions()) {
                if (tavolo.get(p) != null && passata.get(p) == null) {
                    tavolo.remove(p);}
                else if (passata.get(p)!=null&&!passata.get(p).equals(tavolo.get(p))){ tavolo.put((PieceModel<Species>) passata.get(p),p);}
                }
        turnoG=lista_stati.get(lista_stati.size()-2).turn();
        lista_stati.remove(lista_stati.size()-1);
        return true;
        }
    @Override
    public boolean isPlaying(int i) {
        if (i<=0||i>2){throw new IllegalArgumentException("l'indice non individua alcun giocatore");}
        if (turnoG!=0){return true;}
        return false;
    }

    @Override
    public int result() {
        if (turnoG!=0){return -1; }
        else if(vtavolino!=-1){ return vtavolino; }
        else if(score(1)==score(2)){return 0;}
        else if ( score(1)>score(2)){return 1;}
        return 2;
    }


    /** Ogni mossa, eccetto l'abbandono, è rappresentata da una {@link Action} di tipo
     * {@link Action.Kind#ADD} seguita da una {@link Action} di tipo
     * {@link Action.Kind#SWAP}. */
    @Override
    public Set<Move<PieceModel<Species>>> validMoves() {
        if (turnoG == 0) {
            throw new IllegalStateException("il gioco è terminato.");
        }
        List<Board.Dir> direzioni = Arrays.asList(Board.Dir.DOWN, Board.Dir.DOWN_L, Board.Dir.DOWN_R, Board.Dir.LEFT, Board.Dir.RIGHT, Board.Dir.UP, Board.Dir.UP_L, Board.Dir.UP_R);
        Set<Move<PieceModel<Species>>> finale = new HashSet<>();
        PieceModel<Species> disco1 = new PieceModel<>(Species.DISC, "nero");
        PieceModel<Species> disco2 = new PieceModel<>(Species.DISC, "bianco");
        if (turnoG == 2) {
            disco1 = new PieceModel<>(Species.DISC, "bianco");
            disco2 = new PieceModel<>(Species.DISC, "nero");
        }
        for (Pos p : tavolo.positions()) {
            if (tavolo.get(p) == null) {
                Set<Pos> lista_swap = new HashSet<>();
                for (Board.Dir d : direzioni) {
                    Set<Pos> lista_swapt = new HashSet<>();
                    try {
                        if (tavolo.get(tavolo.adjacent(p, d)).equals(disco2)) {
                            Pos temporanea = tavolo.adjacent(p, d);
                            while (true) {
                                if (tavolo.get(temporanea) == null) {
                                    lista_swapt = new HashSet<>();
                                    break;
                                }
                                if (tavolo.get(temporanea).equals(disco1)) {
                                    break;
                                }
                                if (tavolo.adjacent(temporanea, d) == null) {
                                    lista_swapt = new HashSet<>();
                                    break;
                                }
                                if (tavolo.get(temporanea).equals(disco2)) {
                                    lista_swapt.add(temporanea);
                                    temporanea = tavolo.adjacent(temporanea, d);
                                }
                            }
                        }

                    } catch (NullPointerException f) {
                        continue;
                    }
                    if (lista_swapt.size() != 0) {
                        lista_swap.addAll(lista_swapt);
                    }
                }
                if (lista_swap.size() != 0) {
                    Pos[] arrayp=new Pos[lista_swap.size()];
                    arrayp=lista_swap.toArray(arrayp);
                    Action prima= new Action(p,disco1);
                    Action seconda= new Action(disco1,arrayp);
                    finale.add(new Move(Arrays.asList(prima,seconda)));
                }
            }
        }
        if(finale.size()!=0){
            finale.add(new Move(Move.Kind.RESIGN));
        }
        return Collections.unmodifiableSet(finale);
    }
    @Override
    public double score(int i) {
        if (turnoG < 0 || turnoG > 2) {
            throw new IllegalArgumentException("l'indice non identifica alcun giocatore");
        }
        int sg1= tavolo.get(new PieceModel<Species>(Species.DISC,"nero")).size();
        int sg2= tavolo.get(new PieceModel<Species>(Species.DISC,"bianco")).size();
        if(i==1){return sg1;}
        return sg2;
    }
    @Override
    public GameRuler<PieceModel<Species>> copy() {
        Board board1=new BoardOct(size,size);
        for(int i =0;i<size;i++){
            for(int h=0;h<size;h++){
                Pos p= new Pos(i,h);
                if (tavolo.get(p)!=null){
                    board1.put(tavolo.get(p),p);
                }
            }
        }
        return new Othello(time,size,giocatore1,giocatore2,board1,turnoG,vtavolino,lista_stati);
    }
    private Othello(long t,int s, Player p1,Player p2, Board b2, int tg,int vt, List l){
        this.time=t;
        this.size=s;
        this.giocatore1=p1;
        this.giocatore2=p2;
        this.tavolo=b2;
        this.turnoG=tg;
        this.vtavolino=vt;
        this.lista_stati = l;
    }
    @Override
    public Mechanics<PieceModel<Species>> mechanics() {return null; }
}
