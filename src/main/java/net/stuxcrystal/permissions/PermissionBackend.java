package net.stuxcrystal.permissions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Backend for permission systems.
 */
public interface PermissionBackend extends SupportList {

    /**
     * Checks if the user is a operator.
     * @param user The user to check.
     * @return True if the given user is a operator.
     */
    public boolean isOperator(@Nonnull String user);


    /**
     * Checks if the user has the permission needed.
     * @param user  The user to check.
     * @param node  The node to check
     * @param world The world to check or null if a global permission should be checked..
     * @return true if the user has the permission node needed.
     * @throws UnsupportedOperationException If the world passed was not supported or nodes are not supported.
     */
    public boolean hasUserPermission(@Nonnull String user, @Nonnull String node, @Nullable String world)
        throws UnsupportedOperationException;

    /**
     * Checks if the group has the permission needed.
     * @param group The group to check.
     * @param node  The node to check.
     * @param world The world to check or null if a global permission should be checked..
     * @return true if the user has the permission node needed.
     * @throws UnsupportedOperationException If the backend does not support groups or does not support multiworld.
     */
    public boolean hasGroupPermission(@Nonnull String group, @Nonnull String node, @Nullable String world)
        throws UnsupportedOperationException;


    /**
     * Adds a permission to the user.
     * @param user  The user to manipulate.
     * @param node  The node to add.
     * @param world The world to use or null if the permission should be set globally.
     * @param time  The time to use if the time is 0, the permission will be given permenently.
     * @return UnsupportedOperationException If the backend does support not some functions of the method.
     */
    public void addUserPermission(@Nonnull String user, @Nonnull String node, @Nullable String world, int time)
        throws UnsupportedOperationException;

    /**
     * Removes a permission node from the user.
     * @param user  The user to manipulate.
     * @param node  The node to remove.
     * @param world The world to use or null if the permission should be removed globally.
     * @return UnsupportedOperationException If the backend does not support some functions of the method.
     */
    public void removeUserPermission(@Nonnull String user, @Nonnull String node, @Nullable String world)
        throws UnsupportedOperationException;


    /**
     * Adds a permission to the user.
     * @param group  The user to manipulate.
     * @param node  The node to add.
     * @param world The world to use or null if the permission should be set globally.
     * @param time  The time to use if the time is 0, the permission will be given permenently.
     * @return UnsupportedOperationException If the backend does not support some functions of the method.
     */
    public void addGroupPermission(@Nonnull String group, @Nonnull String node, @Nullable String world, int time)
            throws UnsupportedOperationException;

    /**
     * Removes a permission node from a group.
     * @param group  The user to manipulate.
     * @param node  The node to remove.
     * @param world The world to use or null if the permission should be removed globally.
     * @return UnsupportedOperationException If the backend does not support some functions of the method.
     */
    public void removeGroupPermission(@Nonnull String group, @Nonnull String node, @Nullable String world)
            throws UnsupportedOperationException;


    /**
     * Creates a group.
     * @param group The name of the group.
     * @param world The world where the group should be created or null if the group should be created globally.
     * @return UnsupportedOperationException If the backend does not support some functions of the method.
     */
    public void createGroup(@Nonnull String group, @Nullable String world) throws UnsupportedOperationException;

    /**
     * Deletes a group.
     * @param group The group to delete.
     * @param world
     * @throws UnsupportedOperationException
     */
    public void deleteGroup(@Nonnull String group, @Nullable String world) throws UnsupportedOperationException;


}
