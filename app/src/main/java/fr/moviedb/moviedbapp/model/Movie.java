package fr.moviedb.moviedbapp.model;

public class Movie {
    private int id;
    private String original_language;
    private String overview;
    private String release_date;
    private String poster_path;
    private String title;
    private String image;
    private String vote_average;

    public Movie(int id, String title, String image,String overview,String date,String language,String vote){
        this.id=id;
        this.title=title;
        this.image=image;
        this.overview=overview;
        this.release_date=date;
        this.original_language=language;
        this.vote_average=vote;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }
}
