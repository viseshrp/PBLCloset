package com.dbfall16.pblcloset.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.dbfall16.pblcloset.models.PblResponse;
import com.dbfall16.pblcloset.models.User;
import com.dbfall16.pblcloset.parsers.PblResponseParser;
import com.dbfall16.pblcloset.parsers.UserLoginParser;
import com.dbfall16.pblcloset.utils.APIConstants;

import java.nio.charset.Charset;

/**
 * Created by viseshprasad on 12/2/16.
 */

public class SignupAPI extends AppRequest<PblResponse> {

    public SignupAPI(String userType, String firstName, String lastName, String address, String city, String state,
                     String zip, String phone, String country, String dob, String email, String password, boolean subscription,
                     Response.Listener<PblResponse> listener, Response.ErrorListener errorListener) {
        super(Method.POST, APIConstants.SIGNUP, listener, errorListener);
        setShouldCache(false);
        setPriority(Request.Priority.IMMEDIATE);

        setHttpParams("userType", userType);
        setHttpParams("firstName", firstName);
        setHttpParams("secondName", lastName);
        setHttpParams("address", address);
        setHttpParams("city", city);
        setHttpParams("state", state);
        setHttpParams("zip", zip);
        setHttpParams("phone", phone);
        setHttpParams("country", country);
        setHttpParams("dob", dob);
        setHttpParams("email", email);
        setHttpParams("password", password);

        setHttpParams("subscription", String.valueOf(subscription));
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
