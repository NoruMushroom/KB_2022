package com.example.kb_2022;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class AndClient {
    //서버 연결 객체
    static class requestServer{
        private static final String Server_URL = "http://123.215.162.92/KBServer/";
        private static Retrofit retrofit;

        static Retrofit getClient(){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            if(retrofit == null){
                retrofit = new Retrofit.Builder()
                        .baseUrl(Server_URL)
                        .addConverterFactory(new NullOnEmptyConverterFactory())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }
    }
    //null 예외처리 객체
    static class NullOnEmptyConverterFactory extends Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit)
        {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException
                {
                    if (body.contentLength() == 0) {
                        return null;
                    }
                    return delegate.convert(body);
                }
            };
        }
    }
    
    //서버 연결 인터페이스
    interface pwchangeInterface{
        @FormUrlEncoded
        @POST("pwChange.php")
        Call<pwchangeResponse> pwchangePost(
                @Field("userID") String userid,
                @Field("userPW") String userpw,
                @Field("changePW") String changepw
        );
    }
    interface signoutInterface{
        @FormUrlEncoded
        @POST("signout.php")
        Call<signoutResponse> singoutPost(
                @Field("userID") String userid,
                @Field("userPW") String userpw
        );
    }
    interface sguploadInterface{
        @Multipart
        @POST("upload.php")
        Call<sguploadResponse> sgUpload(@Part MultipartBody.Part image);
    }
    
    
    //response 받는 객체
    class pwchangeResponse{
        @SerializedName("success")
        boolean success;

        public boolean pwRespoense() {
            return success;
        }
    }
    class signoutResponse{
        @SerializedName("success")
        boolean success;
        public boolean signoutResponse(){
            return success;
        }
    }
    class sguploadResponse{
        @SerializedName("success")
        boolean success;
        @SerializedName("error")
        String error;
        public String sguploadResponse(){
            if (success = false){
                return error;
            }
            else{
                return "true";
            }
        }
    }

}
