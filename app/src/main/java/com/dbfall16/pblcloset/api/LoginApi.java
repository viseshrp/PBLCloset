package com.dbfall16.pblcloset.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.dbfall16.pblcloset.models.User;
import com.dbfall16.pblcloset.parsers.UserLoginParser;
import com.dbfall16.pblcloset.utils.APIConstants;

import java.nio.charset.Charset;

/**
 * Created by viseshprasad on 11/14/16.
 */

public class LoginApi extends AppRequest<User> {

    public LoginApi(String userName, String password, Response.Listener<User> listener, Response.ErrorListener errorListener) {
        super(Method.POST, APIConstants.LOGIN, listener, errorListener);
        setShouldCache(false);
        setPriority(Request.Priority.IMMEDIATE);

        setHttpParams("email", userName);
        setHttpParams("password", password);
    }

    @Override
    protected Response<User> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) {
            User user = new UserLoginParser(new String(response.data, Charset.forName("UTF-8"))).getParserResponse();
            return Response.success(user, null);
        } else if (response.statusCode == 401) {
            return Response.error(new AuthFailureError());
        } else {
            return Response.error(new NetworkError());
        }
    }
}
