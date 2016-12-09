package com.dbfall16.pblcloset.parsers;

import com.dbfall16.pblcloset.models.Item;
import com.dbfall16.pblcloset.models.ItemList;
import com.dbfall16.pblcloset.utils.BaseApiParser;
import com.google.gson.Gson;

/**
 * Created by viseshprasad on 12/8/16.
 */

public class ItemParser extends BaseApiParser {
    public ItemParser(String response) {
        super(response);
    }

    @Override
    public Item getParserResponse() {
        Gson gson = new Gson();
        return gson.fromJson(mResponse, Item.class);
    }

}
