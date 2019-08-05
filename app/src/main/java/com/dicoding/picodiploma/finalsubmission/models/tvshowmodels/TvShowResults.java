package com.dicoding.picodiploma.finalsubmission.models.tvshowmodels;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dicoding.picodiploma.finalsubmission.db.DatabaseContract;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.getColumnDouble;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.getColumnInt;
import static com.dicoding.picodiploma.finalsubmission.db.DatabaseContract.getColumnString;

public class TvShowResults implements Parcelable {

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("overview")
    private String overview;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("genre_ids")
    private List<Integer> genreIds;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("poster_path")
    private String posterPath;

    private String genre;

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

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

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public TvShowResults(String firstAirDate, String overview, String originalLanguage,
                         double popularity, double voteAverage, String name, int id, int voteCount,
                         String posterPath, String genre) {

        this.firstAirDate = firstAirDate;
        this.overview = overview;
        this.originalLanguage = originalLanguage;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.name = name;
        this.id = id;
        this.voteCount = voteCount;
        this.posterPath = posterPath;
        this.genre = genre;
    }

    public TvShowResults(Cursor cursor) {
        this.firstAirDate = getColumnString(cursor, DatabaseContract.TvShowColumns.DATE);
        this.overview = getColumnString(cursor, DatabaseContract.TvShowColumns.OVERVIEW);
        this.originalLanguage = getColumnString(cursor, DatabaseContract.TvShowColumns.LANGUAGE);
        this.popularity = getColumnDouble(cursor, DatabaseContract.TvShowColumns.POPULAR);
        this.voteAverage = getColumnDouble(cursor, DatabaseContract.TvShowColumns.VOTE_AVERAGE);
        this.name = getColumnString(cursor, DatabaseContract.TvShowColumns.TITLE);
        this.id = getColumnInt(cursor, DatabaseContract.TvShowColumns.ID);
        this.voteCount = getColumnInt(cursor, DatabaseContract.TvShowColumns.VOTE_COUNT);
        this.posterPath = getColumnString(cursor, DatabaseContract.TvShowColumns.POSTER);
        this.genre = getColumnString(cursor, DatabaseContract.TvShowColumns.GENRE);
    }

    @Override
    public String toString() {
        return
                "TvShowResults{" +
                        "first_air_date = '" + firstAirDate + '\'' +
                        ",overview = '" + overview + '\'' +
                        ",original_language = '" + originalLanguage + '\'' +
                        ",popularity = '" + popularity + '\'' +
                        ",vote_average = '" + voteAverage + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",genre_ids = '" + genreIds + '\'' +
                        ",vote_count = '" + voteCount + '\'' +
                        ",poster_path = '" + posterPath + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstAirDate);
        dest.writeString(this.overview);
        dest.writeString(this.originalLanguage);
        dest.writeDouble(this.popularity);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeList(this.genreIds);
        dest.writeInt(this.voteCount);
        dest.writeString(this.posterPath);
        dest.writeString(this.genre);
    }


    protected TvShowResults(Parcel in) {
        this.firstAirDate = in.readString();
        this.overview = in.readString();
        this.originalLanguage = in.readString();
        this.popularity = in.readDouble();
        this.voteAverage = in.readDouble();
        this.name = in.readString();
        this.id = in.readInt();
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.voteCount = in.readInt();
        this.posterPath = in.readString();
        this.genre = in.readString();
    }

    public static final Parcelable.Creator<TvShowResults> CREATOR = new Parcelable.Creator<TvShowResults>() {
        @Override
        public TvShowResults createFromParcel(Parcel source) {
            return new TvShowResults(source);
        }

        @Override
        public TvShowResults[] newArray(int size) {
            return new TvShowResults[size];
        }
    };
}