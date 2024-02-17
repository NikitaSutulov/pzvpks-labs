import com.nikitasutulov.pzvpks.lab1.data.Data;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class OperationsTest {

    @Test
    public void testMultiplyMatrices() {
        int[][] m1 = {
                {1, 2, 3},
                {1, 2, 3},
                {1, 2, 3}
        };
        int[][] m2 = {
                {4, 5, 6},
                {4, 5, 6},
                {4, 5, 6}
        };

        int[][] expected = {
                {24, 30, 36},
                {24, 30, 36},
                {24, 30, 36}
        };

        int[][] result = Data.multiplyTwoMatrices(m1, m2);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testMultiplyingVectorAndMatrix() {
        int[] vector = {2, 1, -3};
        int[][] matrix = {
                {1, 2, -1, 2},
                {3, 0, 4, -2},
                {2, 3, 3, 5}
        };

        int[] expected = {-1, -5, -7, -13};

        int[] result = Data.multiplyVectorAndMatrix(vector, matrix);
        assertArrayEquals(expected, result);
    }
}
