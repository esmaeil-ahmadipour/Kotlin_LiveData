package ir.ea2.kotlin_livedata.data.remote;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
    private String credentials;
    public BasicAuthInterceptor(String email , String password) {
        credentials=Credentials.basic(email, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .header("Authorization",credentials)
                .build();
        return chain.proceed(request);
    }
}
