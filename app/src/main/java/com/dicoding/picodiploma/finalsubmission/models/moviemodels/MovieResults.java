package com.dicoding.picodiploma.finalsubmission.models.moviemodels;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.getColumnDouble;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.getColumnInt;
import static com.dicoding.picodiploma.finalsubmission.db.moviedb.MovieDatabaseContract.getColumnString;

public class MovieResults implements Parcelable {

    @SerializedName("overview")
    private String overview;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("title")
    private String title;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("id")
    private int id;

    @SerializedName("vote_count")
    private int voteCount;

    private String genre;

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public MovieResults(String overview, String originalLanguage, String title, String posterPath,
                        String releaseDate, double voteAverage, double popularity,
                        int id, int voteCount, String genre) {

        this.overview = overview;
        this.originalLanguage = originalLanguage;
        this.title = title;
//        this.genreIds = genreIds;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.id = id;
        this.voteCount = voteCount;
        this.genre = genre;
    }


    public MovieResults(Cursor cursor) {
        this.overview = getColumnString(cursor, MovieDatabaseContract.MovieColumns.OVERVIEW);
        this.originalLanguage = getColumnString(cursor, MovieDatabaseContract.MovieColumns.LANGUAGE);
        this.title = getColumnString(cursor, MovieDatabaseContract.MovieColumns.TITLE);
        this.posterPath = getColumnString(cursor, MovieDatabaseContract.MovieColumns.POSTER);
        this.releaseDate = getColumnString(cursor, MovieDatabaseContract.MovieColumns.DATE);
        this.voteAverage = getColumnDouble(cursor, MovieDatabaseContract.MovieColumns.VOTE_AVERAGE);
        this.popularity = getColumnDouble(cursor, MovieDatabaseContract.MovieColumns.POPULAR);
        this.id = getColumnInt(cursor, MovieDatabaseContract.MovieColumns.ID);
        this.voteCount = getColumnInt(cursor, MovieDatabaseContract.MovieColumns.VOTE_COUNT);
        this.genre = getColumnString(cursor, MovieDatabaseContract.MovieColumns.GENRE);

    }

    @NonNull
    @Override
    public String toString() {
        return
                "MovieResults{" +
                        "overview = '" + overview + '\'' +
                        ",original_language = '" + originalLanguage + '\'' +
                        ",title = '" + title + '\'' +
                        ",genre_ids = '" + genreIds + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        ",release_date = '" + releaseDate + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",id = '" + id + '\'' +
                        ",vote_count = '" + voteCount + '\'' +
                        "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.overview);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.title);
        dest.writeList(this.genreIds);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.voteAverage);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.id);
        dest.writeInt(this.voteCount);
        dest.writeString(this.genre);
    }

    protected MovieResults(Parcel in) {
        this.overview = in.readString();
        this.originalLanguage = in.readString();
        this.title = in.readString();
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readDouble();
        this.popularity = in.readDouble();
        this.id = in.readInt();
        this.voteCount = in.readInt();
        this.genre = in.readString();
    }

    public static final Creator<MovieResults> CREATOR = new Creator<MovieResults>() {
        @Override
        public MovieResults createFromParcel(Parcel source) {
            return new MovieResults(source);
        }

        @Override
        public MovieResults[] newArray(int size) {
            return new MovieResults[size];
        }
    };
}