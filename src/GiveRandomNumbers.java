import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jozef on 15.10.2014.
 * this code was created in case to show how medoids and centroids can be computed in 2D
 */
public class GiveRandomNumbers {
    public static int capacity = 120;
    public static float[][] numfield = new float[2][capacity];

    public static void printField(float[][] field) {

        for (int i = 0; i < capacity; i++) System.out.println(numfield[0][i] + "\t" + numfield[1][i]);

    }

    public static void deleteFile(){
        
    }
    public static float[] createRandomField(int min, int max) {
        float[] field = new float[capacity];
        for (int i = 0; i < capacity; i++) {
            Random rand = new Random();
            int randomNum1;
            randomNum1 = rand.nextInt((max - min) + 1) + min;
            field[i] = (float) randomNum1;

        }
        return field;
    }

    public static void createNumField(int min, int max) {

        float[] firstIndex;
        float[] secondIndex;

        firstIndex = createRandomField(min, max);
        secondIndex = createRandomField(min, max);
        controlForUnique(firstIndex, secondIndex, min, max);
        numfield = asignArray(firstIndex, secondIndex);

    }

    public static float[][] asignArray(float[] a, float[] b) {

        numfield[0] = a;
        numfield[1] = b;
        return numfield;
    }

    public static void controlForUnique(float[] a, float[] b, int min, int max) {
        for (int i = 0; i < capacity; i++) {
            while (a[i] == b[i]) {
                Random random = new Random();
                b[i] = random.nextInt((max - min) + 1) + min;
            }
        }

    }

    public static void printToFile(String path, float[][] file) {

        File fout = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (int i = 0; i < capacity; i++) {
            try {
                bw.write(file[0][i] + "\t" + file[1][i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static float[][] polynomialSecond(float[][] field) {
        float[][] editedField = new float[2][capacity];
        editedField[0] = field[0];
        for (int i = 0; i < capacity; i++) {
            float x = field[0][i];
            editedField[1][i] = x * x + 2 * x - 1;
        }

        return editedField;
    }

    public static float[][] polynomialBig(float[][] field) {
        float[][] editedField = new float[2][capacity];
        editedField[0] = field[0];
        for (int i = 0; i < capacity; i++) {
            float x = field[0][i];
            editedField[1][i] = (2 * x * x * x * x - 12 * x * x * x - 1584 * x + 12 * x * x - 584) / (3 * x * x * x - 8 * x);
        }

        return editedField;
    }

    public static float[][] editFieldTo2Fields(float[][] file) {
        float[][] field = file;
        for (int i = 0; i < capacity; i++) {
            field[0][i] = 2 * file[0][i];
            field[1][i] = 3 * file[1][i];
            i++;
        }

        return field;
    }

    public static float[][] createOutliers(float[][] file, float percent) {
        Random rand = new Random();
        float[][] field = file;
        for (int i = 0; i < capacity; i++) {
            int modulo = (int) (capacity / percent);
            if ((i % modulo) == 0) {
                if (rand.nextBoolean()) {
                    field[0][i] = (float) ((2.8) * file[0][i]);
                    field[1][i] = (float) ((1.7) * file[1][i]);
                } else {
                    field[0][i] = (float) ((3.2) * file[0][i]);
                    field[1][i] = (float) ((1.2) * file[1][i]);
                }
            }
            if (i % 50 == 0) {
                field[1][i] = (float) ((2.1) * file[1][i]);
            }
        }
        return field;
    }

    public static List<String> readFieldFromFile(String file) {
        float[][] field = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> stringList = new ArrayList<String>();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                stringList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (String aStringList : stringList) {
            // System.out.println(stringList.get(i));

        }


        return stringList;
    }

    public static float[][] parseToFloatArray(List<String> list) {
        float[][] field = new float[2][list.size()];
        for (int i = 0; i < list.size(); i++) {
            String[] split = list.get(i).split("\\\t");
            field[0][i] = Float.parseFloat(split[0]);
            field[1][i] = Float.parseFloat(split[1]);
        }


        return field;
    }

    public static float[] computeCentroid(float[][] inputField) {
        float[] field = new float[2];
        float sumX = (float) 0.0;
        float sumY = (float) 0.0;

        for (int i = 0; i < inputField[0].length; i++) {
            sumX += inputField[0][i];
            sumY += inputField[1][i];
        }
        sumX = sumX / inputField[0].length;
        sumY = sumY / inputField[0].length;

        field[0] = sumX;
        field[1] = sumY;

        return field;
    } // compute centroid of dataset in 2D

    public static float[] computeMedoid(float[][] mainField) {
        int bestIndex = 0;
        float bestAverageDistance = Float.MAX_VALUE;


        for (int i = 0; i < mainField[0].length; i++) {
            float cX = mainField[0][i], cY = mainField[1][i];
            float curResult = 0;
            for (int c = 0; c < mainField[0].length; c++) {
                if (c == i) continue;
                else {
                    //(ax - bx)^2 sqrt2 - euclidean distance
                    float nX = mainField[0][c], nY = mainField[1][c]; //
                    float result1 = (cX - nX) * (cX - nX), result2 = (cY - nY) * (cY - nY), result3 = (float) Math.sqrt((double) (result1 + result2));
                    curResult += result3;
                }
            }
            curResult = curResult / mainField[0].length;
            if (curResult < bestAverageDistance) {
                bestAverageDistance = curResult;
                bestIndex = i;
            }
        }
        float[] finalField = new float[2];
        finalField[0] = mainField[0][bestIndex];
        finalField[1] = mainField[1][bestIndex];
        return finalField;
    }  // compute medoid of dataset in 2D

    public static void main(String[] args) {
        createNumField(1000, 1500);
        // printField(numfield);

        float[][] editedField = numfield;
        // float[][] editedField = createOutliers(numfield,17);
        // float[][] editedField = editFieldTo2Fields(numfield);
        //float[][] editedField = polynomialSecond(numfield);
        // float[][] editedField = polynomialBig(numfield);

        //printToFile("Z:\\file.txt", editedField);


        List<String> list = readFieldFromFile("Z:\\classic-data.txt");
        float[][] array = parseToFloatArray(list);

        //float[][] editedField2 = createOutliers(array,17);
        float[] field = computeCentroid(array);
        System.out.println("centroid> x: " + field[0] + " y:" + field[1]);
        float[] medoid = computeMedoid(array);
        System.out.println("medoid> x: " + medoid[0] + " y:" + medoid[1]);
        //printToFile("Z:\\file-outliers.txt", editedField2);

    }


}
