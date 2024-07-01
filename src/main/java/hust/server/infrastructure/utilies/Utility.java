package hust.server.infrastructure.utilies;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Utility {
    public static Integer random(Integer a, Integer b){
        Random random = new Random();

        // Sinh số ngẫu nhiên trong phạm vi từ a đến b
        return random.nextInt(b - a + 1) + a;
    }

    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // Bán kính trái đất ở đơn vị km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }
    public static Pageable paginationAndSorting(int page, int size, String sortBy){
        if (sortBy == null)return PageRequest.of(page, size);
        Pageable pageable;
        if (typeOfSort(sortBy).equals("desc")){
            pageable = PageRequest.of(page, size, Sort.by(getFieldOfSort(sortBy)).descending());
        }else{
            pageable = PageRequest.of(page, size);
        }
        return pageable;
    }
    public static LocalDate formatToDate(String date, String form){
        if (form == null || form.isEmpty())form = "dd/MM/yyyy";
        if (date == null)return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(form);

       return LocalDate.parse(date, formatter);
    }
    public static LocalDateTime formatToDateTime(String dateTime, String form){
        if (form == null || form.isEmpty())form = "dd/MM/yyyy HH:mm:ss";
        if (dateTime == null)return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(form);

        return LocalDateTime.parse(dateTime, formatter);
    }
    public static String formatDateTimeToString(LocalDateTime dateTime, String form){
        if (form == null || form.isEmpty())form = "dd/MM/yyyy HH:mm:ss";
        if (dateTime == null)return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(form);

       return dateTime.format(formatter);
    }
    public static String formatDateTimeToString(LocalDate dateTime, String form){
        if (form == null || form.isEmpty())form = "dd/MM/yyyy";
        if (dateTime == null)return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(form);

        return dateTime.format(formatter);
    }
    public static String typeOfSort(String sortBy){
        if (sortBy == null)return null;
        return sortBy.substring(sortBy.indexOf(':') + 1);
    }
    public static String getFieldOfSort(String sortBy){
        if (sortBy == null)return null;
        return  sortBy.substring(0, sortBy.indexOf(':'));
    }

    public static String toLocalDateTime(Timestamp timestamp, String format){
        return formatDateTimeToString(timestamp.toLocalDateTime(), format);
    }

    public static String formatCurrency(Long value) {
        Locale vietnam = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(vietnam);
        return format.format(value);
    }

    public static String concatPrefixWord(String word){
        String[] words = word.split(" ");
        StringBuilder result = new StringBuilder();
        for (String item : words) {
            result.append(item.charAt(0));
        }

        return result.toString().toUpperCase().trim(); //
    }

    public static String padWithZeros(String str, int length){
        StringBuilder sb = new StringBuilder();
        while (sb.length() + str.length() < length) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(padWithZeros("123", 10));
    }
}
