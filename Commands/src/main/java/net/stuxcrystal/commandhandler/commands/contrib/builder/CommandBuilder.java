package net.stuxcrystal.commandhandler.commands.contrib.builder;

import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.commands.CommandContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * A builder for new commands.
 */
public class CommandBuilder {

    private String name = null;

    private List<String> aliases = new ArrayList<>();

    private String permission = "";

    private String description = "";

    private boolean allowPlayers = false;

    private boolean allowConsole = false;

    private boolean opCommand = false;

    private boolean async = false;

    private String flags = "";

    private int minSize = -1;

    private int maxSize = -1;

    /**
     * Creates the builder.
     */
    public CommandBuilder() {}

    /**
     * Sets the name of the command.
     * @param newName The new name of the command.
     * @return The builder itself.
     */
    public CommandBuilder name(String newName) {
        this.name = newName;
        return this;
    }

    /**
     * Adds an alias for the command.
     * @param newAlias The new alias.
     * @return The builder itself.
     */
    public CommandBuilder alias(String newAlias) {
        this.aliases.add(newAlias);
        return this;
    }

    /**
     * Sets the permission for this command builder.
     * @param newPermission The permission for this command builder.
     * @return The builder itself.
     */
    public CommandBuilder permission(String newPermission) {
        this.permission = newPermission;
        return this;
    }

    /**
     * Sets if the console is allowed to use this command.
     * @param allowConsole {@code true} if it is allowed.
     * @return The builder itself.
     */
    public CommandBuilder console(boolean allowConsole) {
        this.allowConsole = allowConsole;
        return this;
    }

    /**
     * Sets if the player is allowed to use this command.
     * @param player {@code true} if it is allowed.
     * @param opOnly {@code true} if only ops can use this command.
     * @return The builder itself.
     */
    public CommandBuilder player(boolean player, boolean opOnly) {
        this.allowPlayers = player;
        this.opCommand = opOnly;
        return this;
    }

    /**
     * Sets if the player is allowed to use this command.
     * @param player {@code true} if a player can use this command.
     * @return The builder itself.
     */
    public CommandBuilder player(boolean player) {
        return this.player(player, true);
    }

    /**
     * Sets if the command can be executed asynchronously.
     * @param flag {@code true} if the command should be executed asynchronously.
     * @return The builder itself.
     */
    public CommandBuilder async(boolean flag) {
        this.async = flag;
        return this;
    }

    /**
     * Flags for the parser of the command.
     *
     * @param flags     The flags that are accepted.
     * @param minCount  The minimal argument count. (-1 => Unlimited)
     * @param maxCount  The maximal argument count. (-1 => Unlimited)
     * @return The builder itself.
     */
    public CommandBuilder parser(String flags, int minCount, int maxCount) {
        this.parser(flags);
        this.parser(minCount, maxCount);
        return this;
    }

    /**
     * Sets the flags for the parser of the command.
     *
     * @param flags     The flags that are accepted.
     * @return The builder itself.
     */
    public CommandBuilder parser(String flags) {
        this.flags = flags;
        return this;
    }

    /**
     * Flags for the parser of the command.
     *
     * @param minCount  The minimal argument count. (-1 => Unlimited)
     * @param maxCount  The maximal argument count. (-1 => Unlimited)
     * @return The builder itself.
     */
    public CommandBuilder parser(int minCount, int maxCount) {
        this.minSize = minCount;
        this.maxSize = maxCount;
        return this;
    }

    /**
     * Creates a new command
     * @param listener That executes this listener.
     * @return The command object.
     */
    public CommandContainer create(CommandListener listener) {
        return new ListenerCommandContainer(
                name, aliases, permission, description,
                allowPlayers, allowConsole, opCommand,
                async,
                flags, minSize, maxSize,
                listener
        );
    }

    /**
     * Creates a new command with sub-commands.
     * @param handler The handler for sub-commands.
     * @return The command object.
     */
    public CommandContainer create(CommandHandler handler) {
        return new SubCommandContainer(
                name, aliases, permission, description,
                allowPlayers, allowConsole, opCommand,
                async,
                flags, minSize, maxSize,
                handler
        );
    }

}
