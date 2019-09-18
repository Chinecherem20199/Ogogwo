package nigeriandailies.com.ng.ogogwo.admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import nigeriandailies.com.ng.ogogwo.R;
import nigeriandailies.com.ng.ogogwo.Sellers.SellerProductCategoryActivity;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn, delectProductBtn;
    private EditText name, price, description;
    private ImageView imageView;

    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);


        applyChangesBtn = findViewById(R.id.apply_changes_btn);
        name = findViewById(R.id.product_maintain_name);
        price = findViewById(R.id.product_maintain_price);
        description = findViewById(R.id.product_maintain_description);
        imageView = findViewById(R.id.product_maintain_imageView);
        delectProductBtn = findViewById(R.id.delete_products_btn);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });
    }

    private void applyChanges() {
        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();

        if (pName.equals(""))
        {
            Toast.makeText(this, "Please write the product name", Toast.LENGTH_SHORT).show();
        }
        else  if (pPrice.equals(""))
        {
            Toast.makeText(this, "Please write the product price", Toast.LENGTH_SHORT).show();
        }
        else  if (pDescription.equals(""))
        {
            Toast.makeText(this, "Please write the product description", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("pname", pName);
            productMap.put("price", pPrice);
            productMap.put("description", pDescription);


            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                   
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
              if (dataSnapshot.exists())
              {
                  String pName = dataSnapshot.child("pname").getValue().toString();
                  String pPrice = dataSnapshot.child("price").getValue().toString();
                  String pDescription = dataSnapshot.child("description").getValue().toString();
                  String pImage = dataSnapshot.child("image").getValue().toString();


                  name.setText(pName);
                  price.setText(pPrice);
                  description.setText(pDescription);
                  Picasso.get().load(pImage).into(imageView);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        delectProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });
    }

    private void deleteThisProduct() {

        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent i = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(AdminMaintainProductsActivity.this, "The product has been deleted successfully.", Toast.LENGTH_SHORT).show();



            }
        });
    }
}
