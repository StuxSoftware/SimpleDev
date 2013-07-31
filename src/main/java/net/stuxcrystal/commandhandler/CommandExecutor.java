package net.stuxcrystal.commandhandler;

/**
 * Represents the sender of a command.
 */
public abstract class CommandExecutor<T> {

    /**
     * @return Returns the name of the sender.
     */
    public abstract String getName();

    /**
     * Sends one or more messages.
     *
     * @param message The messages to send.
     */
    public abstract void sendMessage(String... message);

    /**
     * Checks if the sender has a certain permission.
     *
     * @param node The node to test.
     * @return true if the sender has the permission needed.
     */
    public abstract boolean hasPermission(String node);

    /**
     * Returns the type of the sender.
     *
     * @return true if the sender is a player.
     */
    public abstract boolean isPlayer();

    /**
     * Returns the type of the player.
     *
     * @return true if the sender is the console or is op. Otherwise false.
     */
    public abstract boolean isOp();

    /**
     * Used to return a handle.
     *
     * @return
     */
    public abstract T getHandle();

    /**
     * Compare the underlying handles.
     *
     * @param otherObject an other object.
     * @return true if the executor have the same handle.
     */
    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof CommandExecutor)) return false;
        T me = this.getHandle();
        Object other = ((CommandExecutor) otherObject).getHandle();

        if (me == null || other == null) return false;
        return me.equals(other);
    }

    /**
     * Use the hash code of the underlying object.
     *
     * @return
     */
    @Override
    public int hashCode() {
        T me = this.getHandle();
        if (me == null) return super.hashCode();
        return me.hashCode();
    }

}
