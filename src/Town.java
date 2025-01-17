import java.awt.*;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private OutputWindow window;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean searched;
    private String treasure;
    private boolean dug;
    private String recentMsg;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, OutputWindow window) {
        this.shop = shop;
        this.window = window;
        this.terrain = getNewTerrain();

        dug = false;
        searched = false;
        double rand = Math.random();
        if (rand < .2) {
            treasure = "crown";
        } else if (rand < .4) {
            treasure = "trophy";
        } else if (rand < .6) {
            treasure = "gem";
        } else {
            treasure = "dust";
        }

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    public void setPrintMessage(String printMessage) {
        this.printMessage = printMessage;
    }

    public String getRecentMsg() {
        return recentMsg;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage +="\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            String coloredItem = item;
            printMessage = "You used your " + coloredItem + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + coloredItem + ".";
            }
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            window.addTextToWindow("\nYou couldn't find any trouble", Color.blue);
            printMessage = "A brawl has not occurred.";
        } else {
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (hunter.hasItemInKit("sword")) {
                window.addTextToWindow("\nThat's a mighty fine sword there, just take some gold; I'm not in the mood for a brawl.", Color.blue);
                window.addTextToWindow("You won the brawl and receive " + goldDiff + " gold.", Color.green);
                printMessage = "You recently won a brawl.";
                hunter.changeGold(goldDiff);
            } else {
                window.addTextToWindow("\nYou want trouble, stranger!  You got it!\nOof! Umph! Ow!", Color.red);
                if (Math.random() > noTroubleChance) {
                    window.addTextToWindow("Okay, stranger! You proved yer mettle. Here, take my gold.", Color.green);
                    window.addTextToWindow("You won the brawl and receive " + goldDiff +" gold.", Color.green);
                    printMessage = "You recently won a brawl.";
                    hunter.changeGold(goldDiff);
                } else {
                    window.addTextToWindow("That'll teach you to go lookin' fer trouble in MY town! Now pay up!", Color.red);
                    window.addTextToWindow("You lost the brawl and pay "+ goldDiff +" gold.", Color.red);
                    printMessage = "You recently lost a brawl.";
                    hunter.changeGold(-goldDiff);
                }
            }
        }
    }

    public String infoString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    public void treasureHunt() {
        if (!searched) {
            window.addTextToWindow("\nYou found a " + treasure + ".", Color.black);
            if (!treasure.equals("dust")) {
                if (toughTown && Math.random() < .7) {
                    window.addTextToWindow("However, you accidentally drop the " + treasure + " before putting it in your bag.", Color.red);
                    printMessage = "You dropped the treasure.";
                } else {
                    hunter.addTreasure(treasure);
                    printMessage = "You recently obtained some treasure.";
                }
            } else {
                printMessage = "You did not find treasure in this town.";
            }
            searched = true;
        } else {
            window.addTextToWindow("\nYou have already searched this town.", Color.red);
            printMessage = "";
        }
    }

    public void digForGold() {
        if (!dug) {
            if (hunter.hasItemInKit("shovel")) {
                if (Math.random() < .5) {
                    int gold = (int) (Math.random() * 20) + 1;
                    hunter.changeGold(gold);
                    window.addTextToWindow("\nYou dug up " + gold + " gold!", Color.orange);
                    printMessage = "You recently dug up some gold.";
                } else {
                    window.addTextToWindow("\nYou dug but only found dirt", Color.red);
                    printMessage = "You recently dug up some dirt.";
                }
                dug = true;
            } else {
                window.addTextToWindow("\nYou can't dig for gold without a " + "shovel.", Color.red);
                printMessage = "Buy a shovel.";
            }
        } else {
            window.addTextToWindow("\nYou already dug for gold in this town.", Color.red);
            printMessage = "";
        }
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < .166666666) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < .3333333333) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < .5) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < .666666666666) {
            return new Terrain("Desert", "Water");
        } else if (rnd < .833333333333){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        if (hunter.isEasyMode()) {
            return (false);
        }
        double rand = Math.random();
        return (rand < 0.5);
    }
}