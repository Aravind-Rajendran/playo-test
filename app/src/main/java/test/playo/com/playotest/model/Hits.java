package test.playo.com.playotest.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asthme on 10/15/17.
 */

public class Hits {
    int  hitsPerPage;
    String query;

    List<Item> hits = new ArrayList<>();

    public int getHitsPerPage() {
        return hitsPerPage;
    }

    public void setHitsPerPage(int hitsPerPage) {
        this.hitsPerPage = hitsPerPage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Item> getHits() {

        return hits;
    }

    public void setHits(List<Item> hits) {
        this.hits = hits;
    }

    public  static  class Item implements Serializable


    {
        String title;
        String url;
        String author;
        int points;
        int num_comments;

        HighLightResult _highlightResult;







    }


    public  static  class HighLightResult implements  Serializable
    {

        Title title;
        Url url;
        Author author;
        StoryText story_text;

        public Author getAuthor() {
            return author;
        }

        public Title getTitle() {
            return title;
        }

        public Url getUrl() {
            return url;
        }

        public void setAuthor(Author author) {
            this.author = author;
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public void setUrl(Url url) {
            this.url = url;
        }
    }

    public  static  class Title implements  Serializable
    {
        String value;
        String matchLevel;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMatchLevel() {
            return matchLevel;
        }

        public void setMatchLevel(String matchLevel) {
            this.matchLevel = matchLevel;
        }
    }

    public  static  class Url implements  Serializable
    {
        String value;
        String matchLevel;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMatchLevel() {
            return matchLevel;
        }

        public void setMatchLevel(String matchLevel) {
            this.matchLevel = matchLevel;
        }
    }
    public  static  class Author implements  Serializable
    {
        String value;
        String matchLevel;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMatchLevel() {
            return matchLevel;
        }

        public void setMatchLevel(String matchLevel) {
            this.matchLevel = matchLevel;
        }
    }

    public  static  class StoryText implements  Serializable
    {

        String value;
        String matchLevel;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMatchLevel() {
            return matchLevel;
        }

        public void setMatchLevel(String matchLevel) {
            this.matchLevel = matchLevel;
        }
    }






}
