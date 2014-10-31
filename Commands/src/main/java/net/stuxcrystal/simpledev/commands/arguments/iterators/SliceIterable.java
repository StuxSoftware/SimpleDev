package net.stuxcrystal.simpledev.commands.arguments.iterators;

/**
 * Returns the iterable at the given item.
 */
class SliceIterable extends ArgumentIterable {

    /**
     * The start index of the iterable.
     */
    final int start;

    /**
     * The stop index of the iterable.
     */
    final int stop;

    /**
     * The step index of the iterable.
     */
    final int step;

    /**
     * Creates a new slice iterable.
     * @param parent    The parent iterable.
     * @param start     The first item to return.
     * @param stop      The first item not to be returned.
     * @param step      The step.
     */
    SliceIterable(ArgumentIterable parent, Integer start, Integer stop, Integer step) {
        super(parent);

        int length = parent.getArgumentParser().count();

        // The default value of step is always 1
        if (step == null)
            step = 1;

        // Determine the correct default value for start.
        if (start == null)
            start = (step>0)?0:length-1;
        else if (start < 0)
            start += length-1;

        // Determine the correct default value for stop.
        if (stop == null)
            stop = (step > 0)?length:-1;
        else if (stop < 0)
            stop += length;

        // Assign the values.
        this.start = start;
        this.stop = stop;
        this.step = step;
    }

    @Override
    public <T> T getArgument(int index, Class<T> cls) {
        int i = start + step*index;

        if ((step>0)?(i<stop):(i>stop))
            throw new ArrayIndexOutOfBoundsException(index);

        return this.getParent().getArgument(index, cls);
    }
}
