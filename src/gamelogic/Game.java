package gamelogic;

import gamelogic.player.*;
import gui.console.Cons_GameBoard;

public class Game {

    private char [][] playDek = new char [3][3];
    private char move;

    // Class constructor initializes without accessible variables
    public Game() {
    }
    
    // instance of Cons_GameBoard Class
    Cons_GameBoard terminalGame = new Cons_GameBoard();

    //TODO reconsider pre-initialized instances    
    Player_0 player0 = new Player_0();
    Player_1 player1 = new Player_1();
    Player_ai playerAI = new Player_ai();

    public void setGame(boolean p0, boolean p1){
        if(p0&&p1){
            player0.setplayChar(terminalGame.askPlayerSymbol("player 1"));      //TODO get rid of hardbaked 
            player1.setplayChar(terminalGame.askPlayerSymbol("player 2"));

            gameSwitch();
        }else if(p0&&!p1){
            player0.setplayChar(terminalGame.askPlayerSymbol("player 1"));
            playerAI.setOponentChar(player0.getplayChar());
            //TODO create fixed symbol for CPU
        }
    }

    private void gameSwitch(){
        //TODO whole method needs whichPlayerReturn() method overloading
        boolean p0 = player0.getTurn(); 
        boolean p1 = player1.getTurn();
        if(p0&&!p1){
            player0.noTurn();       //TODO get rid of hardbaked
            player1.isTurn();
            updateBoard(playDek);
            gameState();       
        }else if(!p0&&p1){
            player0.isTurn();       //TODO get rid of hardbaked
            player1.noTurn();
            updateBoard(playDek);
            gameState();
        }else{
            //TODO random first turn generator.
            player0.noTurn();       //TODO get rid of hardbaked
            player1.isTurn();
            updateBoard(playDek);
            gameState();        
        }
    }

    private void gameState() {         
        if(player0.getTurn()&&!player1.getTurn()){      //TODO get rid of hardbaked
            move = terminalGame.askPlayerToPlay(player0.getplayChar(), "Player 1");
        }else if(!player0.getTurn()&&player1.getTurn()){
            move = terminalGame.askPlayerToPlay(player1.getplayChar(), "Player 2");
        }else if(!player0.getTurn()&&!player1.getTurn()&&playerAI.getTurn()){
            move = playerAI.takeTurn(playDek);
        }else{terminalGame.errorMessage("in gamestate() player select");}

        for (char[] row : playDek) {
            for (int i=0;i<row.length; i++) {
                if(row[i]==move){
                    row[i] = whichPlayerReturn();
                }  
            }
        }
        if(!gameWin()){
            gameSwitch();
        }else if(gameWin()){
            updateBoard(playDek);
            terminalGame.playerWins(whichPlayerReturn());

        }else{terminalGame.errorMessage("in gamestate() winning game select.");}
    
    }
    

    private boolean gameWin(){
        boolean state = false;
        char turnChar = whichPlayerReturn();
        // if horizontals 3 same chars in a row win game & end game
        if(!state){
            for (char[] row : playDek) {
                int w=0;
                for (char node : row) {
                    if(node==turnChar){
                        w++;
                    }
                }
                if(w==3){
                    state = true;
                    break;
                }
            }
        // if verticals 3 same chars in a row win game & end game
        }if(!state){
            int w=0;
            for(int n=0; n<=2; n++){
                for(int row=0; row<=2; row++){
                    if(playDek[row][n]==turnChar){
                        w++;                        
                    }
                    if(row==2&&w==3){
                        state = true;
                        break;
                    }
                }
                if(w!=3){
                    w = 0;
                }
            }
        // if Diagonals 3 same chars in a row win game & end game
        }if(!state){
            int w=0, n=0, row=0;
            // forwards diagonal
            if(playDek[0][0]==turnChar){
                while(row<=2){
                    if(playDek[row][n]==turnChar){
                        w++;
                        if(w==3){
                            state = true;
                        }
                    }
                    n++;
                    row++;
                }
            // backwards diagonal
            }else if(playDek[0][2]==turnChar){
                w = 0;
                n = 2;
                row = 0;
                while(row<=2){
                    if(playDek[row][n]==turnChar){
                        w++;
                        if(w==3){
                            state = true;
                        }
                    }
                    n--;
                    row++;
                }
            
            }
        // if playDek is full, end game
        }else if(!state){
            int w=0;
            for(int p=0; p<=2; p++){
                // overloaded method, which returns
                turnChar = whichPlayerReturn(p); 
                for(char[] row : playDek) {
                    for(char node : row) {
                        if(node==turnChar){
                            w++;
                        }
                    }
                   
                }
                if(w==9){
                    gameFullDeck();
                    break;
                }
            }
        }
        return state;
    }

    private void gameFullDeck(){
        terminalGame.playerDraw();
    }

    //controlled by playerselect, injects parameters of players
    public void playerObjectInjection(){
        // TODO create logic

    }

    // Returns The current playing character
    private char whichPlayerReturn(){
        char playerChar = ' ';
        if(player0.getTurn()&&!player1.getTurn()){  
            playerChar = player0.getplayChar();
        }else if(!player0.getTurn()&&player1.getTurn()){
            playerChar = player1.getplayChar();
        }
        return playerChar;
    }

    // Returns all playing character
    private char whichPlayerReturn(int player){
        char playerChar = ' ';
        if(player==0){  
            playerChar = player0.getplayChar();
        }else if(player==1){
            playerChar = player1.getplayChar();
        }else if(player==2){
            playerChar = playerAI.getplayChar();
        }
        return playerChar;
    }


    public void setDefaultPlayDek() {
        int n = '1';

        for (char[] row : playDek) {
            for (int i = 0; i < row.length; i++) {
                row[i] = (char)n;
                n++;
            }
        }
    }

    public void updateBoard(char [][] playDek){
        // method call to Console Gameboard Class instance.
        // this method only updates the playing field, 
        // and accepts only a 2D character array, the array contains the 9 playable positions of the playingBoard.
        terminalGame.updateBoard(playDek);

    }     

}