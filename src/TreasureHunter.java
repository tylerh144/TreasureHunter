import javax.print.attribute.standard.PresentationDirection;
import java.awt.*;
import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private OutputWindow window = new OutputWindow();
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean test;

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        window.addTextToWindow("Welcome to TREASURE HUNTER! \n", Color.black);
        window.addTextToWindow("Going hunting for the big treasure, eh? \n", Color.black);
        window.addTextToWindow("What's your name, Hunter? ", Color.black);
        String name = SCANNER.nextLine().toLowerCase();
        window.clear();
        // set hunter instance variable
        hunter = new Hunter(name, 20);

        window.addTextToWindow("Easy, normal, or hard mode? (e/n/h): ", Color.red);
        String hard = SCANNER.nextLine().toLowerCase();
        window.clear();
        if (hard.equals("h")) {
            hardMode = true;
        } else if (hard.equals("test")) {
            test = true;
            hunter.changeGold(80);
            hunter.testAdder("boat");
            hunter.testAdder("machete");
            hunter.testAdder("water");
            hunter.testAdder("horse");
            hunter.testAdder("rope");
            hunter.testAdder("boots");
            hunter.testAdder("shovel");
        } else if (hard.equals("e")) {
            hunter.changeGold((hunter.getGold()));
            easyMode = true;
            hunter.setEasyMode(true);
        }  else if (hard.equals("s")) {
            hunter.setSecretSamurai(true);
        } }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        } else if (easyMode) {
            markdown = 1;
            toughness = .25;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, window);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, window);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    public String getDifficulty() {
        if (hardMode) {
            return "h";
        } else if (easyMode) {
            return "e";
        } else {
            return "n";
        }
    }
    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x") && !hunter.isGameOver() && !hunter.isWin()) {
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow(currentTown.getLatestNews(), Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("***\n", Color.black);
            window.addTextToWindow(hunter.infoString(), Color.orange);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow(currentTown.infoString(), Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("\n(B)uy something at the shop.", Color.black);
            window.addTextToWindow("\n(S)ell something at the shop.", Color.black);
            window.addTextToWindow("\n(E)xplore surrounding terrain.", Color.black);
            window.addTextToWindow("\n(M)ove on to a different town.", Color.black);
            window.addTextToWindow("\n(L)ook for trouble!", Color.black);
            window.addTextToWindow("\n(H)unt for treasure.", Color.black);
            window.addTextToWindow("\n(D)ig for gold.", Color.black);
            window.addTextToWindow("\nGive up the hunt and e(X)it.", Color.black);
            window.addTextToWindow("\n", Color.black);
            window.addTextToWindow("\nWhat's your next move? ", Color.black);
            choice = SCANNER.nextLine().toLowerCase();
            window.clear();
            processChoice(choice);
        }
        if (hunter.isWin()) {
            window.clear();
            window.addTextToWindow(currentTown.getLatestNews(), Color.black);
            window.addTextToWindow("Congratulations, you have found the last of the three treasures, you win!", Color.yellow);
        } else if (hunter.isGameOver()) {
            window.addTextToWindow("\nWhat? You don't have enough money to pay up...then you're going to have to pay with your life!\n", Color.red);
            window.addTextToWindow("\nThe stranger comes up to you, weapon in hand, malice unshakable. Their hits connect to your body and you begin to feel numb all over. ", Color.red);
            window.addTextToWindow("As if it were just a dream, you try to get back up. But the pain you feel in your bones overwhelms you. ", Color.red);
            window.addTextToWindow("Your body collapses to the ground, as if all of your matter begins to dematerializes. You hear crazed laughter from above. ", Color.red);
            window.addTextToWindow("Your connection to the mortal world begins to unravel, string by string. You cannot hold on any longer. ", Color.red);
            window.addTextToWindow("It seems like the treasure cannot be hunted after all... ", Color.red);
            window.addTextToWindow("\nEverything goes dark and you die.", Color.red);
        } else {
            window.addTextToWindow("Fare thee well, ", Color.black);
            window.addTextToWindow(hunter.getHunterName(), Color.MAGENTA);
            window.addTextToWindow("!", Color.black);
        }
    }


    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("e")) {
            window.addTextToWindow(currentTown.getTerrain().infoString(), Color.black);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                window.addTextToWindow(currentTown.getLatestNews(), Color.black);
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("h")){
            currentTown.treasureHunt();
        } else if (choice.equals("d")) {
            currentTown.digForGold();
        } else if (!choice.equals("x")) {
            window.addTextToWindow("Yikes! That's an invalid option! Try again.", Color.black);
        }
    }
}