import java.util.List;
import java.util.function.Consumer;

public class Statistic {

    public void displayStatistics(boolean sEnable, boolean fEnable, FileReader fileReader) {
        if (sEnable) {
            displayShortStatistics("Strings", fileReader.getStrings().size());
            displayShortStatistics("Integers", fileReader.getIntegers().size());
            displayShortStatistics("Floats", fileReader.getFloats().size());
        }
        if (fEnable) {
            displayDetailedStatisticsForType(fileReader.getStrings(), this::stringStatistic, fileReader);
            displayDetailedStatisticsForType(fileReader.getIntegers(), this::intStatistic, fileReader);
            displayDetailedStatisticsForType(fileReader.getFloats(), this::doubleStatistic, fileReader);
        }
    }

    private void stringStatistic(FileReader fileReader) {
        int count = fileReader.getStrings().size();
        int minLength = fileReader.getStrings().stream().mapToInt(String::length).min().orElse(0);
        int maxLength = fileReader.getStrings().stream().mapToInt(String::length).max().orElse(0);

        List<String> shortestStrings = fileReader.getStrings().stream()
                .filter(s -> s.length() == minLength)
                .toList();
        List<String> longestStrings = fileReader.getStrings().stream()
                .filter(s -> s.length() == maxLength)
                .toList();

        System.out.println("Detailed statistics for strings:" +
                "\n- Count of recorded strings: " + count +
                "\n- Shortest strings: " + shortestStrings +
                "\n- Count of characters in shortest strings: " + minLength +
                "\n- Longest strings: " + longestStrings +
                "\n- Count of characters in longest strings: " + maxLength);
    }

    private void intStatistic(FileReader fileReader) {
        int count = fileReader.getIntegers().size();
        long min = fileReader.getIntegers().stream().min(Long::compare).orElse(0L);
        long max = fileReader.getIntegers().stream().max(Long::compare).orElse(0L);
        double sum = fileReader.getIntegers().stream().mapToDouble(Long::doubleValue).sum();
        double average = fileReader.getIntegers().stream().mapToDouble(Long::doubleValue).average().orElse(0.0);

        System.out.println("Detailed statistics for integers:" +
                "\n- Count of recorded integers: " + count +
                "\n- Minimum value: " + min +
                "\n- Maximum value: " + max +
                "\n- Sum of integers: " + sum +
                "\n- Arithmetic mean of integers: " + average);
    }

    private void doubleStatistic(FileReader fileReader) {
        int count = fileReader.getFloats().size();
        double min = fileReader.getFloats().stream().min(Double::compare).orElse(0.0);
        double max = fileReader.getFloats().stream().max(Double::compare).orElse(0.0);
        double sum = fileReader.getFloats().stream().mapToDouble(Double::doubleValue).sum();
        double average = fileReader.getFloats().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        System.out.println("Detailed statistics for floats:" +
                "\n- Count of recorded floats: " + count +
                "\n- Minimum value: " + min +
                "\n- Maximum value: " + max +
                "\n- Sum of floats numbers: " + sum +
                "\n- Arithmetic mean of floats: " + average);
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