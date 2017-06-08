package hackeru.edu.parsingxml;

import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YnetDataSource {
    //http://www.ynet.co.il/Integration/StoryRss2.xml
    public interface OnYnetArrivedListener{
        void onYnetArrived(List<Ynet> data);
    }

    public static void getYnet(final OnYnetArrivedListener listener){
        new AsyncTask<Void, Void, List<Ynet>>() {
            @Override
            protected List<Ynet> doInBackground(Void... params) {
                try {
                    String xml = IO.readWebSite("http://www.ynet.co.il/Integration/StoryRss2.xml", "Windows-1255");
                    List<Ynet> data = parse(xml);
                    return data;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Ynet> ynets) {
                listener.onYnetArrived(ynets);
            }
        }.execute();
    }

    private static List<Ynet> parse(String xml) {
        ArrayList<Ynet> data = new ArrayList<>();

        Document document = Jsoup.parse(xml);
        Elements elements = document.getElementsByTag("item");
        for (Element element : elements) {
            String title = element.getElementsByTag("title").first().text().replace("<![CDATA[", "").replace("]]>", "");
            String descriptionHTML = element.getElementsByTag("description").first().text();
            Document descriptionDocument = Jsoup.parse(descriptionHTML);
            String link = descriptionDocument.getElementsByTag("a").first().attr("href");
            String thumbnail = descriptionDocument.getElementsByTag("img").first().attr("src");
            String content = descriptionDocument.text();
            Ynet ynet = new Ynet(title, link, thumbnail, content);
            data.add(ynet);
        }

        return data;
    }

    public static class Ynet{
        private String title;
        private String link;
        private String thumbnail;
        private String content;

        //Constructor
        public Ynet(String title, String link, String thumbnail, String content) {
            this.title = title;
            this.link = link;
            this.thumbnail = thumbnail;
            this.content = content;
        }

        //Getters
        public String getTitle() {
            return title;
        }
        public String getLink() {
            return link;
        }
        public String getThumbnail() {
            return thumbnail;
        }
        public String getContent() {
            return content;
        }

        //toString
        @Override
        public String toString() {
            return "Ynet{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
