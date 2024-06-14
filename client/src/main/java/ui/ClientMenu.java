package ui;

import chess.*;
import model.GameData;
import request.*;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;
import serverhandling.ServerFacade;
import serverhandling.ServerMessageObserver;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class ClientMenu implements ServerMessageObserver {
    private final ServerFacade facade;
    HashMap<Integer, Integer> gameIDMap;
    ChessGame.TeamColor teamColor;
    ChessGame mostRecentGame;


    public ClientMenu(int port) {
        facade = new ServerFacade(port, this);
        gameIDMap = new HashMap<>(); //key is temporary game int, value is primary key of table


    }

    /**
     * Pre Login UI
     */


    public void run(){


        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.println("Welcome to Chess! Select an option below: ");
            printPreLoginOptions();
            String line = scanner.nextLine();

            try {
                result = evalPreLogin(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }



    private void printPreLoginOptions(){
        System.out.println("1: Help");
        System.out.println("2: Quit");
        System.out.println("3: Login");
        System.out.println("4: Register");
    }

    public String evalPreLogin(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "2" -> "quit";
                case "3" -> login();
                case "4" -> register();
                default -> preLoginHelp();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    /**
     * Pre login methods
     */

    private String preLoginHelp(){
        return "Enter 1 to see help options" + "\n" +
                "Enter 2 to Quit" + "\n" +
                "Enter 3 to Login" + "\n" +
                "Enter 4 to Register" + "\n";
    }

    private String register(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        RegisterResponse regResponse = facade.register(new RegisterRequest(username,password, email));
        if (regResponse.authToken() != null){
            postLoginUI(regResponse.authToken());
            return "";
        } else {
            return regResponse.message();
        }
    }

    private String login(){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        LoginResponse response =  facade.login(new LoginRequest(username, password));
        if (response.authToken() != null){
            postLoginUI(response.authToken());
            return "";
        } else {
            return response.message();
        }
    }


    /**
     * Post login UI
     */

    public void postLoginUI(String authToken){
        System.out.println("Log in success. Select an option below: ");

        String loggedIn = "Logged In";
        while (!loggedIn.equals("Logged out")){
            printPostLoginOptions();
            Scanner scanner = new Scanner(System.in);

            String line = scanner.nextLine();
            try {
                loggedIn = evalPostLogin(line, authToken);
                System.out.print(loggedIn);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }




    public String evalPostLogin(String input, String authToken) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "2" -> logout(authToken);
                case "3" -> createGame(authToken);
                case "4" -> listGames(authToken);
                case "5" -> playGame(authToken);
                case "6" -> observeGame(authToken);
                default -> postLoginHelp();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    /**
     * Post login methods
     */

    private void printPostLoginOptions() {
        System.out.println("1: Help");
        System.out.println("2: Logout");
        System.out.println("3: Create Game");
        System.out.println("4: List Games");
        System.out.println("5: Play Game");
        System.out.println("6: Observer Game");
    }

    private String postLoginHelp(){
        return "Enter 1 to see help options" + "\n" +
                "Enter 2 to Logout" + "\n" +
                "Enter 3 to Create a Game" + "\n" +
                "Enter 4 to List the Games" + "\n" +
                "Enter 5 to Play a Game" + "\n" +
                "Enter 6 to Observe a Game" + "\n";
    }

    private String logout(String authToken) {
        facade.logout(authToken);

        return "Logged out";
    }

    private String observeGame(String authToken) {
        listGames(authToken);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the game number you'd like to observe: ");
        int gameNumber = Integer.parseInt(scanner.nextLine());
        int gameID = gameIDMap.get(gameNumber);
        teamColor = ChessGame.TeamColor.WHITE;
        facade.observeGame(authToken, gameID);
        gameplayUI(authToken, gameID);

        return "";
    }

    private String playGame(String authToken) {
        listGames(authToken);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the game number you'd like to join: ");
        int gameNumber = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the color you'd like to join as, select w for white or b for black. If you don't select either, white will be selected: ");
        String color = scanner.nextLine();
        if (color.equals("b")){
            teamColor = ChessGame.TeamColor.BLACK;
        } else
        {
            teamColor = ChessGame.TeamColor.WHITE;
        }

        facade.joinGame(new ConnectCommand(authToken, gameIDMap.get(gameNumber)));
//        showBoard(gameNumber,authToken, null);

        gameplayUI(authToken, gameNumber);

        return "";
    }

    private String listGames(String authToken) {
        ListGamesResponse listGamesResponse =  facade.listGames(authToken);

        int gameNumber = 1;
        for (GameData game : listGamesResponse.games()){
            System.out.println("Game Number: " + gameNumber + ", Game Name: " + game.getGameName() + ", White Username: " + game.getWhiteUsername() + ", Black Username: " + game.getBlackUsername());
            gameIDMap.put(gameNumber, game.getGameID());
            gameNumber ++;
        }
        return "";
    }

    private String createGame(String authToken) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new game name: ");
        String gameName = scanner.nextLine();

        facade.createGame(new CreateGameRequest(gameName, null), authToken);


        return "";
    }


    /**
     * Gameplay UI
     */

    public void gameplayUI(String authToken, int gameID){
        boolean inProgress = true;
        while (inProgress == true){
            printGameplayOptions();
            Scanner scanner = new Scanner(System.in);

            String line = scanner.nextLine();
            try {
                inProgress = evalGameplay(line, authToken, gameID);
//                System.out.print(loggedIn);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    public boolean evalGameplay(String input, String authToken, int gameID) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "2" -> redrawBoard(authToken);
                case "3" -> leaveGame(authToken, gameID);
                case "4" -> makeMove(authToken, gameID);
                case "5" -> resign(authToken, gameID);
                case "6" -> highlightLegalMoves(authToken, gameID);
                default -> gameplayHelp();
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return true;   //remove this later
    }

    private boolean redrawBoard(String authToken) {
        displayBoard(mostRecentGame.getBoard(), null);
        return true;
    }

    private boolean leaveGame(String authToken, int gameID) {
        teamColor = null;
        facade.leaveGame(new LeaveGameCommand(authToken, gameID));
        return false;
    }
    private boolean makeMove(String authToken, int gameID) {
        System.out.println("Enter the location of the piece you would like to move (Ex: a4): ");
        ChessPosition startPosition = getPositionFromInput();
        System.out.println("Enter the location you would like to move to (Ex: a5): ");
        ChessPosition endPosition = getPositionFromInput();

        ChessPiece.PieceType promotionPieceType = promotionOptions();
        ChessMove newMove = new ChessMove(startPosition, endPosition, promotionPieceType);

        facade.makeMoveInGame(new MakeMoveCommand(authToken, gameID, newMove));
        return true;
    }

    private ChessPiece.PieceType promotionOptions(){
        System.out.println("If you are promoting a pawn, select the option below, or just press 0: ");
        System.out.println("0: No promotion");
        System.out.println("1: Queen");
        System.out.println("2: Bishop");
        System.out.println("3: Knight");
        System.out.println("4: Rook");

        Scanner scanner = new Scanner(System.in);
        int pieceType = scanner.nextInt();
        if (pieceType > 4 || pieceType < 0){
            System.out.println("Incorrect input, try again.");
            return promotionOptions();
        }
        return evalPieceType(pieceType);
    }

    private ChessPiece.PieceType evalPieceType(int pieceType) {
        assert pieceType >= 0 && pieceType <= 4;
        return switch(pieceType){
            case 0 -> null;
            case 1 -> ChessPiece.PieceType.QUEEN;
            case 2 -> ChessPiece.PieceType.BISHOP;
            case 3 -> ChessPiece.PieceType.KNIGHT;
            case 4 -> ChessPiece.PieceType.ROOK;
            default -> throw new IllegalArgumentException("Unexpected value: " + pieceType);
        };
    }

    private boolean resign(String authToken, int gameID) {
        facade.resignGame(new ResignCommand(authToken, gameID));
        return true;
    }

    private boolean highlightLegalMoves(String authToken, int gameID) {
        //takes in the position of the piece to check for
        System.out.println("Enter the location of the piece you would like to check (Example: a4): ");
        Scanner scanner = new Scanner(System.in);
        String location = scanner.nextLine();
        //makes sure that there is a piece there and that it is for the right team potentially
        int row = translateRow(location);
        int col =  ColumnTranslator.translateCol(location);
//        System.out.println("Row = " + row + ". Col = " + col);
        if (row >= 9 || col >= 9){
            System.out.println("Invalid input");
            return highlightLegalMoves(authToken, gameID);
        }
        //get the list of potential moves for that piece
        Collection<ChessMove> chessMoves;
        ChessGame game = mostRecentGame;
        assert game != null;
        if (game.getBoard().getPiece(new ChessPosition(row, col))!= null){
            chessMoves = game.validMoves(new ChessPosition(row, col));
        } else {
            System.out.println("There are no valid moves");
            return true;
        }

        displayBoard(game.getBoard(), chessMoves);

        return true;
    }


    private void printGameplayOptions() {
        System.out.println("1: Help ");
        System.out.println("2: Redraw Chess Board ");
        System.out.println("3: Leave ");
        System.out.println("4: Make Move ");
        System.out.println("5: Resign");
        System.out.println("6: Highlight Legal Moves");
    }

    private boolean gameplayHelp(){
        System.out.println("Enter 1 to see help options" + "\n" +
                "Enter 2 to Redraw the Chess Board" + "\n" +
                "Enter 3 to Leave the Game" + "\n" +
                "Enter 4 to Make a Move" + "\n" +
                "Enter 5 to Resign" + "\n" +
                "Enter 6 to Highlight Legal Moves" + "\n");
        return true;
    }


    /**
     * Helper functions
     */

    private void displayBoard(ChessBoard board, Collection<ChessMove> validMoves){
        BoardCreator boardCreator = new BoardCreator();
        boardCreator.createBoard(teamColor, board, validMoves);
    }

    private int translateRow(String location){
        if (location.length() == 2 && Character.isLetter(location.charAt(0)) && Character.isDigit(location.charAt(1))) {
            // Get the second character and convert it to an integer
            char numberChar = location.charAt(1);
            return Character.getNumericValue(numberChar);
        } else {
            return 10; // out of range
        }
    }

    /**
     * Client Messaging
     */
    @Override
    public void notify(ServerMessage message) {
//        System.out.println("Entering client notifier. Recieved: " + message);
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }

    private void displayNotification(String message){
        System.out.print(SET_TEXT_COLOR_YELLOW);
        System.out.println(message);
        System.out.print(RESET_TEXT_COLOR);
    }

    private void displayError(String message){
        System.out.print(SET_TEXT_COLOR_BLUE);
        System.out.println(message);
        System.out.print(RESET_TEXT_COLOR);
    }

    private void loadGame(ChessGame game){
        System.out.println("Trying to display board.");
        mostRecentGame = game;
        displayBoard(game.getBoard(), null);

    }

    private ChessPosition getPositionFromInput(){
        Scanner scanner = new Scanner(System.in);
        String location = scanner.nextLine();
        //makes sure that there is a piece there and that it is for the right team potentially
        int row = translateRow(location);
        int col = ColumnTranslator.translateCol(location);
        if (row >= 9 || col >= 9){
            System.out.println("Invalid input, try again: ");
            return getPositionFromInput();
        }
        return new ChessPosition(row, col);
    }
}
