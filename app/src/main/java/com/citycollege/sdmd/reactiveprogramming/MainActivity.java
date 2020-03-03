package com.citycollege.sdmd.reactiveprogramming;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private StudentsApi studentsApi;
    private ListView lvResults;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StudentsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        studentsApi = retrofit.create(StudentsApi.class);

        lvResults = findViewById(R.id.lv_results);

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStudentToServer();
            }
        });

        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStudentsFromServer();
            }
        });
    }

    @Override
    protected void onDestroy() {
        this.disposable.dispose();
        super.onDestroy();
    }

    private void sendStudentToServer() {
        String firstName = ((TextView) findViewById(R.id.txt_first_name)).getText().toString();
        String lastName = ((TextView) findViewById(R.id.txt_last_name)).getText().toString();
        String age = ((TextView) findViewById(R.id.txt_age)).getText().toString();

        studentsApi.createStudent(new Student(firstName, lastName, age))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            showToast(R.string.msg_student_created);
                        } else {
                            showToast(R.string.msg_student_not_created);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        showToast(R.string.msg_server_error);
                    }
                });
    }

    private void getStudentsFromServer() {
        disposable.add(
                studentsApi.getAllStudents()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<ListResponse, List<Student>>() {
                            @Override
                            public List<Student> apply(ListResponse listResponse) throws Exception {
                                return listResponse.getStudents();
                            }
                        })
                        .subscribe(new Consumer<List<Student>>() {
                            @Override
                            public void accept(List<Student> students) throws Exception {
                                lvResults.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, students));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                showToast(R.string.msg_server_error);
                            }
                        })
        );
    }

    private void showToast(int msgString) {
        Toast.makeText(this, msgString, Toast.LENGTH_SHORT).show();
    }
}
