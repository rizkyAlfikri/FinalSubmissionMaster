package com.dicoding.picodiploma.finalsubmission.models.tvshowmodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowGenreResponse {

	@SerializedName("genres")
	private List<TvShowGenres> genres;

	public void setGenres(List<TvShowGenres> genres){
		this.genres = genres;
	}

	public List<TvShowGenres> getGenres(){
		return genres;
	}

	@Override
 	public String toString(){
		return 
			"TvShowGenreResponse{" +
			"genres = '" + genres + '\'' + 
			"}";
		}
}