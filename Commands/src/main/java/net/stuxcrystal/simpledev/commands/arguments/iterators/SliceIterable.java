package net.stuxcrystal.simpledev.commands.arguments.iterators;

/**
 * Returns the iterable at the given item.
 */
class SliceIterable extends ArgumentContainer {

    /**
     * Stores the values of a slice.
     */
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
        private final int len;

        /**
         * Creates a new slice object.
         * @param start The first argument.
         * @param stop  The second argument.
         * @param step  The step or stride.
         * @param len   The length of the slice.
         */
        private Slice(Integer start, Integer stop, Integer step, int len) {
            // Assign the values.
            this.start = start;
            this.stop = stop;
            this.step = step;
            this.len = len;
        }

        /**
         * Creates a new slice object for object with the given lenght.
         * @param len     The original length.
         * @param rStart  The start parameter.
         * @param rStop   The stop parameter.
         * @param rStep   The step parameter.
         * @return The new slice object.
         */
        public static Slice get(int len, Integer rStart, Integer rStop, Integer rStep) {
            int start;
            int stop;
            int step;
            int slicelength;

            if (rStep == null) {
                step = 1;
            } else {
                step = rStep;
                if (step == 0) {
                    throw new IllegalArgumentException("slice step cannot be zero");
                }
            }

            if (rStart == null) {
                start = step < 0 ? len - 1 : 0;
            } else {
                start = rStart;
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

            if (rStop == null) {
                stop = step < 0 ? -1 : len;
            } else {
                stop = rStop;
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
    }

    /**
     * The slice to use.
     */
    private final Slice slice;

    /**
     * Creates a new slice iterable.
     * @param parent    The parent iterable.
     * @param start     The first item to return.
     * @param stop      The first item not to be returned.
     * @param step      The step.
     */
    SliceIterable(ArgumentContainer parent, Integer start, Integer stop, Integer step) {
        super(parent);
        this.slice = Slice.get(parent.size(), start, stop, step);
    }

    @Override
    public int size() {
        return this.slice.len;
    }

    @Override
    public <T> T get(int index, Class<? extends T> cls) {
        int rindex = this.getRealIndex(index);
        if (rindex == -1)
            throw new IndexOutOfBoundsException(this.outOfBoundsMsg(index));
        return this.getParent().get(this.slice.start + rindex * this.slice.step, cls);
    }
}
