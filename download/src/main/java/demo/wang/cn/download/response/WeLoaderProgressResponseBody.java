package demo.wang.cn.download.response;

import java.io.IOException;

import demo.wang.cn.download.callback.WeLoaderProgressListener;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class WeLoaderProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private WeLoaderProgressListener weLoaderProgressListener;
    private BufferedSource bufferedSource;

    public WeLoaderProgressResponseBody(ResponseBody responseBody, WeLoaderProgressListener weLoaderProgressListener) {
        this.responseBody = responseBody;
        this.weLoaderProgressListener = weLoaderProgressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (null == bufferedSource) {
            bufferedSource = Okio.buffer(createSource(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source createSource(Source source) {
        return new ForwardingSource(source) {
            long currentRead;

            @Override
            public long read(Buffer sink, long byteCount) {
                try {
                    long read = super.read(sink, byteCount);
                    currentRead = read != -1 ? read : 0;
                    if (null != weLoaderProgressListener) {
                        weLoaderProgressListener.progress(responseBody.contentLength(), currentRead, read == -1);
                    }
                    return read;
                } catch (IOException e) {
                    if (null != weLoaderProgressListener) {
                        weLoaderProgressListener.progressException(e);
                    }
                }
                return currentRead;
            }
        };
    }
}
