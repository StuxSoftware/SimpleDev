package net.stuxcrystal.permissions;

/**
 * Method to check the permission support.
 */
public interface SupportList {

    /**
     * Does the backend support multi-world.
     * @return true if so.
     */
    public boolean hasMultiWorldSupport();

    /**
     * Does the backend support global permissions.
     * @return true if so.
     */
    public boolean hasGlobalPermissionSupport();

    /**
     * Does the backend support groups.
     * @return true if so.
     */
    public boolean hasGroupSupport();

    /**
     * Does the backend support permissions.
     * @return true if so.
     */
    public boolean hasPermissionSupport();

    /**
     * Does the backend support temporary permissions.
     * @return true if so.
     */
    public boolean hasTemporaryPermissionSupport();

    /**
     * Does the backend support temporary groups.
     * @return true if so.
     */
    public boolean hasTemporaryGroupSupport();

    /**
     * Does the backend support modifying permissions.
     * @return true if so.
     */
    public boolean hasWriteSupport();

    /**
     * Does the backend support operators.
     * @return true if so.
     */
    public boolean hasOperatorSupport();

}
