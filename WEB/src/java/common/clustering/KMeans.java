
package common.clustering;

import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.IntStream;
import smile.clustering.BBDTree;
import smile.clustering.CentroidClustering;
import smile.math.MathEx;

public class KMeans extends CentroidClustering<double[], double[]> {
    private static final long serialVersionUID = 2L;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KMeans.class);

    /**
     * Constructor.
     * @param distortion the total distortion.
     * @param centroids the centroids of each cluster.
     * @param y the cluster labels.
     */
    public KMeans(double distortion, double[][] centroids, int[] y) {
        super(distortion, centroids, y);
    }

    @Override
    protected double distance(double[] x, double[] y) {
        return MathEx.squaredDistance(x, y);
    }

    /**
     * Partitions data into k clusters up to 100 iterations.
     * @param data the input data of which each row is an observation.
     * @param k the number of clusters.
     * @return the model.
     */
    public static KMeans fit(double[][] data, int k) {
        return fit(data, k, 100, 1E-4);
    }

    /**
     * Partitions data into k clusters up to 100 iterations.
     * @param data the input data of which each row is an observation.
     * @param k the number of clusters.
     * @param maxIter the maximum number of iterations.
     * @param tol the tolerance of convergence test.
     * @return the model.
     */
    public static KMeans fit(double[][] data, int k, int maxIter, double tol) {
        return fit(new BBDTree(data), data, k, maxIter, tol);
    }

    /**
     * Partitions data into k clusters.
     * @param bbd the BBD-tree of data for fast clustering.
     * @param data the input data of which each row is an observation.
     * @param k the number of clusters.
     * @param maxIter the maximum number of iterations.
     * @param tol the tolerance of convergence test.
     * @return the model.
     */
    public static KMeans fit(BBDTree bbd, double[][] data, int k, int maxIter, double tol) {
        if (k < 2) {
            throw new IllegalArgumentException("Invalid number of clusters: " + k);
        }

        if (maxIter <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIter);
        }

        int n = data.length;
        int d = data[0].length;

        int[] y = new int[n];
        double[][] medoids = new double[k][];

        double distortion = MathEx.sum(seed(data, medoids, y, MathEx::squaredDistance));
        logger.info(String.format("Distortion after initialization: %.4f", distortion));

        // Initialize the centroids
        int[] size = new int[k];
        double[][] centroids = new double[k][d];
        updateCentroids(centroids, data, y, size);

        double[][] sum = new double[k][d];
        double diff = Double.MAX_VALUE;
        for (int iter = 1; iter <= maxIter && diff > tol; iter++) {
            double wcss = bbd.clustering(centroids, sum, size, y);

            logger.info(String.format("Distortion after %3d iterations: %.4f", iter, wcss));
            diff = distortion - wcss;
            distortion = wcss;
        }

        return new KMeans(distortion, centroids, y);
    }

    /**
     * The implementation of Lloyd algorithm as a benchmark. The data may
     * contain missing values (i.e. Double.NaN). The algorithm runs up to
     * 100 iterations.
     * @param data the input data of which each row is an observation.
     * @param k the number of clusters.
     * @return the model.
     */
    public static KMeans lloyd(double[][] data, int k) {
        return lloyd(data, k, 100, 1E-4);
    }

    /**
     * The implementation of Lloyd algorithm as a benchmark. The data may
     * contain missing values (i.e. Double.NaN).
     * @param data the input data of which each row is an observation.
     * @param k the number of clusters.
     * @param maxIter the maximum number of iterations.
     * @param tol the tolerance of convergence test.
     * @return the model.
     */
    public static KMeans lloyd(double[][] data, int k, int maxIter, double tol) {
        if (k < 2) {
            throw new IllegalArgumentException("Invalid number of clusters: " + k);
        }

        if (maxIter <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIter);
        }

        int n = data.length;
        int d = data[0].length;

        int[] y = new int[n];
        double[][] medoids = new double[k][];

        double distortion = MathEx.sum(seed(data, medoids, y, MathEx::squaredDistanceWithMissingValues));
        logger.info(String.format("Distortion after initialization: %.4f", distortion));

        int[] size = new int[k];
        double[][] centroids = new double[k][d];
        // The number of non-missing values per cluster per variable.
        int[][] notNaN = new int[k][d];

        double diff = Double.MAX_VALUE;
        for (int iter = 1; iter <= maxIter && diff > tol; iter++) {
            updateCentroidsWithMissingValues(centroids, data, y, size, notNaN);

            double wcss = assign(y, data, centroids, MathEx::squaredDistanceWithMissingValues);
            logger.info(String.format("Distortion after %3d iterations: %.4f", iter, wcss));

            diff = distortion - wcss;
            distortion = wcss;
        }

        // In case of early stop, we should recalculate centroids.
        if (diff > tol) {
            updateCentroidsWithMissingValues(centroids, data, y, size, notNaN);
        }

        return new KMeans(distortion, centroids, y) {
            @Override
            public double distance(double[] x, double[] y) {
                return MathEx.squaredDistanceWithMissingValues(x, y);
            }
        };
    }
    
    static void updateCentroids(double[][] centroids, double[][] data, int[] y, int[] size) {
        int n = data.length;
        int k = centroids.length;
        int d = centroids[0].length;

        Arrays.fill(size, 0);
        IntStream.range(0, k).parallel().forEach(cluster -> {
            Arrays.fill(centroids[cluster], 0.0);
            for (int i = 0; i < n; i++) {
                if (y[i] == cluster) {
                    size[cluster]++;
                    for (int j = 0; j < d; j++) {
                        centroids[cluster][j] += data[i][j];
                    }
                }
            }

            for (int j = 0; j < d; j++) {
                centroids[cluster][j] /= size[cluster];
            }
        });
    }
    
    
    static <T> double assign(int[] y, T[] data, T[] centroids, ToDoubleBiFunction<T, T> distance) {
        int k = centroids.length;

        return IntStream.range(0, data.length).parallel().mapToDouble(i -> {
            double nearest = Double.MAX_VALUE;
            for (int j = 0; j < k; j++) {
                double dist = distance.applyAsDouble(data[i], centroids[j]);
                if (nearest > dist) {
                    nearest = dist;
                    y[i] = j;
                }
            }
            return nearest;
        }).sum();
    }

    
    static void updateCentroidsWithMissingValues(double[][] centroids, double[][] data, int[] y, int[] size, int[][] notNaN) {
        int n = data.length;
        int k = centroids.length;
        int d = centroids[0].length;

        IntStream.range(0, k).parallel().forEach(cluster -> {
            Arrays.fill(centroids[cluster], 0);
            Arrays.fill(notNaN[cluster], 0);
            for (int i = 0; i < n; i++) {
                if (y[i] == cluster) {
                    size[cluster]++;
                    for (int j = 0; j < d; j++) {
                        if (!Double.isNaN(data[i][j])) {
                            centroids[cluster][j] += data[i][j];
                            notNaN[cluster][j]++;
                        }
                    }
                }
            }

            for (int j = 0; j < d; j++) {
                centroids[cluster][j] /= notNaN[cluster][j];
            }
        });
    }
}

