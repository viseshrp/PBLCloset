package com.dbfall16.pblcloset.parsers;

import com.dbfall16.pblcloset.models.ItemList;
import com.dbfall16.pblcloset.utils.BaseApiParser;
import com.google.gson.Gson;

/**
 * Created by viseshprasad on 12/8/16.
 */

public class ItemListParser extends BaseApiParser {
    public ItemListParser(String response) {
        super(response);
    }

    @Override
    public ItemList getParserResponse() {
        Gson gson = new Gson();
        return gson.fromJson(mResponse, ItemList.class);
    }

}
