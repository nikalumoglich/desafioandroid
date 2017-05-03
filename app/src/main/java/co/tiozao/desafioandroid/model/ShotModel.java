package co.tiozao.desafioandroid.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;
import java.util.TimeZone;

public class ShotModel implements Serializable {

    public int id;
    public String title;
    public String description;
    public int width;
    public int height;
    public ImageSet images;
    public int views_count;
    public int likes_count;
    public int comments_count;
    public int attachments_count;
    public int rebounds_count;
    public int buckets_count;
    public String created_at;
    public String updated_at;
    public String html_url;
    public String attachments_url;
    public String buckets_url;
    public String comments_url;
    public String likes_url;
    public String projects_url;
    public String rebounds_url;
    public boolean animated;
    public String[] tags;
    public User user;
    //public String team;

    public class ImageSet implements Serializable {
        public String hidpi;
        public String normal;
        public String teaser;
    }

    public class User implements Serializable {
        public int id;
        public String name;
        public String username;
        public String html_url;
        public String avatar_url;
        public String bio;
        public String location;
        public LinkSet links;
        public int buckets_count;
        public int comments_received_count;
        public int followers_count;
        public int followings_count;
        public int likes_count;
        public int likes_received_count;
        public int projects_count;
        public int rebounds_received_count;
        public int shots_count;
        public int teams_count;
        public boolean can_upload_shot;
        public String type;
        public boolean pro;
        public String buckets_url;
        public String followers_url;
        public String following_url;
        public String likes_url;
        public String projects_url;
        public String shots_url;
        public String teams_url;
        public String created_at;
        public String updated_at;

        public class LinkSet implements Serializable {
            public String web;
            public String twitter;
        }
    }

    public static class ComparatorByComments implements Comparator<ShotModel> {

        @Override
        public int compare(ShotModel shotModel, ShotModel other) {
            return other.comments_count - shotModel.comments_count;
        }
    }

    public static class ComparatorByRecent implements Comparator<ShotModel> {

        @Override
        public int compare(ShotModel shotModel, ShotModel other) {

            try{
                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));

                Timestamp shotTimestamp = new java.sql.Timestamp
                        (format.parse(shotModel.updated_at).getTime());
                Timestamp otherTimestamp = new java.sql.Timestamp
                        (format.parse(other.updated_at).getTime());

                return otherTimestamp.compareTo(shotTimestamp);
            }catch(Exception e){
                e.printStackTrace();
            }

            return 0;
        }
    }

    public static class ComparatorByViews implements Comparator<ShotModel> {

        @Override
        public int compare(ShotModel shotModel, ShotModel other) {
            return other.views_count - shotModel.views_count;
        }
    }
}