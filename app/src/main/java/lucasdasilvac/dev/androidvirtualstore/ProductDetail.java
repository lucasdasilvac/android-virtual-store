package lucasdasilvac.dev.androidvirtualstore;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import lucasdasilvac.dev.androidvirtualstore.model.Product;

public class ProductDetail extends AppCompatActivity {

    TextView product_name, product_price, product_description;
    ImageView product_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String productid;

    FirebaseDatabase database;
    DatabaseReference product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //Firebase
        database = FirebaseDatabase.getInstance();
        product = database.getReference("Product");

        //init view
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);

        product_description = (TextView) findViewById(R.id.product_description);
        product_name = (TextView) findViewById(R.id.product_name);
        product_price = (TextView) findViewById(R.id.product_price);
        product_image = (ImageView) findViewById(R.id.img_product);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        //get product_id from intent
        if(getIntent() != null) {
            productid = getIntent().getStringExtra("productid");
        }
        if(!productid.isEmpty()) {
            getDetailProduct(productid);
        }
    }

    private void getDetailProduct(final String productid) {
        product.child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);

                //set image
                Picasso.with(getBaseContext()).load(product.getImage())
                        .into(product_image);

                collapsingToolbarLayout.setTitle(product.getName());

                product_price.setText(product.getPrice());
                product_name.setText(product.getName());
                product_description.setText(product.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
