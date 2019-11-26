package com.worldline.nicolaldi.service;

import com.worldline.nicolaldi.model.StoreItem;
import com.worldline.nicolaldi.model.TransactionModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Nicola Verbeeck
 */
public interface NicolaldiService {

    @POST("/posts")
    Call<TransactionModel> sendTransaction(@Body TransactionModel model);

    @POST("/posts")
    Call<String> sendTransactionLog(@Body String logs);

}
