package com.dbfall16.pblcloset.parsers;

import com.dbfall16.pblcloset.models.User;
import com.dbfall16.pblcloset.utils.BaseApiParser;
import com.google.gson.Gson;

/**
 * Created by viseshprasad on 11/14/16.
 */

public class UserLoginParser extends BaseApiParser {
    public UserLoginParser(String response) {
        super(response);
    }

    @Override
    public User getParserResponse() {
        Gson gson = new Gson();
        return gson.fromJson(mResponse, User.class);
    }
}
