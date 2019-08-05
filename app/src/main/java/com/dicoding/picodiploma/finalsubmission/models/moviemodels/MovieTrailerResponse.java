package com.dicoding.picodiploma.finalsubmission.models.moviemodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerResponse {

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<MovieTrailer> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<MovieTrailer> results){
		this.results = results;
	}

	public List<MovieTrailer> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"MovieTrailerResponse{" +
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}