package lucasdasilvac.dev.androidvirtualstore.ViewHolder;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import lucasdasilvac.dev.androidvirtualstore.Interface.ItemClickListener;
import lucasdasilvac.dev.androidvirtualstore.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView product_name;
    public ImageView product_image, share_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_name = (TextView) itemView.findViewById(R.id.product_name);
        product_image = (ImageView) itemView.findViewById(R.id.product_image);
        share_image = (ImageView) itemView.findViewById(R.id.btnShare);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
