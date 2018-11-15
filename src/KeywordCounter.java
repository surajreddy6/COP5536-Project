import fibheap.FibonacciHeap;
import fibheap.FibonacciHeapImpl;
import fibheap.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeywordCounter {

    public static void main(String[] args) {
        String inputFileName = args[0];
        // List of input strings to be processed
        List<String> inputs = readInputFile(inputFileName);
        // Hash table to store keywords and nodes
        HashMap<String, Node> hashTable = new HashMap<String, Node>();
        // Fibonacci Heap
        FibonacciHeap fibHeap = new FibonacciHeapImpl(hashTable);
        // Output file name
        String outputFile = "output_file.txt";

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(outputFile);
            bufferedWriter = new BufferedWriter(fileWriter);

            // Process input strings
            for (String input : inputs) {
                // if input string starts with '$' process it else print the top n keywords
                if (input.charAt(0) == '$') {
                    // split input into keyword and frequency
                    String[] k = input.replace("$", "").split("\\s");
                    String keyword = k[0];

                    int frequency = Integer.parseInt(k[1]);

                    // check if the keyword already exists
                    if (hashTable.containsKey(keyword)) {
                        // increase the count of an already existing keyword
                        fibHeap.increaseKey(keyword, frequency);
                    } else {
                        // insert keyword and frequency into the fibonacci heap
                        fibHeap.insert(keyword, frequency);
                    }
                } else {
                    List<Node> nodes = new ArrayList<Node>();
                    int n = Integer.parseInt(input);

                    for (int i = 0; i < n; i++) {
                        // remove max node from the fibonacci heap
                        Node node = fibHeap.removeMax();

                        // check if there are no elements in the heap
                        if (node == null) {
                            break;
                        }

                        // write keyword to output file
                        if (i != n-1) {
                            bufferedWriter.write(node.getKeyword() + ",");
                        }
                        else {
                            bufferedWriter.write(node.getKeyword());
                        }

                        nodes.add(node);
                    }

                    // re-insert the removed nodes into the fibonacci heap
                    for (Node node : nodes) {
                        fibHeap.insert(node.getKeyword(), node.getFrequency());
                    }

                    bufferedWriter.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }

                if (fileWriter != null) {
                    fileWriter.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }

        return lines;
    }
}
