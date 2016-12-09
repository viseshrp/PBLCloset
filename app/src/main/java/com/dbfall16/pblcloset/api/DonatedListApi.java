package com.dbfall16.pblcloset.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.dbfall16.pblcloset.models.ItemList;
import com.dbfall16.pblcloset.parsers.ItemListParser;
import com.dbfall16.pblcloset.utils.APIConstants;

import java.nio.charset.Charset;

/**
 * Created by viseshprasad on 12/8/16.
 */

public class DonatedListApi extends AppRequest<ItemList> {

    public DonatedListApi(String userId, Response.Listener<ItemList> listener, Response.ErrorListener errorListener) {
        super(Method.POST, APIConstants.GET_DONATED_LIST_URL, listener, errorListener);
        setShouldCache(false);
        setPriority(Priority.IMMEDIATE);

        setHttpParams("donorId", userId);
    }

    @Override
    protected Response<ItemList> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) {
            ItemList itemList = new ItemListParser(new String(response.data, Charset.forName("UTF-8"))).getParserResponse();
            return Response.success(itemList, null);
        } else if (response.statusCode == 401) {
            return Response.error(new AuthFailureError());
        } else {
            return Response.error(new NetworkError());
        }
    }
}
