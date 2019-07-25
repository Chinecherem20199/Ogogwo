package ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import Interface.ItemClickListner;
import nigeriandailies.com.ng.ogogwo.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProdctPrice,  txtProductQuantity;

    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProdctPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity= itemView.findViewById(R.id.cart_product_quantity);
    }

//    Called when a view has been clicked.
//     @param v The view that was clicked.
    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(),false);

    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;

    }
}
