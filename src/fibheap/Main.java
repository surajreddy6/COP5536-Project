package fibheap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

//    public static void main(String[] args) {
//        Test.test();
//    }

    public static void main(String[] args) {
        try {
            String inputFileName = args[0];
            // List of input strings to be processed
            List<String> inputs = readInputFile(inputFileName);

            // Fibonacci Heap
            FibonacciHeap fibHeap = new FibonacciHeapImpl();

            // Output file name
            String outputFile = "output_file.txt";
            FileWriter fileWriter = new FileWriter(outputFile);

            // Process input strings
            for (String input : inputs) {
                // if input string starts with '$' process it else print the top n keywords
                if (input.charAt(0) == '$') {

                    // split input into keyword and frequency
                    String[] k = input.replace("$", "").split("\\s");
                    String keyword = k[0];
                    int frequency = Integer.parseInt(k[1]);

                    // insert keyword and frequency into the fibonacci heap
                    fibHeap.insert(keyword, frequency);
                } else {
                    List<Node> nodes = new ArrayList<Node>();
                    int n = Integer.parseInt(input);

                    for (int i = 0; i < n; i++) {
                        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        // remove max node from fib heap
                        Node node = fibHeap.removeMax();

                        // write keyword to output file
                        if(i != n-1)
                            fileWriter.write(node.getKeyword() + ",");
                        else
                            fileWriter.write(node.getKeyword());

                        nodes.add(node);
                        System.out.println("+++++++++++++++++++++" + "Keyword: " + node.getKeyword() + " Count: " + node.getCount());
                        fibHeap.print();
                        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }

                    // re-insert the removed nodes into the fib heap
                    for (Node node : nodes) {
                        fibHeap.insert(node.getKeyword(), node.getCount());
                    }

                    fileWriter.write("\n");
                }
            }
            fibHeap.print();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
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
