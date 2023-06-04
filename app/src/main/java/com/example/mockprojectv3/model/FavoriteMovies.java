package com.example.mockprojectv3.model;

import static androidx.room.ForeignKey.CASCADE;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_movie" , foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id", childColumns = "user_id", onDelete = CASCADE))
public class FavoriteMovies extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favourite_movie_id")
    private int movieId;
    @ColumnInfo(name = "favourite_movie_title")
    private String title;
    @ColumnInfo(name = "favourite_movie_poster")
    private byte[] poster;
    @ColumnInfo(name = "favourite_movie_overview")
    private String overView;
    @ColumnInfo(name = "favourite_movie_vote_star")
    private double voteStar;
    @ColumnInfo(name = "favourite_movie_release_date")
    private String releaseDate;
    @ColumnInfo(name = "user_id")
    private int userId;

    public FavoriteMovies(int movieId, String title, byte[] poster, String overView, double voteStar, String releaseDate, int userId) {
        this.movieId = movieId;
        this.title = title;
        this.poster = poster;
        this.overView = overView;
        this.voteStar = voteStar;
        this.releaseDate = releaseDate;
        this.userId = userId;
    }

    @Ignore
    public FavoriteMovies() {
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }
    public double getVoteStar() {
        return voteStar;
    }

    public void setVoteStar(double voteStar) {
        this.voteStar = voteStar;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
