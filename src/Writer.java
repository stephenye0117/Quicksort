/**
 * Writer class is responsible for recording and reporting various statistics
 * such as cache hits, disk reads, disk writes, and runtimes.
 * 
 * @author {Stephen Ye}
 * @version {10/19/2023}
 */
public class Writer {

    private static String fileName;
    private static int cacheHits = 0;
    private static int diskReads = 0;
    private static int diskWrites = 0;
    private static long executionTime = 0;

    /**
     * Sets the name of the file.
     * 
     * @param fileName
     *            The name of the file.
     */
    public static void setFileName(String fileName) {
        Writer.fileName = fileName;
    }


    /**
     * Gets the filename.
     * 
     * @return The number of cache hits.
     */
    public static String getFileName() {
        return fileName;
    }


    /**
     * Increments the count of cache hits.
     */
    public static void incrementCacheHits() {
        cacheHits++;
    }


    /**
     * Gets the number of cache hits.
     * 
     * @return The number of cache hits.
     */
    public static int getCacheHits() {
        return cacheHits;
    }


    /**
     * Increments the count of disk reads.
     */
    public static void incrementDiskReads() {
        diskReads++;
    }


    /**
     * Gets the number of disk reads.
     * 
     * @return The number of cache hits.
     */
    public static int getDiskReads() {
        return diskReads;
    }


    /**
     * Increments the count of disk writes.
     */
    public static void incrementDiskWrites() {
        diskWrites++;
    }


    /**
     * Gets the number of disk writes.
     * 
     * @return The number of cache hits.
     */
    public static int getDiskWrites() {
        return diskWrites;
    }


    /**
     * Adds a specified time to the total execution time.
     * 
     * @param time
     *            Time to be added to the total execution time.
     */
    public static void addExecutionTime(long time) {
        executionTime += time;
    }


    /**
     * gets the execution time
     * 
     * @return the execution time
     */
    public static long getExecutionTime() {
        return executionTime;
    }


    /**
     * Outputs the statistical information as a string.
     * 
     * @return A string representation of the statistical information.
     */
    public static String output() {
        return String.format("Sorting file: %s\nCache Hits: %d\nDisk Reads:"
            + " %d\nDisk Writes: %d\nRuntime is: %d", fileName, cacheHits,
            diskReads, diskWrites, executionTime);
    }


    /**
     * Resets all the statistical data to their initial states.
     */
    public static void reset() {
        cacheHits = 0;
        diskReads = 0;
        diskWrites = 0;
        executionTime = 0;
    }
}
