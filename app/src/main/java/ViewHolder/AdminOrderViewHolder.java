package ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import nigeriandailies.com.ng.ogogwo.R;

public  class AdminOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userName, userPhoneNumber, userTotalPrice,userDateTime, userShippingAdress;
    public Button showAllOrdersBtn;

    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.order_user_name);
        userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        userDateTime = itemView.findViewById(R.id.order_date_time);
        userShippingAdress = itemView.findViewById(R.id.order_address_city_name);
        showAllOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);





    }


    @Override
    public void onClick(View v) {

    }
}
