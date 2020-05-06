package com.charlezz.sample_java;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.charlezz.sample_java.databinding.ActivityMainBinding;

import life.sabujak.pickle.Pickle;
import life.sabujak.pickle.data.entity.PickleError;
import life.sabujak.pickle.ui.dialog.OnResultListener;
import life.sabujak.pickle.ui.dialog.PickleResult;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String[] items = new String[]{"Basic","Insta"};
    private ImageAdapter adapter = new ImageAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, items));
        binding.load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (items[binding.spinner.getSelectedItemPosition()]){
                    case "Basic":
                        Pickle.start(getSupportFragmentManager(), new OnResultListener() {
                            @Override
                            public void onSuccess(PickleResult result) {
                                adapter.submitList(result.getMediaList());
                            }

                            @Override
                            public void onError(PickleError error) {
                                Toast.makeText(MainActivity.this, error.getErrorCode().name(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case "Insta":
                        break;
                }
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerView.setAdapter(adapter);
        new PagerSnapHelper().attachToRecyclerView(binding.recyclerView);

    }
}
