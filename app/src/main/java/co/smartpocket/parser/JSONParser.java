package co.smartpocket.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.smartpocket.model.Product;
import co.smartpocket.model.Products;

/**
 * Created by hugo on 9/7/14.
 */
public class JSONParser {

    public Product productParser(JSONObject product){
        Product mProduct = new Product();
            try {

                mProduct.setProductName(product.getString("name"));
                mProduct.setProductImage(product.getString("image"));
                mProduct.setProductPrice(product.getString("price"));
                mProduct.setProductBrand(product.getString("brand"));
                mProduct.setProductCode(product.getString("code"));
                mProduct.setProductCategory(product.getString("category"));
                mProduct.setProductKey(product.getString("key"));

            }catch (JSONException e){
                e.printStackTrace();
            }
        return mProduct;
    }

    public Products productListParser(JSONArray jsonArray){
        Products mProducts = new Products();
        ArrayList<Product> products = new ArrayList<Product>();

        try {
            for (int i = 0; i < jsonArray.length(); i++){
                Product product = new Product();
                JSONObject ob = jsonArray.getJSONObject(i);

                product.setProductPrice(ob.getString("price"));
                product.setProductImage(ob.getString("image"));
                product.setProductName(ob.getString("name"));

                products.add(product);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        mProducts.setProducts(products);

        return mProducts;
    }
}
