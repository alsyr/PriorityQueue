
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScriptReader {

    private MinHeap minHeap;

    /**
     * Public constructor to read and process a script text file This text file include function calls for heap operations.
     */
    public ScriptReader(MinHeap minHeap, String scriptName) {

	this.minHeap = minHeap;

	Charset charset = Charset.forName("US-ASCII");
	Path path = Paths.get(scriptName);
	try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
	    String line;
	    while ((line = reader.readLine()) != null) {
		processScriptLine(line);
	    }

	} catch (Exception e) {
	}
    }

    /**
     * Method to check if the string matches the method names. If yes, then process and run the according methods If no, return 0
     *
     * @param line an input line
     * @return 1 = insert, 2 = decreaseKey, 3 = increaseKey, 4 = deleteMin, 5 = delete, 0 = no match
     */
    public int processScriptLine(String line) {
	if (line.contains("insert")) {
	    String subLine = line.substring(line.indexOf('t') + 1);
	    subLine.trim();

	    //Check to see if X is a number in [insert X]
	    if (!isANumber(subLine)) {
		return 0;
	    } else {
		Double newEntry = Double.parseDouble(subLine);
		minHeap.insert(newEntry);
		System.out.println(minHeap.toString());
		return 1;
	    }

	} else if (line.contains("decreaseKey") && hasTwoBrackets(line) && containsComma(line)) {

	    Integer[] arguments = extractArgumentsFrom(line);

	    // if the arguments are invalid, it is null, we do nothing and return 0
	    if (arguments == null) {
		return 0;
	    }

	    minHeap.decreaseKey(arguments[0], arguments[1]);// [0] is p, [1] is delta
	    System.out.println(minHeap.toString());
	    return 2;

	} else if (line.contains("increaseKey") && hasTwoBrackets(line) && containsComma(line)) {

	    Integer[] arguments = extractArgumentsFrom(line);

	    // if the arguments are invalid, it is null, we do nothing and return 0
	    if (arguments == null) {
		return 0;
	    }

	    minHeap.increaseKey(arguments[0], arguments[1]);// [0] is p, [1] is delta
	    System.out.println(minHeap.toString());
	    return 3;

	} else if (line.contains("deleteMin")) {
	    minHeap.deleteMin();
	    System.out.println(minHeap.toString());
	    return 4;

	} else if (line.contains("delete") && hasTwoBrackets(line)) {

	    int node = (int) extractArgumentFrom(line);

	    if (node == -1) {
		return 0;
	    }

	    minHeap.delete(node);
	    System.out.println(minHeap.toString());
	    return 5;
	} else {
	    return 0;
	}
    }

    /**
     * Method to extract arguments as integers from an input line
     *
     * @param line an input line
     * @return an array of integers containing 2 int arguments, {-1,-1} if the input lacks either one or both the arguments or the arguments is not a number
     */
    public Integer[] extractArgumentsFrom(String line) {
	try {
	    String subLine = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
	    String[] arguments = subLine.split(",");
	    int p = Integer.parseInt(arguments[0].trim());
	    int delta = Integer.parseInt(arguments[1].trim());
	    return new Integer[]{p, delta};

	} catch (Exception ex) {
	    return null;
	}

    }

    /**
     * Method to extract an argument as double from an input line
     *
     * @param line an input line
     * @return an argument as a double, -1 if the input is invalid either because of argument is empty or it is not numeric.
     */
    public double extractArgumentFrom(String line) {
	try {
	    String subLine = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
	    double argument = Double.parseDouble(subLine.trim());
	    return argument;
	} catch (Exception ex) {
	    return -1;
	}
    }

    /**
     * Method to check whether a string is numeric
     *
     * @param string the given string
     * @return true if the string is numeric, otherwise false.
     */
    public boolean isANumber(String string) {
	try {
	    double number = Double.parseDouble(string);
	} catch (NumberFormatException exception) {
	    System.out.println(string.trim() + " is not a number");
	    return false;
	}
	return true;
    }

    /**
     * Method to check if the method call has two brackets
     *
     * @param line the given string
     * @return true if the method call has two brackets, false otherwise.
     */
    public boolean hasTwoBrackets(String line) {
	if (line.contains("(") && line.contains(")")) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Method to check if the method call has a comma
     *
     * @param line the giving string
     * @return true if the method call has a comma, false otherwise
     */
    public boolean containsComma(String line) {
	if (line.contains(",")) {
	    return true;
	} else {
	    return false;
	}
    }

}
