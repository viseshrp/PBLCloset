package com.dbfall16.pblcloset.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.dbfall16.pblcloset.models.Item;
import com.dbfall16.pblcloset.models.ItemList;
import com.dbfall16.pblcloset.models.PblResponse;
import com.dbfall16.pblcloset.parsers.ItemListParser;
import com.dbfall16.pblcloset.parsers.ItemParser;
import com.dbfall16.pblcloset.parsers.PblResponseParser;
import com.dbfall16.pblcloset.utils.APIConstants;

import java.nio.charset.Charset;

/**
 * Created by viseshprasad on 12/8/16.
 */

public class DonateItemApi extends AppRequest<PblResponse> {

    public DonateItemApi(String userId, String description, String color, String itemType, String size,
                         String brand, String picture, Response.Listener<PblResponse> listener,
                         Response.ErrorListener errorListener) {
        super(Method.POST, APIConstants.DONATE_ITEM_URL, listener, errorListener);
        setShouldCache(false);
        setPriority(Priority.IMMEDIATE);

        setHttpParams("donorId", userId);
        setHttpParams("description", description);
        setHttpParams("color", color);
        setHttpParams("itemType", itemType);
        setHttpParams("size", size);
        setHttpParams("brand", brand);
        setHttpParams("picture", picture);
    }

    @Override
    protected Response<PblResponse> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) {
            PblResponse pblResponse = new PblResponseParser(new String(response.data, Charset.forName("UTF-8"))).getParserResponse();
            return Response.success(pblResponse, null);
        } else if (response.statusCode == 401) {
            return Response.error(new AuthFailureError());
        } else {
            return Response.error(new NetworkError());
        }
    }
}
