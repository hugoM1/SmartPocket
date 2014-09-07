package co.smartpocket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.smartpocket.R;
import co.smartpocket.model.Product;

/**
 * Created by hugo on 9/7/14.
 */
public class DrawerMatchesAdapter extends ArrayAdapter<Product> {

    private Context _context;
    private ArrayList<Product> _cards;

    public DrawerMatchesAdapter(Context context, int resource,
                                ArrayList<Product> objects) {
        super(context, resource, objects);
        this._context = context;
        this._cards = objects;
    }

//	public DrawerMatchesAdapter(Context context, ArrayList<Card> cards){
//		this._context = context;
//		this._cards = cards;
//	}

    @Override
    public int getCount() {
        return this._cards.size();
    }

    @Override
    public Product getItem(int position) {
        return _cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Product product =  getItem(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.item_cart_layout, null);
            holder = new ViewHolder();
            holder.imgView = (ImageView) convertView.findViewById(R.id.itemImage);
            holder.txtName = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
            holder.txtName.setText(String.valueOf(product.getProductPrice()));

        Glide.with(_context)
                .load(product.getProductImage())
                .into(holder.imgView);

        return convertView;
    }

    private class ViewHolder{
        ImageView imgView;
        TextView txtName, txtPrice, txtPriceSale;
    }
}
