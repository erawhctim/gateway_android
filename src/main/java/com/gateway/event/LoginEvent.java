package com.gateway.event;

/**
 * Posted on the bus for the Login page to update its button
 */
public final class LoginEvent {

    private boolean success;

    public LoginEvent(boolean success) { this.success = success; }

    public boolean isSuccess() {
        return success;
    }
}
