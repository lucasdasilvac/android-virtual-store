package lucasdasilvac.dev.androidvirtualstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lucasdasilvac.dev.androidvirtualstore.Interface.ItemClickListener;
import lucasdasilvac.dev.androidvirtualstore.ViewHolder.ProductViewHolder;
import lucasdasilvac.dev.androidvirtualstore.Model.Product;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryid = "";

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

    //search
    FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Product");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get intent here
        if(getIntent() != null) {
            categoryid = getIntent().getStringExtra("categoryid");
        }
        if(!categoryid.isEmpty() && categoryid != null) {
            loadListProduct(categoryid);
        }

        //search
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Digite seu produto");
        loadSuggest(); // load suggest from firebase

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user type their text, change suggest list

                List<String> suggest = new ArrayList<>();
                for(String search : suggestList) {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) {
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is close, restore original adapter
                if(!enabled) {
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search finish, show result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("name").equalTo(text.toString()) //compare name

        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.product_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.product_image);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new activity
                        Intent productDetail = new Intent(ProductList.this, ProductDetail.class);
                        productDetail.putExtra("productid", searchAdapter.getRef(position).getKey()); //send product_id to new activity
                        startActivity(productDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter); //set adapter for recycler view in search result

    }

    private void loadSuggest() {
        productList.orderByChild("menuid").equalTo(categoryid)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Product item = postSnapShot.getValue(Product.class);
                    suggestList.add(item.getName()); //add name of product to suggest list
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListProduct(String categoryid) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("menuid").equalTo(categoryid)
                ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.product_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.product_image);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new activity
                        Intent productDetail = new Intent(ProductList.this, ProductDetail.class);
                        productDetail.putExtra("productid", adapter.getRef(position).getKey()); //send product_id to new activity
                        startActivity(productDetail);
                    }
                });
            }
        };
        //set adapter
        recyclerView.setAdapter(adapter);
    }
}
