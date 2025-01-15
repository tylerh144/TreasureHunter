/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean searched;
    private String treasure;
    private boolean dug;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness) {
        this.shop = shop;
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

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + Colors.PURPLE + hunter.getHunterName() + Colors.RESET + ".";
        if (toughTown) {
            printMessage += Colors.RED + "\nIt's pretty rough around here, so watch yourself." + Colors.RESET;
        } else {
            printMessage += Colors.GREEN + "\nWe're just a sleepy little town with mild mannered folk." + Colors.RESET;
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
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak()) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + Colors.PURPLE + terrain.getNeededItem() + Colors.RESET + ".";
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
            printMessage = Colors.BLUE + "You couldn't find any trouble" + Colors.RESET;
        } else {
            if (hunter.hasItemInKit("sword")) {
                int goldDiff = (int) (Math.random() * 10) + 1;
                System.out.println("That's a mighty fine sword there, just take some gold; I'm not in the mood for a brawl.");
                printMessage += Colors.GREEN + "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.GREEN + " gold." + Colors.RESET;
                hunter.changeGold(goldDiff);
            } else {
                printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
                int goldDiff = (int) (Math.random() * 10) + 1;
                if (Math.random() > noTroubleChance) {
                    printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold." + Colors.RESET;
                    printMessage += Colors.GREEN + "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.GREEN + " gold." + Colors.RESET;
                    hunter.changeGold(goldDiff);
                } else {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                    printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + Colors.RED + " gold." + Colors.RESET;
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
            System.out.println("\nYou found a " + Colors.YELLOW + treasure + Colors.RESET);
            if (!treasure.equals("dust")) {
                hunter.addTreasure(treasure);
            }
            searched = true;
        } else {
            System.out.println("\nYou have already searched this town.");
        }
    }

    public void digForGold() {
        if (!dug) {
            if (hunter.hasItemInKit("shovel")) {
                if (Math.random() < .5) {
                    int gold = (int) (Math.random() * 20) + 1;
                    hunter.changeGold(gold);
                    System.out.println("You dug up " + Colors.YELLOW + gold + Colors.RESET + " gold!");
                } else {
                    System.out.println("You dug but only found dirt");
                }
                dug = true;
            } else {
                System.out.println("You can't dig for gold without a shovel");
            }
        } else {
            System.out.println("You already dug for gold in this town.");
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