import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws IOException {
        String  url = "https://www.pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 5000);
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if(matcher.find()){
            return matcher.group();
        }
        throw new Exception("Can't extract date from string");
    }
    private static int printPathValues(Elements values, int index){
        int iterationCount = 4;
        if(index == 0){
            Element valueLn = values.get(4);
            boolean isMorning = valueLn.text().contains("Утро");
            boolean isDay = valueLn.text().contains("День");
            boolean isEvening = valueLn.text().contains("Вечер");
            boolean isNight = valueLn.text().contains("Ночь");
            if(isNight){
                iterationCount = 5;
            }
            else if(isMorning){
                iterationCount = 4;
            }
            else if(isDay){
                iterationCount = 3;
            }
            else if(isEvening){
                iterationCount = 2;
            }
            else if(isMorning){
                iterationCount = 1;
            }
            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index++);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "    ");
                }
                System.out.println();
            }
        }
        else{
            for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index++);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "    ");
                }
                System.out.println();
            }
        }
        return iterationCount;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");

        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "     Явление     Температура     Давление    Влажность   Ветер");
            int iterationCount = printPathValues(values, index);
            index = index + iterationCount;
        }
    }
}
