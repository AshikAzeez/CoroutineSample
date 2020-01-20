package com.mobiotics.kotlincoroutinesample;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Ashik
 * Created on 10/1/20 .
 */
public class ConvertDuration {

    private static HashMap<String, String> regexMap = new HashMap<>();

    private static String regex2two = "(?<=[^\\d])(\\d)(?=[^\\d])";

    private static String two = "0$1";

    public static long getDuration() {

        String time = "PT1H".substring(2);
        long duration = 0L;
        Object[][] indexs = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
        for (int i = 0; i < indexs.length; i++) {
            int index = time.indexOf((String) indexs[i][0]);
            if (index != -1) {
                String value = time.substring(0, index);
                duration += Integer.parseInt(value) * (int) indexs[i][1] * 1000;
                time = time.substring(value.length() + 1);
            }
        }
        return duration;
    }

    public static void main(String[] args) {
        System.out.print(getDuration() + "");
        System.out.println("TimeUnit.SECONDS.toMillis(34):" + TimeUnit.SECONDS.toMillis(0));
        System.out.println("TimeUnit.MINUTES.toMillis(12):" + TimeUnit.MINUTES.toMillis(12));
        System.out.println("TimeUnit.HOURS.toMillis(1):" + TimeUnit.HOURS.toMillis(1));

    }
   /* @RequiresApi(api = VERSION_CODES.KITKAT)
    public static void main(String[] args) {

        regexMap.put("PT(\\d\\d)S", "00:$1");
        regexMap.put("PT(\\d\\d)M", "$1:00");
        regexMap.put("PT(\\d\\d)H", "$1:00:00");
        regexMap.put("PT(\\d\\d)M(\\d\\d)S", "$1:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)S", "$1:00:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M", "$1:$2:00");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M(\\d\\d)S", "$1:$2:$3");

        String[] dates = { "PT1S", "PT1M", "PT1H", "PT1M1S", "PT1H1S", "PT1H1M", "PT1H1M1S", "PT10H1M13S", "PT10H1S", "PT1M11S" };

        for (String date : dates) {
            String d = date.replaceAll(regex2two, two);
            String regex = getRegex(d);
            if (regex == null) {
                System.out.println(d + ": invalid");
                continue;
            }
            String newDate = d.replaceAll(regex, Objects.requireNonNull(regexMap.get(regex)));
            System.out.println(date + " : " +newDate);
        }
    }

    */

    private static String getRegex(String date) {
        for (String r : regexMap.keySet()) {
            if (Pattern.matches(r, date)) {
                return r;
            }
        }
        return null;
    }
}
