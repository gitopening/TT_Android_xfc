package com.mogujie.tt.ui.widget;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.mogujie.tt.utils.CommonUtil;
import com.squareup.okhttp.internal.DiskLruCache;
import com.squareup.okhttp.internal.Util;

import org.apache.commons.io.IOUtils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhujian on 15/3/26.
 */
public class GifLoadTask extends AsyncTask<String, Void, byte[]> {

    public GifLoadTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(final String... params) {
        final String gifUrl = params[0];
        if (gifUrl == null)
            return null;
        byte[] gif = null;
        try {
            gif = byteArrayHttpClient(gifUrl);
        } catch (OutOfMemoryError e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gif;
    }

    private FilterInputStream getFromCache(String url) throws Exception {
        DiskLruCache cache = DiskLruCache.open(CommonUtil.getImageSavePath(), 1, 2, 2*1024*1024);
        cache.flush();
        String key = Util.hash(url);
        final DiskLruCache.Snapshot snapshot;
        try {
            snapshot = cache.get(key);
            if (snapshot == null) {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
        FilterInputStream bodyIn = new FilterInputStream(snapshot.getInputStream(1)) {
            @Override
            public void close() throws IOException {
                snapshot.close();
                super.close();
            }
        };
        return bodyIn;
    }

    public byte[] byteArrayHttpClient(final String urlString) throws Exception {
		OkHttpClient  client = null;
        if (client == null) {
			client = new okhttp3.OkHttpClient.Builder()
				.addInterceptor(interceptor)
				.cache(new Cache(CommonUtil.getImageSavePath(), 20 * 1024 * 1024))
				.connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
				.readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
				.build();
        }

		FilterInputStream inputStream = getFromCache(urlString);
		if (inputStream != null) {
			return IOUtils.toByteArray(inputStream);
		}
		InputStream in = null;
		try {
			final String decodedUrl = URLDecoder.decode(urlString, "UTF-8");
			final URL url = new URL(decodedUrl);
			final okhttp3.Request request = new Request.Builder().url(url).build();
			final Response response = client.newCall(request).execute();
			in = response.body().byteStream();
			return IOUtils.toByteArray(in);
		} catch (final MalformedURLException e) {
		} catch (final OutOfMemoryError e) {
		} catch (final UnsupportedEncodingException e) {
		} catch (final IOException e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ignored) {
				}
			}
		}
		return null;
    }
	Interceptor interceptor = new Interceptor() {
		@Override
		public okhttp3.Response intercept(Chain chain) throws IOException {
			okhttp3.Request request = chain.request();
			okhttp3.Response response = chain.proceed(request);

			String cacheControl = request.cacheControl().toString();
			if (TextUtils.isEmpty(cacheControl)) {
				cacheControl = "public, max-age=6000";
			}
			return response.newBuilder()
				.header("Cache-Control", cacheControl)
				.removeHeader("Pragma")
				.build();
		}
	};
}

