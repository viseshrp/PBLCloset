package com.dbfall16.pblcloset.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.dbfall16.pblcloset.models.ItemList;
import com.dbfall16.pblcloset.models.PblResponse;
import com.dbfall16.pblcloset.parsers.ItemListParser;
import com.dbfall16.pblcloset.parsers.PblResponseParser;
import com.dbfall16.pblcloset.utils.APIConstants;

import java.nio.charset.Charset;

/**
 * Created by viseshprasad on 12/8/16.
 */

public class DonatedListApi extends AppRequest<PblResponse> {

    public DonatedListApi(String userId, Response.Listener<PblResponse> listener, Response.ErrorListener errorListener) {
        super(Method.POST, APIConstants.GET_DONATED_LIST_URL, listener, errorListener);
        setShouldCache(false);
        setPriority(Priority.IMMEDIATE);

        setHttpParams("donorId", userId);
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
