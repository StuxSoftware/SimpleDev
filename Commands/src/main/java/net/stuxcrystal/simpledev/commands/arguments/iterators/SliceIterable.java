package net.stuxcrystal.simpledev.commands.arguments.iterators;

/**
 * Returns the iterable at the given item.
 */
class SliceIterable extends ArgumentIterable {

    private static class Slice {
        /**
         * The start index of the iterable.
         */
        private final Integer start;

        /**
         * The stop index of the iterable.
         */
        private final Integer stop;

        /**
         * The step index of the iterable.
         */
        private final Integer step;

        /**
         * The length of the slice.
         */
        private final Integer len;

        /**
         * Creates a new slice object.
         * @param start The first argument.
         * @param stop  The second argument.
         * @param step  The step or stride.
         * @param len   The length of the slice.
         */
        private Slice(Integer start, Integer stop, Integer step, Integer len) {
            // Assign the values.
            this.start = start;
            this.stop = stop;
            this.step = step;
            this.len = len;
        }

        /**
         * Returns the length of the slice.
         * @return The length of the slice.
         */
        public int getLength() {
            if (this.len == null)
                throw new IllegalStateException("Cannot calculate length of the slice.");
            return this.len;
        }

        /**
         * Adapted from jython PySlice implementation.
         * @param len The count of arguments.
         * @return A new slice object.
         */
        public Slice indices(int len) {
            int start;
            int stop;
            int step;
            int slicelength;

            if (this.step == null) {
                step = 1;
            } else {
                step = this.step;
                if (step == 0) {
                    throw new IllegalArgumentException("slice step cannot be zero");
                }
            }

            if (this.start == null) {
                start = step < 0 ? len - 1 : 0;
            } else {
                start = this.start;
                if (start < 0) {
                    start += len;
                }
                if (start < 0) {
                    start = step < 0 ? -1 : 0;
                }
                if (start >= len) {
                    start = step < 0 ? len - 1 : len;
                }
            }

            if (this.stop == null) {
                stop = step < 0 ? -1 : len;
            } else {
                stop = this.stop;
                if (stop < 0) {
                    stop += len;
                }
                if (stop < 0) {
                    stop = -1;
                }
                if (stop > len) {
                    stop = len;
                }
            }

            if ((step < 0 && stop >= start) || (step > 0 && start >= stop)) {
                slicelength = 0;
            } else if (step < 0) {
                slicelength = (stop - start + 1) / (step) + 1;
            } else {
                slicelength = (stop - start - 1) / (step) + 1;
            }

            return new Slice(start, stop, step, slicelength);
        }

        /**
         * Automatically assign the right indices for the slice.
         * @param fullCount The full count of arguments.
         * @param start     The first argument.
         * @param stop      The first argument that should not show up.
         * @param step      The stride.
         * @return A new slice object that contains its length.
         */
        public static Slice get(int fullCount, Integer start, Integer stop, Integer step) {
            return new Slice(start, stop, step, null).indices(fullCount);
        }
    }

    private final Slice slice;

    /**
     * Creates a new slice iterable.
     * @param parent    The parent iterable.
     * @param start     The first item to return.
     * @param stop      The first item not to be returned.
     * @param step      The step.
     */
    SliceIterable(ArgumentIterable parent, Integer start, Integer stop, Integer step) {
        super(parent);
        this.slice = Slice.get(parent.count(), start, stop, step);
    }

    @Override
    public int count() {
        return this.slice.getLength();
    }

    @Override
    public <T> T getArgument(int index, Class<T> cls) {
        int rindex = this.getRealIndex(index);
        if (rindex == -1)
            throw new ArrayIndexOutOfBoundsException(index);
        return this.getParent().getArgument(this.slice.start + rindex*this.slice.step, cls);
    }
}
