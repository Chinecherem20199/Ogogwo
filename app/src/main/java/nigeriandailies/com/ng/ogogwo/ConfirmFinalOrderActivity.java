package nigeriandailies.com.ng.ogogwo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameToConfirm, pnoneNumberToConfirm, addressToConfirm, cityToConfirm;
    private Button confirmOrderBtn;
    private String TotalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        nameToConfirm = findViewById(R.id.shipment_name);
        pnoneNumberToConfirm = findViewById(R.id.shipment_phone_number);
        addressToConfirm = findViewById(R.id.shipment_address);
        cityToConfirm = findViewById(R.id.shipment_city);
        confirmOrderBtn = findViewById(R.id.confirm_order_shipment_btn);

        TotalAmount = getIntent().getStringExtra("Total Price");

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();

            }
        });
    }

    private void check() {
        if (TextUtils.isEmpty(nameToConfirm.getText().toString())){
            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pnoneNumberToConfirm.getText().toString())){
            Toast.makeText(this, "Please proved your phone number.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressToConfirm.getText().toString())){
            Toast.makeText(this, "Please provide your home delivery address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cityToConfirm.getText().toString())){
            Toast.makeText(this, "Please provide your city name.", Toast.LENGTH_SHORT).show();
        }else {
            confirmOrder();
        }
    }

    private void confirmOrder() {

        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH: mm: ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhonenumber());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("TotalAmount", TotalAmount);
        ordersMap.put("name", nameToConfirm.getText().toString());
        ordersMap.put("phone", pnoneNumberToConfirm.getText().toString());
        ordersMap.put("address", addressToConfirm.getText().toString());
        ordersMap.put("city", confirmOrderBtn.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", " not shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("Users View")
                            .child(Prevalent.currentOnlineUser.getPhonenumber())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, " Your final order has been placed successfully.", Toast.LENGTH_SHORT)
                                                .show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);

//                                     Add flag intent so that the user will not comeback to this activity except if place another oder
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}
