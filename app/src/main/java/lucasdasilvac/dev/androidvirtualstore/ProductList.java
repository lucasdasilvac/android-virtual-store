package lucasdasilvac.dev.androidvirtualstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import lucasdasilvac.dev.androidvirtualstore.Interface.ItemClickListener;
import lucasdasilvac.dev.androidvirtualstore.ViewHolder.ProductViewHolder;
import lucasdasilvac.dev.androidvirtualstore.model.Product;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryid = "";

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

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
                        Toast.makeText(ProductList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        //set adapter
        recyclerView.setAdapter(adapter);
    }
}
