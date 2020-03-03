package com.citycollege.sdmd.reactiveprogramming;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface StudentsApi {

    static final String BASE_URL = "https://city-201617.appspot.com/_ah/api/students/v1/";
    static final String STUDENTS_URL = "student";

    @GET(STUDENTS_URL)
    Observable<ListResponse> getAllStudents();

    @POST(STUDENTS_URL)
    Call<Void> createStudent(@Body Student student);
}