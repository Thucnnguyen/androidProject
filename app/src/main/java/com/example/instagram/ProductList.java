package com.example.instagram;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.databinding.ActivityProductListBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends AppCompatActivity {

    private RecyclerView rvItem;
    private WordListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        rvItem = findViewById(R.id.rcv_user);
        adapter = new WordListAdapter(this);

        GridLayoutManager grid = new GridLayoutManager(this, 3);
        rvItem.setLayoutManager(grid);

        adapter.setData(getListItem());
        rvItem.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private List<Product> getListItem() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Card item 1",0,0,null,null,1));
        list.add(new Product("Card item 2",0,0,null,null,2));
        list.add(new Product("Card item 3",0,0,null,null,3));
        list.add(new Product("Card item 4",0,0,null,null,4));

        list.add(new Product("Card item 5",0,0,null,null,5));
        list.add(new Product("Card item 6",0,0,null,null,6));
        list.add(new Product("Card item 7",0,0,null,null,7));
        list.add(new Product("Card item 8",0,0,null,null,8));

        list.add(new Product("Card item 9",0,0,null,null,9));
        list.add(new Product("Card item 10",0,0,null,null,10));
        list.add(new Product("Card item 11",0,0,null,null,11));
        list.add(new Product("Card item 12",0,0,null,null,12));

        return list;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_product_list);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}