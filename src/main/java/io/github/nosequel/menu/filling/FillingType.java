package io.github.nosequel.menu.filling;

import io.github.nosequel.menu.Menu;
import io.github.nosequel.menu.buttons.Button;
import io.github.nosequel.menu.pagination.PaginatedMenu;

public enum FillingType {

    BORDER {
        /**
         * Fill the slots inside of the menu with the
         * specified filling type.
         *
         * @param menu the menu to fill
         * @return the filling buttons
         */
        @Override
        public Button[] fillMenu(Menu menu) {
            boolean paginated = menu instanceof PaginatedMenu;
            int newSize = paginated ? menu.getSize() - 9 : menu.getSize();

            final Button[] buttons = new Button[newSize];

            for (int i = 0; i < newSize; i++) { // for each page
                    //top    //bottom            // side 1     //side 2
                if (i < 9 || i >= newSize - 9 || i % 9 == 0 || i % 9 == 8) { // for borders
                    buttons[i] = new Button(menu.getFillerType().getType())
                            .setData(menu.getFillerType().getData().getData())
                            .setDisplayName(" ")
                            .setClickAction(event -> event.setCancelled(true));
                }
            }

            return buttons;
        }
    },

    EMPTY_SLOTS {
        /**
         * Fill the slots inside of the menu with the
         * specified filling type.
         *
         * @param menu the menu to fill
         * @return the filling buttons
         */
        @Override
        public Button[] fillMenu(Menu menu) {
            final Button[] buttons = new Button[menu.getSize()];

            for (int i = 0; i < menu.getSize(); i++) {
                buttons[i] = new Button(menu.getFillerType().getType())
                        .setData(menu.getFillerType().getData().getData())
                        .setDisplayName(" ")
                        .setClickAction(event -> event.setCancelled(true));

            }

            return buttons;
        }
    };

    /**
     * Fill the slots inside of the menu with the
     * specified filling type.
     *
     * @param menu the menu to fill
     * @return the filling buttons
     */
    public abstract Button[] fillMenu(Menu menu);
}