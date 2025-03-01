package io.github.nosequel.menu.pagination;

import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import io.github.nosequel.menu.filling.FillingType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;

@Getter
@Setter
public abstract class PaginatedMenu extends Menu {

    private NavigationPosition navigationPosition = NavigationPosition.TOP;

    private Button previousPageButton = new Button(Material.MELON)
            .setDisplayName(ChatColor.GREEN + "Previous Page");

    private Button nextPageButton = new Button(Material.MELON)
            .setDisplayName(ChatColor.GREEN + "Next Page");

    private int page = 1;
    private int maxPages;
    private Map<Button, Integer> addedNavButtons = new HashMap<>();

    /**
     * Constructor to make a new menu object
     *
     * @param player the player to create the menu for
     * @param title  the title to display at the top of the inventory
     * @param size   the size of the inventory
     */
    public PaginatedMenu(Player player, String title, int size) {
        this(player, title, size, 16);
    }

    /**
     * Constructor to make a new menu object
     *
     * @param player the player to create the menu for
     * @param title  the title to display at the top of the inventory
     * @param size   the size of the inventory
     * @param maxPages the maximum amount of pages
     */
    public PaginatedMenu(Player player, String title, int size, int maxPages) {
        super(player, title, size);
        this.maxPages = maxPages;
        this.buttons = new Button[size * maxPages];
    }

    /**
     * Navigate to the next menu page
     */
    public void navigateNext() {
        this.page += 1;
        this.updateMenu();
    }

    /**
     * Navigate to the previous menu page
     */
    public void navigatePrevious() {
        this.page = Math.max(1, this.page - 1);
        this.updateMenu();
    }

    /**
     * Update the menu for the player
     */
    @Override
    public void updateMenu() {
        this.updateMenu(this.getButtonsInRange());
        this.updateMenu(this.getButtonsInRange());
    }

    /**
     * Handle clicking on a button
     *
     * @param event the event called
     */
    @Override
    public void click(InventoryClickEvent event) {
        try {
            final Button[] buttons = this.getButtonsInRange();
            final Button button = buttons[event.getSlot()];

            if (button == null) {
                event.setCancelled(true);
                return;
            }

            if (button.getClickAction() != null) {
                button.getClickAction().accept(event);
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
    }

    /**
     * Get the filler buttons for the menu
     *
     * @return the filler buttons
     */
    @Override
    public Button[] getFillerButtons() {
        final Button[] buttons = new Button[this.getSize()];

        for (FillingType filler : this.getFillers()) {
            final Button[] fillers = filler.fillMenu(PaginatedMenu.this);

            for (int i = 0; i < fillers.length; i++) {
                if (fillers[i] != null) {
                    for (int page = 0; page < this.maxPages; page++) {
                        this.buttons[((page * (this.getSize() - 9)) + i) + (9 * (page + 1))] = fillers[i];
                    }
                }
            }
        }

        return buttons;
    }

    /**
     * Get the list of buttons in the
     * range of the current page.
     *
     * @return the list of buttons
     */
    public Button[] getButtonsInRange() {
        return this.navigationPosition.getButtonsInRange(this.getButtons(), this);
    }

    /**
     * Get the list of buttons for the navigation bar.
     * <p>
     * These buttons will be displayed independent
     * of the current page of the menu.
     *
     * @return the list of buttons
     */
    public Button[] getNavigationBar() {
        Button[] navButtons = this.navigationPosition.getNavigationButtons(this).clone();
        for (Button button : addedNavButtons.keySet()) {
            navButtons[addedNavButtons.get(button)] = button;
        }

        return navButtons;
    }

    public int usedButtons() {
        int count = 0;
        for (Button button : buttons) {
            if (button != null && !button.getDisplayName().equals(" ")) count++;
        }

        return count;
    }

    public void addNavigationButton(Button button, int position) {
        this.addedNavButtons.put(button, position);
    }
}