package hackeru.edu.parsingxml;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hackeru on 05/06/2017.
 */

public class CurrencyDataSource {
    public interface OnCurrenciesArrivedListener{
            void onCurrenciesArrived(List<Currency> currencies);
    }

    public static void getCurrencies(final OnCurrenciesArrivedListener listener) {
        AsyncTask<String, Integer, List<Currency>> asyncTask = new AsyncTask<String, Integer, List<Currency>>() {
            @Override
            protected List<Currency> doInBackground(String... params) {
                //code that runs in the background
                try {
                    String xml = IO.readWebSite("http://www.boi.org.il/currency.xml");
                    List<Currency> currencies = parse(xml);
                    return currencies;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(List<Currency> currencies) {
                //code that runs on the ui thread
                listener.onCurrenciesArrived(currencies);
            }
        };
        asyncTask.execute();
    }

    private static List<Currency> parse(String xml) {
        List<Currency> currencies = new ArrayList<>();
        Document document = Jsoup.parse(xml);
        Elements elements = document.getElementsByTag("CURRENCY");

        for (Element element : elements) {
            String name = element.getElementsByTag("NAME").first().text();
            int unit = Integer.parseInt(element.getElementsByTag("UNIT").first().text());
            String currencyCode = element.getElementsByTag("CURRENCYCODE").first().text();
            String country = element.getElementsByTag("COUNTRY").first().text();
            Double rate = Double.valueOf(element.getElementsByTag("RATE").first().text());
            Double change = Double.valueOf(element.getElementsByTag("CHANGE").first().text());

            currencies.add(new Currency(name, unit, currencyCode, country, rate, change));
        }
        return currencies;
    }


    //inner class POJO
    public static class Currency {
        private final String name;
        private final int unit;
        private final String currencyCode; //ILS USD NIS GBP
        private final String country;
        private final double rate;
        private final double change;

        //Constructor
        public Currency(String name, int unit, String currencyCode, String country, double rate, double change) {
            this.name = name;
            this.unit = unit;
            this.currencyCode = currencyCode;
            this.country = country;
            this.rate = rate;
            this.change = change;
        }

        //Getters
        public String getName() {
            return name;
        }

        public int getUnit() {
            return unit;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getCountry() {
            return country;
        }

        public double getRate() {
            return rate;
        }

        public double getChange() {
            return change;
        }

        //toString
        @Override
        public String toString() {
            return "Currency{" +
                    "name='" + name + '\'' +
                    ", unit=" + unit +
                    ", currencyCode='" + currencyCode + '\'' +
                    ", country='" + country + '\'' +
                    ", rate=" + rate +
                    ", change=" + change +
                    '}';
        }


        /*
        <NAME>Dollar</NAME>
        <UNIT>1</UNIT>
        <CURRENCYCODE>USD</CURRENCYCODE>
        <COUNTRY>USA</COUNTRY>
        <RATE>3.548</RATE>
        <CHANGE>-0.281</CHANGE>
        *
        * */
    }
}
