package com.app.remi.test.network.backend;

/**
 * Used by polymorphism
 * Allow the backend to uses any client in a transparent way.
 */
public interface Displayable {
    public abstract void handleReception(String textReceived);
}
