/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private String[] kit;
    private int gold;
    private boolean gameOver;
    private boolean win;
    private boolean easyMode;
    private boolean secretSamurai;
    private String[] treasures;
    private int count;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     * @param startingGold The gold the hunter starts with.
     */
    public Hunter(String hunterName, int startingGold) {
        this.hunterName = hunterName;
        kit = new String[8]; // only 5 possible items can be stored in kit
        treasures = new String[3];
        count = 0;
        gold = startingGold;
        gameOver = false;
    }

    //Accessors
    public String getHunterName() {
        return hunterName;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            gold = 0;
            gameOver = true;
        }
    }

    public void setSecretSamurai(boolean secretSamurai) {
        this.secretSamurai = secretSamurai;
    }

    public boolean isSecretSamurai() {
        return secretSamurai;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWin() {
        return win;
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (gold < costOfItem || hasItemInKit(item)) {
            return false;
        }
        gold -= costOfItem;
        addItem(item);
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item)) {
            return false;
        }
        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    public int getGold() {
        return gold;
    }

    public boolean isEasyMode() {
        return easyMode;
    }

    public void setEasyMode(boolean easyMode) {
        this.easyMode = easyMode;
    }

    public String[] getKit() {
        return kit;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }
        return false;
    }

    public void testAdder(String item) {
        int idx = emptyPositionInKit();
        kit[idx] = item;
    }

    /**
     * Checks if the kit Array has the specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

     /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
    public String getInventory(String[] array) {
        String printableKit = "";

        for (String item : array) {
            if (item != null) {
                printableKit += item + " ";
            }
        }
        return printableKit;
    }


    /**
     * @return A string representation of the hunter.
     */
    public String infoString() {
        String str = hunterName + " has " + gold + " gold";
        if (!isEmpty(kit)) {
            str += " and " + getInventory(kit);
        }
        if (!isEmpty(treasures)) {
            str += "\nand "+ getInventory(treasures);
        }
        return str;
    }

    /**
     * Searches kit Array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @return true if kit is completely empty.
     */
    private boolean isEmpty(String[] array) {
        for (String string : array) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first index where there is a null value.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void addTreasure(String treasure) {
        for (int i = 0; i < treasures.length; i++) {
             if (treasures[i] == null) {
                treasures[i] = treasure;
                count++;
                break;
            } else if (treasures[i].equals(treasure)) {
                System.out.println("You already have a " + Colors.YELLOW + treasure + Colors.RESET + ", so you put it back.");
                break;
            }
        }
        if (count == 3) {
            win = true;
        }
    }
}