import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class Statistic {

    public void displayStatistics(boolean sEnable, boolean fEnable, FileReader fileReader) {
        if (sEnable) {
            displayShortStatistics("Strings", fileReader.strings.size());
            displayShortStatistics("Integers", fileReader.integers.size());
            displayShortStatistics("Floating-point numbers", fileReader.doubles.size());
        }
        if (fEnable) {
            displayDetailedStatisticsForType(fileReader.strings, this::stringStatistic, fileReader);
            displayDetailedStatisticsForType(fileReader.integers, this::intStatistic, fileReader);
            displayDetailedStatisticsForType(fileReader.doubles, this::doubleStatistic, fileReader);
        }
    }

    private void stringStatistic(FileReader fileReader) {
        int count = fileReader.strings.size();
        String longest = fileReader.strings.stream().max(Comparator.comparing(String::length)).orElse("");
        String shortest = fileReader.strings.stream().min(Comparator.comparing(String::length)).orElse("");

        System.out.println("Detailed statistics for strings:" +
                "\n- Number of recorded strings: " + count +
                "\n- Longest string: " + longest +
                "\n- Number of characters in it: " + longest.length() +
                "\n- Shortest string: " + shortest +
                "\n- Number of characters in it: " + shortest.length());
    }

    private void intStatistic(FileReader fileReader) {
        int count = fileReader.integers.size();
        int min = fileReader.integers.stream().min(Integer::compare).orElse(0);
        int max = fileReader.integers.stream().max(Integer::compare).orElse(0);
        int sum = fileReader.integers.stream().mapToInt(Integer::intValue).sum();
        double average = fileReader.integers.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        System.out.println("Detailed statistics for integers:" +
                "\n- Number of recorded integers: " + count +
                "\n- Minimum value: " + min +
                "\n- Maximum value: " + max +
                "\n- Sum of integers: " + sum +
                "\n- Arithmetic mean of integers: " + average);
    }

    private void doubleStatistic(FileReader fileReader) {
        int count = fileReader.doubles.size();
        double min = fileReader.doubles.stream().min(Double::compare).orElse(0.0);
        double max = fileReader.doubles.stream().max(Double::compare).orElse(0.0);
        double sum = fileReader.doubles.stream().mapToDouble(Double::doubleValue).sum();
        double average = fileReader.doubles.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        System.out.println("Detailed statistics for floating-point numbers:" +
                "\n- Number of recorded floating-point numbers: " + count +
                "\n- Minimum value: " + min +
                "\n- Maximum value: " + max +
                "\n- Sum of floating-point numbers: " + sum +
                "\n- Arithmetic mean of floating-point numbers: " + average);
    }

    private <T> void displayDetailedStatisticsForType(List<T> dataList, Consumer<FileReader> statisticMethod,
                                                      FileReader fileReader) {
        if (!dataList.isEmpty()) {
            statisticMethod.accept(fileReader);
        }
    }

    private void displayShortStatistics(String type, int count) {
        if (count > 0) {
            System.out.println(type + " count: " + count);
        }
    }
}