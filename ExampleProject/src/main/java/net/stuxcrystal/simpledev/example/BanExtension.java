package net.stuxcrystal.simpledev.example;

/**
 * The extension to interface with ban systems.
 */
public interface BanExtension {

    /**
     * Bans a player.
     * @param banned  {@code true} if the player should be banned; {@code false} otherwise.
     */
    public void ban(boolean banned);

    /**
     * Checks if a player is banned.
     * @return {@code true} if the player is banned.
     */
    public boolean isBanned();

}
