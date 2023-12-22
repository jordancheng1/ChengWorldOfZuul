/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 *
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room room11, room12, room13, room14, room15,
            room21, room22, room23, room24, room25,
            room31, room32, room33, room34, room35,
            room41, room42, room43, room44, room45,
            room51, room52, room53, room54, room55;

        // create the rooms
        room11 = new Room("in room on point (1, 1) It's a dead end!");
        room12 = new Room("in room on point (1, 2)");
        room13 = new Room("in room on point (1, 3)");
        room14 = new Room("in room on point (1, 4)");
        room15 = new Room("in room on point (1, 5) It's a dead end!");
        room21 = new Room("in room on point (2, 1) It's a dead end!");
        room22 = new Room("in room on point (2, 2)");
        room23 = new Room("in room on point (2, 3)");
        room24 = new Room("in room on point (2, 4)");
        room25 = new Room("in room on point (2, 5)");
        room31 = new Room("in room on point (3, 1)");
        room32 = new Room("in room on point (3, 2)");
        room33 = new Room("in the center of the maze. Point (3, 3)");
        room34 = new Room("in room on point (3, 4)");
        room35 = new Room("in room on point (3, 5)");
        room41 = new Room("in room on point (4, 1) It's a dead end!");
        room42 = new Room("in room on point (4, 2)");
        room43 = new Room("in room on point (4, 3)");
        room44 = new Room("in room on point (4, 4)");
        room45 = new Room("in room on point (4, 5)");
        room51 = new Room("in room on point (5, 1) It's a dead end!");
        room52 = new Room("in room on point (5, 2)");
        room53 = new Room("in room on point (5, 3)");
        room54 = new Room("in room on point (5, 4) It's a dead end!");
        room55 = new Room("in room on point (5, 5) It's a dead end!");


        // initialise room exits (north, east, south, west)
        room11.setExits(room12, null, null, null);
        room12.setExits(room13, room22, room11, null);
        room13.setExits(room14, room23, room12, null);
        room14.setExits(null, room24, room13, null);
        room15.setExits(null, room25, null, null);
        room21.setExits(null, room31, null, null);
        room22.setExits(room23, null, null, room12);
        room23.setExits(room24, null, room22, room13);
        room24.setExits(room25, room34, room23, room14);
        room25.setExits(null, null, room24, room15);
        room31.setExits(room32, room41, null, room21);
        room32.setExits(room33, room42, room31, null);
        room33.setExits(null, room43, room32, null);
        room34.setExits(room35, room44, null, room24);
        room35.setExits(null, room45, room34, null);
        room41.setExits(null, null, null, room31);
        room42.setExits(room43, room52, null, room32);
        room43.setExits(room44, room53, room42, room33);
        room44.setExits(room45, room54, room43, room34);
        room45.setExits(null, room55, room44, room35);
        room51.setExits(room52, null, null, null);
        room52.setExits(room53, null, room51, room42);
        room53.setExits(null, null, room52, room43);
        room54.setExits(null, null, null, room44);
        room55.setExits(null, null, null, room45);

        currentRoom = room33;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("You are " + currentRoom.getDescription());
        System.out.print("You can go: ");
        if(currentRoom.northExit != null) {
            System.out.print("north ");
        }
        if(currentRoom.eastExit != null) {
            System.out.print("east ");
        }
        if(currentRoom.southExit != null) {
            System.out.print("south ");
        }
        if(currentRoom.westExit != null) {
            System.out.print("west ");
        }
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    /**
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = currentRoom.northExit;
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.eastExit;
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.southExit;
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.westExit;
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println("You are " + currentRoom.getDescription());
            System.out.print("Exits: ");
            if(currentRoom.northExit != null) {
                System.out.print("north ");
            }
            if(currentRoom.eastExit != null) {
                System.out.print("east ");
            }
            if(currentRoom.southExit != null) {
                System.out.print("south ");
            }
            if(currentRoom.westExit != null) {
                System.out.print("west ");
            }
            System.out.println();
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}