package fibheap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String inputFileName = args[0];
        // List of input strings to be processed
        List<String> inputs = readInputFile(inputFileName);
        // Fibonacci Heap
        FibonacciHeap fibHeap = new FibonacciHeapImpl();
        // Process input strings
        for(String input : inputs) {
            // if input string starts with '$' process it else print the top n keywords
            if(input.charAt(0) == '$') {
                // split input into keyword and frequency
                String[] k = input.replace("$", "").split("\\s");
                String keyword = k[0];
                int frequency = Integer.parseInt(k[1]);
                // insert keyword and frequency into the fibonacci heap
                fibHeap.insert(keyword, frequency);
            } else {
                int n = Integer.parseInt(input);
                for (int i = 0; i < n ; i++) {
                    Node node = fibHeap.removeMax();
                    System.out.println(node.getKeyword());
                    System.out.println(node.getCount());
                }
            }
        }
    }

    private static List<String> readInputFile(String inputFileName) {
        List<String> lines = new ArrayList<String>();
        File inputFile = new File(inputFileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String text = null;

            while ((text = reader.readLine()) != null) {
                // Do not add the last line to the input list
                if(!text.equals(new String("stop"))) {
                    lines.add(text);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        return lines;
    }
}
