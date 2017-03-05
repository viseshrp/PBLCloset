package com.dbfall16.pblcloset.parsers;

import com.dbfall16.pblcloset.models.PblResponse;
import com.dbfall16.pblcloset.models.User;
import com.dbfall16.pblcloset.utils.BaseApiParser;
import com.google.gson.Gson;

/**
 * Created by viseshprasad on 12/9/16.
 */

public class PblResponseParser extends BaseApiParser {
    public PblResponseParser(String response) {
        super(response);
    }

    @Override
    public PblResponse getParserResponse() {
        Gson gson = new Gson();
        return gson.fromJson(mResponse, PblResponse.class);
    }
}
