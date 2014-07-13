package com.gateway.event;

import com.google.common.collect.FluentIterable;

import static java.util.Arrays.asList;

public final class NavDrawerSelectionEvent {

    public static enum NavItem {
        login("Login", 0),
        username("username", 0),
        listings("Listings", 1);

        String name;
        int listIndex;

        NavItem(String name, int index) {
            this.name = name;
            this.listIndex = index;
        }

        @Override public String toString() { return this.name; }

        public static NavItem findByIndex(int index) {
            for (NavItem navItem : values()) {
                if (navItem.listIndex == index) {
                    return navItem;
                }
            }

            return null;
        }

        public static int indexOf(NavItem navItem) {
            return asList(values()).indexOf(navItem);
        }
    }

    private NavItem navItem;

    public NavDrawerSelectionEvent(NavItem navItem) {
        this.navItem = navItem;
    }

    public NavItem getNavItem() {
        return navItem;
    }
}
