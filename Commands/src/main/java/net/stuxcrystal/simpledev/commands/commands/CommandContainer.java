package net.stuxcrystal.simpledev.commands.commands;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;

import java.util.List;

/**
 * <p>The command container.</p>
 *
 * <p>
 *     <b>Make sure that no return value is null!</b>
 * </p>
 *
 * <p>
 *     The default fallback command name is " ".
 * </p>
 */
public interface CommandContainer {

    /**
     * Returns the name of the command.
     * @return The name of the command.
     */
    public String getName();

    /**
     * Returns the aliases of the command.
     * @return The aliases of the command.
     */
    public List<String> getAliases();

    /**
     * Returns the permission node needed for the permission.
     * @return The permission.
     */
    public String getPermission();

    /**
     * Returns the description of the command.
     * @return The description of the command.
     */
    public String getDescription();

    /**
     * Check if players are allowed to call this method.
     * @return {@code true} if players are allowed to use this command.
     */
    public boolean allowPlayers();

    /**
     * Check if non players can execute this command.
     * @return {@code true} if the console can use this command.
     */
    public boolean allowConsole();

    /**
     * Check if only operators are allowed to use this command if no permission system has been installed.
     * @return {@code true} if this is an operator command.
     */
    public boolean isOperatorCommand();

    /**
     * Checks if the underlying command is an asynchronous command.
     * @return {@code true} if so.
     */
    public boolean isAsyncCommand();

    /**
     * Returns all supported flags.
     * @return All supported flags.
     */
    public String getSupportedFlags();

    /**
     * <p>Returns the minimal size of arguments that need to be passed.</p>
     *
     * <p>{@code -1} means we don't care.</p>
     *
     * @return The minimal size of arguments.
     */
    public int getMinimalArgumentCount();

    /**
     * <p>Returns the maximal size of arguments that can be used.</p>
     *
     * * <p>{@code -1} means we don't care.</p>
     * @return The maximal size of arguments.
     */
    public int getMaximalArgumentCount();

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Should the container parse arguments.
     * @return {@code true} if the manager should parse the arguments.
     */
    public boolean parseArguments();

    /**
     * Executes the command.<p />
     *
     * Requires parseArguments()==true; when this method should be called.
     *
     * @param executor  The executor that has executed the command.
     * @param parser    The parser that parsed the commands.
     */
    public void execute(CommandExecutor executor, ArgumentList parser);

    /**
     * Executes the command without parsing the arguments.<p />
     *
     * Requires parseArguments()==false; when called.<p />
     *
     * <i>Please note that basic argument checks will not be executed!</i>
     *
     * @param executor  The executor that has executed the command.
     * @param args      The arguments that have been passed.
     */
    public void execute(CommandExecutor executor, String[] args);
}
